package org.jfl110.hotreload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;


/**
 * Periodically scans the source directories, keeps track of the file update
 * times on the .class files in those directories and copies them to the target
 * directory if necessary.
 * 
 * On the first run the service will attempt to trigger a complete reload by
 * the dev server by changing the last update time on the appengine-web.xml file
 *
 * @author JFL110
 */
class HotReloadingService {

	private static final Pattern CLASS_FILE_PATTERN = Pattern.compile(".*.class");
	private static final String APP_ENGINE_XML_FILENAME = "appengine-web.xml";
	private static final Path CLASSES_DIR = Paths.get("classes/");

	private final Map<Path, Long> updateHistory = new HashMap<>();

	void startListening(Configuration configuration) {

		Path outputClassesDir = configuration.getWebInfOutputDirectory().resolve(CLASSES_DIR);

		boolean isFirst = true;

		while (true) {
			final Holder<Boolean> anyChanges = new Holder<>(false);

			configuration.getClassDirectoriesToMonitor().forEach((baseDir) -> {
				try {
					Files
					.walk(baseDir)
					.filter(Files::isRegularFile)
					.filter(isClass())
					.filter(needsUpdating())
							.forEach((path) -> {
								Path relativePath = path.subpath(baseDir.getNameCount(), path.getNameCount());
								Path destinationPath = outputClassesDir.resolve(relativePath);

								try {
									if (!Files.exists(destinationPath)) {
										Files.createDirectories(destinationPath);
									}
									Files.copy(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);

									updateHistory.put(path, Files.getLastModifiedTime(destinationPath).toMillis());

									anyChanges.set(true);
									log("Copied " + destinationPath);
								} catch (Exception e) {
									log(e);
								}
							});
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});

			if (anyChanges.get()) {
				if (isFirst) {
					appengineWebXmlReload(configuration.getWebInfOutputDirectory());
				}
				log("-------");
			}

			isFirst = false;

			try {
				Thread.sleep(configuration.getUpdateIntervalMs());
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	
	void reload(Path webInfDir){
		updateHistory.clear();
		appengineWebXmlReload(webInfDir);
	}
	

	private void appengineWebXmlReload(Path webInfDir) {
		log("Attempting complete reload");

		Path appengineXml = webInfDir.resolve(APP_ENGINE_XML_FILENAME);

		if (!Files.exists(appengineXml)) {
			log("Cannot reload! appengine-web.xml not found at [" + appengineXml + "]");
			return;
		}

		try {
			List<String> contents = Files.readAllLines(appengineXml);
			Files.write(appengineXml, contents, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Predicate<Path> needsUpdating() {
		return (path) -> {
			try {
				long diskUpdateTime = Files.getLastModifiedTime(path).toMillis();
				Long lastUpdateTime = updateHistory.get(path);

				if (lastUpdateTime == null || diskUpdateTime > lastUpdateTime) {
					return true;
				}
				return false;

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		};
	}

	private Predicate<Path> isClass() {
		return (path) -> CLASS_FILE_PATTERN.matcher(path.getFileName().toString()).matches();
	}

	private static void log(Object obj) {
		System.out.println(obj);
	}
}

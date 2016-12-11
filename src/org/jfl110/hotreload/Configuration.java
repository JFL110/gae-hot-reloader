package org.jfl110.hotreload;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The configuration for hot reloading.
 *
 * @author JFL110
 */
class Configuration {

	private Path webInfOutputDirectory;
	private final List<Path> classDirectoriesToMonitor = new ArrayList<>();
	private int updateIntervalMs;

	/**
	 * The WEB-INF target directory.
	 */
	Path getWebInfOutputDirectory() {
		return webInfOutputDirectory;
	}

	void setWebInfOutputDirectory(Path webInfOutputDirectory) {
		this.webInfOutputDirectory = webInfOutputDirectory;
	}

	/**
	 * List of source directories that contain .class files to reload.
	 */
	List<Path> getClassDirectoriesToMonitor() {
		return classDirectoriesToMonitor;
	}

	/**
	 * The update interval at which the source directories will be scanned
	 */
	int getUpdateIntervalMs() {
		return updateIntervalMs;
	}

	void setUpdateIntervalMs(int updateIntervalMs) {
		this.updateIntervalMs = updateIntervalMs;
	}

}

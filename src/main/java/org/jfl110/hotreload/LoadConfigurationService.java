package org.jfl110.hotreload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Parses the reload.properties file
 *
 * See the example reload.properties file.
 *
 * @author JFL110
 */
class LoadConfigurationService {
	
	private static final String CONFIGURATION_FILENAME = "reload.properties";
	private static final String INTERVAL_PROPERTY_KEY = "interval-ms";
	private static final String WEB_INF_DIR_PROPERTY_KEY = "webinf-dir";
	private static final String SOURCE_DIRECTORIES_PROPERTY_KEY = "source-dirs";
	private static final String PATH_DELIMITER = " ";

	/**
	 * @return the result of parsing the reload.properties file
	 * @throws IOException 
	 */
	Configuration loadConfiguration() throws IOException{
		
		InputStream input = LoadConfigurationService.class.getClassLoader()
				.getResourceAsStream(CONFIGURATION_FILENAME);
		
		if(input==null){
			throw new FileNotFoundException(CONFIGURATION_FILENAME);
		}
		
		Properties properties = new Properties();
		properties.load(input);
		
		Configuration configuration = new Configuration();
		
		configuration.setUpdateIntervalMs(Integer.parseInt(properties.getProperty(INTERVAL_PROPERTY_KEY)));
		configuration.setWebInfOutputDirectory(Paths.get(properties.getProperty(WEB_INF_DIR_PROPERTY_KEY)));
		
		for(String path : properties.getProperty(SOURCE_DIRECTORIES_PROPERTY_KEY).split(PATH_DELIMITER)){
			if(path == null || "".equals(path)){
				continue;
			}
			configuration.getClassDirectoriesToMonitor().add(Paths.get(path));
		}
		return configuration;
	}
}

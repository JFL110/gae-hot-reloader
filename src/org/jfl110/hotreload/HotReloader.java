package org.jfl110.hotreload;

import java.io.IOException;

/**
 * 
 *
 *
 * @author JFL110
 */
public class HotReloader {

	public static void main(String[] args) throws IOException {
		HotReloadingService hotReloadingService = new HotReloadingService();
		LoadConfigurationService loadConfigurationService = new LoadConfigurationService();
		
		hotReloadingService.startListening(loadConfigurationService.loadConfiguration());
	}
}
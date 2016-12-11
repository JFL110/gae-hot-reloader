package org.jfl110.hotreload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HotReloader entry point.
 * 
 * See README.md for more info.
 *
 * @author JFL110
 */
public class HotReloader {
	
	private static final String STOP_COMMAND = "STOP";
	private static final String FORCE_RELOAD_COMMAND = "RELOAD";

	public static void main(String[] args) throws IOException {
		
		final HotReloadingService hotReloadingService = new HotReloadingService();
		LoadConfigurationService loadConfigurationService = new LoadConfigurationService();
		
		final Configuration configuration  =loadConfigurationService.loadConfiguration();

		Thread workerThread = new Thread(() -> {
			try {
				hotReloadingService.startListening(configuration);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		
		workerThread.start();
		
		  
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			String command = reader.readLine();
			
			if(command == null){
				continue;
			}
			
			if(command.trim().toUpperCase().equals(STOP_COMMAND)){
				break;
			}
			
			if(command.trim().toUpperCase().equals(FORCE_RELOAD_COMMAND)){
				hotReloadingService.triggerCompleteReload(configuration.getWebInfOutputDirectory());
			}
		}
		
		workerThread.interrupt();
	}
}
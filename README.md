# GAE Hot Reloader

The goal of this tool is to enable hot reloading of classes while running the GAE development server. Changes made in the source code are quickly applied to the running application if an IDE that builds automatically is used (such as Eclipse).

This enables a loosely-coupled multi-project structure to be used without the need to re-explode the compiled war, re-install local maven dependencies or put any hacks into the build script.

When running, the tool:
* Scans the specified source directories for .class files and identifies which have been updated since the last scan.
* Copies the classes files from all source directories into the WEB-INF/classes directory.
* If necessary, triggers a complete reload of the server by re-saving the appengine-web.xml file. The first time the .class files are copied a complete reload is required otherwise the classes will be sourced from their .jar files.

### Running
The tool can be run using gradle from the command line using 'gradle run'.

**A reload is needed after starting the GAE development server.**

### Command-line instructions
* 'stop' - stops the tool.
* 'reload' - tries to trigger a reload of the server by re-saving the appengine-web.xml file.

### Configuration
Specify the following properties in the 'src/main/resources/reload.properties' file.
* 'interval-ms' - The number of milliseconds between each scan of the source directories.
* 'webinf-dir' - The path to the output WEB-INF directory.
* 'source-dirs' - Space-separated list of paths to scan for .class files. For Eclipse projects this would be '\bin' in the root project folder.

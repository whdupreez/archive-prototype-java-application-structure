# Java Application Structure

Folder structure:

```
+- app_home/
   +- bin/                          Application scripts
   |  +- launch
   |  +- launch.bat
   |  +- setenv
   |  +- setenv.bat
   +- data/                         Application and system data
   |  +- log/
   |  +- system/
   |  +- tmp/
   +- etc/                          Application configuration
   |  +- system*.properties         Reserved system properties
   +- lib/                          Application dependencies
   |  +- {project-name}.jar
   +- res/                          Private application resources
   +- res-public/                   Public application resources
   +- system/                       Application management tools
```

## Goals

 * No 3rd-party JARs
 * Not an application server or container, but becomes the application
 * Linux and Windows support
   * Install as service
 * Environment configuration (Java home, memory, etc.)
 * API to support structure
 * Provide Maven archetype
 * Maven plugin for packaging

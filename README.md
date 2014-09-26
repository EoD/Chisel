Chisel
=======
Chisel is a mod for Minecraft which adds aesthetic blocks to the game through the use of a chisel.  Originally created by AUTOMATIC_MAIDEN, it was ported to Minecraft 1.7.2 by asiekierka and contains features by both Pokefenn and Delta534.

Issue Reporting
----------------
If you wish to report an issue with the mod, please submit one to the issue tracker in this repository.  When creating an 
issue, please be sure to include...

- the version of Chisel you are using
- the version of Forge you are using
- the crash report (preferrably via [Gists](https://gist.github.com/) or [Pastebin](http://pastebin.com/)) generated by the error
- depending on the error, an image of the problem

Contributing
-------------
Chisel is open-source under the GPL v2 license.  As a result, you may contribute to the development of the mod via pull requests.  
To set up the mod as to allow you to make changes, do the following:

1. Clone the repository onto your local system.
2. Open a command prompt from the repository folder
  * **Windows**: Shift+right-click in the folder in your Windows Explorer and select "Open Command Window here"
  * **Linux**: You need to replace all ``gradlew.bat`` by ``./gradlew``
4. Run the command ``gradlew.bat setupDecompWorkspace --refresh-dependencies``.
5. Then, continue with either
  * IntelliJ IDEA
    1. run ``gradlew.bat idea`` to generate IntelliJ IDEA files
    2. Open IntelliJ and point to either the project folder or the build.gradle file.
    3. After opening the project in IntelliJ, run 'gradlew genIntellijRuns' in the command terminal you opened earlier.
    4. You now have a functional local copy of Chisel, ready to develop on.
 * Eclipse
    1. ``gradlew.bat eclipse`` to generate the project fielgis
    2. Open Eclipse and right click on the left pane and select "Import..."
    3. Select "Import Existing Project" and select the folder where your source code is
    4. Start developing!
6. To test the client, go back to the command line you opened before and type ``gradlew.bat runClient``

MAKE SURE TO UPDATE YOUR FORK BEFORE MAKING A PULL REQUEST.

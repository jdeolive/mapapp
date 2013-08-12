# MapApp

Sample application showcasing use of the GeoDroid map view component.

## Building

Building the application from sources requires:

* [Android SDK](http://developer.android.com/sdk/index.html)
* [Apache Maven](http://maven.apache.org/)

See the [GeoDroid README](https://github.com/jdeolive/geodroid) for more 
information about setting up the Android SDK.

Start by doing a submodule update to bring in the core GeoDroid library.

    git submodule --init update

Run the ``android`` command to update the project.

    android update project -s -p .

Change to the ``geodroid`` directory and build it with Maven.

    cd geodroid
    mvn install

Change back to the root directory and build the application.

    mvn install

The above should result in a file named ``MapApp-debug.apk`` being 
created in the ``bin`` directory.

## Installing

Install the app on a connected device with the ``adb`` command.

    cd bin
    adb install MapApp-debug.apk





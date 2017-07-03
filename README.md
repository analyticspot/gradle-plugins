# Overview

# Appying the Plugins

With the old plugin syntax you need something like the following in your
`build.gradle`:

```

buildscript {
    repositories {
        // our plugin is on jitpack
        maven { url 'https://jitpack.io' }
        // out plugin relies on some things that need to be fetched
        // from jcenter or mavenCentral
        jcenter()
    }
    dependencies {
        classpath "com.analyticspot:gradle-plugins:$PLUGIN_VERSION"
    }
}

apply plugin: 'com.analyticspot.javaLibrary'
```

If you are developing the plugin and you want to test local changes you can
add `mavenLocal()` to the repository list.

With the new plugin syntax you need to add the following to your **`settings.gradle`** (not your `build.gradle`):

```
pluginManagement {
    repositories {
        maven {
            url 'https://jitpack.io'
        }
    }
}

```

and then add the following to your `build.gradle`:

```
plugins {
    id 'com.analyticspot.javaLibrary' version '0.1'
}
```

The new `pluginManagement` block does not support snapshots or `mavenLocal()` though you can add something like
`maven { url '/home/username/.m2/repository' }` to the `repositories` block to get a limited version of local 
publishing working.

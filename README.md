# Overview

This is the standard Analytic Spot plugin for building Java libraries. Aside from applying the Gradle java plugin, 
this configures things in our standard way (e.g. uses our Lint files, runs tests with testNG, etc.). It also adds 
some convenience properties like the "provided" configuration. The following subsections explain the configuration 
this plugin applies.

## Java Configuration

We have a fairly standard Java setup except:

* We use Java 8.
* We configure things to use `mavenLocal()` and `jcenter()` as repositories.
* We have our own [Checkstyle](http://checkstyle.sourceforge.net/) configuration. It is based mostly on the [Google 
Java style guide](https://google.github.io/styleguide/javaguide.html) but diverges from that guide in a few places.
* We apply a custom [JavaDoc configuration](TODO: LINK HERE).

## Testing

We use testNG and add some custom hooks that report which tests failed at the end of the build if any fail.

## The Provided Configuration

The `provided` configuration allows developers to add a dependency as "provided" meaning you need the dependency on
your IDE classpath and when you compile, but when generating jars and listing transitive dependencies the dependency
should be ignored. Example use case would be a Spark job: the Spark libraries are used in the code and so are needed to
compile, but they're huge and already on the cluster and in the classpath so we don't want to include them in the
generated jar. To use just do something like:

```
dependencies {
   provided 'org.apache.spark:spark-core_2.11:2.1.1'
}
```


# Appying the Plugin

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
        classpath "com.github.oliverdain:gradle-plugins:$PLUGIN_VERSION"
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
            url 'https://jitpack.io' }
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

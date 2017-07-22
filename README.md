# Overview

![Release](https://jitpack.io/v/analyticspot/gradle-plugins.svg)

This is the standard Analytic Spot plugin for building Java libraries. Aside from applying the Gradle java plugin, 
this configures things in our standard way (e.g. uses our Lint files, runs tests with testNG, etc.). It also adds 
some convenience properties like the "provided" configuration. The following subsections explain the configuration 
this plugin applies.

## Java Configuration

We have a fairly standard Java setup except:

* We use Java 8.
* We configure things to use `mavenLocal()` and `jcenter()` as repositories.
* We have our own [Checkstyle](http://checkstyle.sourceforge.net/)
[configuration](./src/main/resources/checkstyle_config.xml). It is 
based mostly on the 
[Google Java style guide](https://google.github.io/styleguide/javaguide.html) but diverges from that guide in a few
places.

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
        classpath "com.github.analyticspot:gradle-plugins:$PLUGIN_VERSION"
    }
}

apply plugin: 'com.analyticspot.javaLibrary'
```

If you are developing the plugin and you want to test local changes you can add `mavenLocal()` to the repository list.

You can find the current version by looking at our [tags](tags) or the jitpack badge at the top of this README.

**Important**: currently you can only apply this plugin using the old plugin syntax. See the
[New Plugin Syntax Issues](#new-plugin-syntax) section for details on why and how this could be fixed.



## <a name="new-plugin-syntax"></a> New Plugin Synatx Issues

Since I'm publishing on jitpack without a [custom domain name](https://jitpack.io/docs/FAQ/) the [plugin marker 
artifact](https://docs.gradle.org/4.0/userguide/plugins.html#sec:plugin_markers) won't trigger jitpack to pull and 
build our project so the artifact won't exist and this **does not work**. However, we could later publish elsewhere or
add a custom domain name and make this work. If so this is how this **could** work with the new plugin syntax:

First, add the repository containing the plugin to **`settings.gradle`** (not your `build.gradle`) like so:

```
pluginManagement {
    repositories {
        maven {
            url 'https://repo.with/plugin'
        }
    }
}

```

and then add the following to your `build.gradle`:

```
plugins {
    id 'com.analyticspot.javaLibrary' version '0.2'
}
```

The new `pluginManagement` block does not support snapshots or `mavenLocal()` though you can add something like
`maven { url '/home/username/.m2/repository' }` to the `repositories` block to get a limited version of local 
publishing working.


import io.github.diegoflassa.template_android_app.buildsrc.Versions

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        jcenter() // Warning: this repository is going to shut down soon
        gradlePluginPortal()
    }
    dependencies {
        //classpath("com.android.tools.build:gradle:${Versions.gradle}")
        classpath("com.android.tools.build:gradle:7.2.0-alpha03")
        //classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_compiler_version}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30-M1")
        //classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.safeargs_plugin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.0-alpha07")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath("com.github.ben-manes:gradle-versions-plugin:0.38.0")
        //classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.version_hilt}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.40")
    }
}

plugins {
    id("com.diffplug.spotless") version "5.14.2" apply true
    id("com.github.ben-manes.versions") version "0.38.0" apply true
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")
            ktlint(Versions.ktlint).userData(mapOf("disabled_rules" to "no-wildcard-imports"))
            // Uncomment if needed
            // licenseHeaderFile("${project.rootProject.projectDir}/spotless/copyright.kt")
        }
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        jcenter() // Warning: this repository is going to shut down soon
    }
}
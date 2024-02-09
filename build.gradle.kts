

buildscript {
    repositories {
        google()
        mavenCentral()
        // JitPack repositoryni qo'shish
        maven(url = "https://jitpack.io")
    }
    dependencies {
        val nav_version = "2.7.6"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.library") version "8.2.2" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}

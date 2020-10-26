buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    dependencies {
        val kotlinVersion = "1.4.10"

        classpath ("com.android.tools.build:gradle:4.1.0")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.1")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.28.3-alpha")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
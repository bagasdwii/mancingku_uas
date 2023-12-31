

buildscript {
    val ktorVersion by extra("1.4.21")

    repositories {
        google()
        jcenter()

    }

    dependencies {
        val nav_version = "2.5.3"
        classpath("com.google.gms:google-services:4.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$ktorVersion")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")

        // Other dependencies specific to the build script can be added here.
    }
}



plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false


}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

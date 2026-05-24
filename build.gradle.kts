plugins {
    kotlin("multiplatform") version "1.9.10" apply false
    id("org.jetbrains.compose") version "1.5.1" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

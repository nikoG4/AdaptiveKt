rootProject.name = "adaptive-kt"
include(":adaptive-core")
include(":adaptive-layout")
include(":adaptive-feedback")
include(":adaptive-navigation")
include(":adaptive-forms")
include(":adaptive-data")
include(":adaptive-components")
include(":admin-demo")
include(":docs-site")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

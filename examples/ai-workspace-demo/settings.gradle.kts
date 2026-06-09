pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        if (providers.gradleProperty("useMavenLocalFallback").getOrElse("false").toBoolean()) {
            mavenLocal()
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "ai-workspace-demo"

includeBuild("../..") {
    dependencySubstitution {
        substitute(module("io.github.nikog4.adaptivekt:adaptive-core")).using(project(":adaptive-core"))
        substitute(module("io.github.nikog4.adaptivekt:adaptive-components")).using(project(":adaptive-components"))
        substitute(module("io.github.nikog4.adaptivekt:adaptive-layout")).using(project(":adaptive-layout"))
        substitute(module("io.github.nikog4.adaptivekt:adaptive-feedback")).using(project(":adaptive-feedback"))
        substitute(module("io.github.nikog4.adaptivekt:adaptive-navigation")).using(project(":adaptive-navigation"))
        substitute(module("io.github.nikog4.adaptivekt:adaptive-forms")).using(project(":adaptive-forms"))
        substitute(module("io.github.nikog4.adaptivekt:adaptive-data")).using(project(":adaptive-data"))
    }
}

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

rootProject.name = "ecommerce-demo"
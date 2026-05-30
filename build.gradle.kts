import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

plugins {
    kotlin("multiplatform") version "2.1.21" apply false
    id("org.jetbrains.compose") version "1.8.2" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21" apply false
    id("com.android.library") version "8.5.2" apply false
    id("com.android.application") version "8.5.2" apply false
}

allprojects {
    group = providers.gradleProperty("GROUP").get()
    version = providers.gradleProperty("VERSION_NAME").get()

    repositories {
        google()
        mavenCentral()
    }
}

val publishableProjects = setOf(
    "adaptive-core",
    "adaptive-components",
    "adaptive-layout",
    "adaptive-feedback",
    "adaptive-navigation",
    "adaptive-forms",
    "adaptive-data",
)

val publicationDescriptions = mapOf(
    "adaptive-core" to "Core adaptive primitives, breakpoints and theme foundation for AdaptiveKt.",
    "adaptive-components" to "Reusable Compose Multiplatform UI components for AdaptiveKt.",
    "adaptive-layout" to "Responsive layout primitives for AdaptiveKt.",
    "adaptive-feedback" to "Empty, loading and error state components for AdaptiveKt.",
    "adaptive-navigation" to "Adaptive navigation scaffolds and navigation primitives for AdaptiveKt.",
    "adaptive-forms" to "Responsive form layout primitives for AdaptiveKt.",
    "adaptive-data" to "Responsive data view primitives for AdaptiveKt.",
)

subprojects {
    if (name in publishableProjects) {
        pluginManager.apply("maven-publish")

        extensions.configure<PublishingExtension>("publishing") {
            repositories {
                maven {
                    name = "LocalTest"
                    url = rootProject.layout.buildDirectory.dir("local-maven").get().asFile.toURI()
                }
            }

            publications.withType<MavenPublication>().configureEach {
                artifactId = project.name
                pom {
                    name.set("AdaptiveKt ${project.name}")
                    description.set(publicationDescriptions.getValue(project.name))
                    url.set("https://github.com/nikoG4/AdaptiveKt")
                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("nikoG4")
                            name.set("Niko Ovelar")
                        }
                    }
                    scm {
                        connection.set("scm:git:https://github.com/nikoG4/AdaptiveKt.git")
                        developerConnection.set("scm:git:https://github.com/nikoG4/AdaptiveKt.git")
                        url.set("https://github.com/nikoG4/AdaptiveKt")
                    }
                }
            }
        }
    }
}

tasks.register("publishAllPublicationsToLocalTestRepository") {
    group = "publishing"
    description = "Publishes all AdaptiveKt library module publications to build/local-maven."
    dependsOn(publishableProjects.map { ":$it:publishAllPublicationsToLocalTestRepository" })
}

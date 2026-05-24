plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":adaptive-core"))
                implementation(project(":adaptive-layout"))
                implementation(project(":adaptive-navigation"))
                implementation(project(":adaptive-forms"))
                implementation(project(":adaptive-data"))
                implementation(project(":adaptive-feedback"))
                implementation(project(":adaptive-components"))
                implementation(compose.foundation)
                implementation(compose.runtime)
                implementation(compose.ui)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.github.adaptivekt.admin.demo.AdminDemoKt"
    }
}

tasks.register("captureVisuals") {
    group = "verification"
    description = "Generate admin-demo visual captures for supported screens."
    dependsOn("jvmJar")

    doLast {
        val runtimeClasspath = configurations.getByName("jvmRuntimeClasspath")
        val jarFile = tasks.named("jvmJar").get().outputs.files.singleFile
        val captureClasspath = files(jarFile, runtimeClasspath)

        val screens = listOf(
            "dashboard",
            "employees",
            "products",
            "invoices",
            "settings",
        )

        val sizes = listOf(
            "compact" to (420 to 900),
            "large" to (1440 to 900),
        )

        screens.forEach { screen ->
            sizes.forEach { (label, dimensions) ->
                val (width, height) = dimensions
                val output = File(layout.buildDirectory.get().asFile, "visual/$screen-$label.png")
                output.parentFile.mkdirs()

                println("Capturing $screen ($label) -> ${output.absolutePath}")
                javaexec {
                    mainClass.set("io.github.adaptivekt.admin.demo.AdminDemoKt")
                    classpath = captureClasspath
                    args = listOf(
                        "--capture",
                        "--screen", screen,
                        "--width", width.toString(),
                        "--height", height.toString(),
                        "--output", output.absolutePath,
                        "--delayMs", "1500",
                    )
                }
            }
        }
    }
}

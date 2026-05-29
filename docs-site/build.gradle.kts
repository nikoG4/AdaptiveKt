@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":adaptive-core"))
                implementation(project(":adaptive-components"))
                implementation(project(":adaptive-layout"))
                implementation(project(":adaptive-feedback"))
                implementation(project(":adaptive-navigation"))
                implementation(project(":adaptive-forms"))
                implementation(project(":adaptive-data"))
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
    }
}

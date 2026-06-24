plugins {
    kotlin("multiplatform") version "2.1.21"
    id("org.jetbrains.compose") version "1.8.2"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21"
    id("com.android.application") version "8.5.2"
}

group = "io.github.adaptivekt.examples"
version = "1.0-SNAPSHOT"

kotlin {
    androidTarget()
    jvm("desktop")
    
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "communication-suite-demo"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "communication-suite-demo.js"
                devServer = (devServer ?: org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val adaptiveKtVersion = "0.1.0-alpha01"
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            
            implementation("io.github.nikog4.adaptivekt:adaptive-core:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-components:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-layout:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-feedback:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-navigation:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-forms:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-data:$adaptiveKtVersion")

            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
        }
        
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.1")
            }
        }
        
        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.9.1")
            }
        }
        
        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.github.adaptivekt.examples.communication.MainKt"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "AdaptiveCommunicationSuite"
            packageVersion = "1.0.0"
        }
    }
}

android {
    namespace = "io.github.adaptivekt.examples.communication"
    compileSdk = 35
    defaultConfig {
        applicationId = "io.github.adaptivekt.examples.communication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
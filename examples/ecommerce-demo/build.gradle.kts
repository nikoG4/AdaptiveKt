plugins {
    kotlin("multiplatform") version "2.1.21"
    id("org.jetbrains.compose") version "1.8.2"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21"
    id("com.android.application") version "8.5.2"
}

group = "io.github.adaptivekt.examples"
version = "1.0-SNAPSHOT"

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    
    jvm("desktop")
    
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "ecommerce-demo"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "ecommerce-demo.js"
                devServer = (devServer ?: org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "EcommerceDemo"
            isStatic = true
        }
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
            
            // AdaptiveKt from Maven Central
            implementation("io.github.nikog4.adaptivekt:adaptive-core:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-components:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-layout:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-feedback:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-navigation:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-forms:$adaptiveKtVersion")
            implementation("io.github.nikog4.adaptivekt:adaptive-data:$adaptiveKtVersion")
        }
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation("androidx.activity:activity-compose:1.9.3")
        }
        
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.1")
            }
        }
    }
}

android {
    namespace = "io.github.adaptivekt.examples.ecommerce"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.github.adaptivekt.examples.ecommerce"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.desktop {
    application {
        mainClass = "io.github.adaptivekt.examples.ecommerce.MainKt"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "AdaptiveStoreDemo"
            packageVersion = "1.0.0"
        }
    }
}

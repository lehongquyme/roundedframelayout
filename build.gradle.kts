plugins {
    id("com.android.library") version "8.12.1"
    id("org.jetbrains.kotlin.android") version "2.1.0"
    id("maven-publish")
}

group = "com.quylh.ui"
version = "1.0.0"

android {
    namespace = "com.quylh.roundedframelayout"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = project.group.toString()
                artifactId = "rounded-frame-layout"
                version = project.version.toString()

                pom {
                    name.set("RoundedFrameLayout")
                    description.set("A lightweight rounded and clipped FrameLayout for Android.")
                }
            }
        }
    }
}

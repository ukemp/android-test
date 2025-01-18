import java.net.URI

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 1

android {
    compileSdk = 35

    namespace = "de.kempmobil.test"

    defaultConfig {
        minSdk = 21
        targetSdk = 35
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    publishing {
        singleVariant("release") {
            withJavadocJar()
            withSourcesJar()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("junit:junit:4.13.2")

    testImplementation("org.robolectric:robolectric:4.8.1")
    testImplementation("org.robolectric:shadows-multidex:4.8.1")
    testImplementation("androidx.test.ext:truth:1.4.0")
}


// Because the components are created only during the afterEvaluate phase, you must
// configure your publications using the afterEvaluate() lifecycle method.
val githubDeployToken: String by project
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "de.kempmobil.android"
                artifactId = "test"
                version = "${versionMajor}.${versionMinor}.${versionPatch}"
                from(components["release"])
            }
        }
        repositories {
            maven {
                url = URI("https://maven.pkg.github.com/ukemp/android-test")
                name = "Android unit test library"
                credentials {
                    username = "ukemp"
                    password = githubDeployToken
                }
            }
        }
    }
}
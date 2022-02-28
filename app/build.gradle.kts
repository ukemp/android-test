
plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 0

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 19
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    testImplementation("org.robolectric:robolectric:4.7.3")
    testImplementation("org.robolectric:shadows-multidex:4.7.3")
    testImplementation("androidx.test.ext:truth:1.4.0")
}


// Also publish the sources:
lateinit var sourcesArtifact: PublishArtifact
tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }

    artifacts {
        sourcesArtifact = archives(sourcesJar)
    }
}


// Because the components are created only during the afterEvaluate phase, you must
// configure your publications using the afterEvaluate() lifecycle method.
val gitLabDeployToken: String by project
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "de.kempmobil.android"
                artifactId = "test"
                version = "${versionMajor}.${versionMinor}.${versionPatch}"
                from(components["release"])
                artifact(sourcesArtifact)
            }
        }
        repositories {
            maven {
                url = uri("https://gitlab.com/api/v4/projects/30012057/packages/maven")
                name = "Android Libraries"
                credentials(HttpHeaderCredentials::class) {
                    name = "Deploy-Token"
                    value = gitLabDeployToken
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
    }
}
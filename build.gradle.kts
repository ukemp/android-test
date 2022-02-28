
buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

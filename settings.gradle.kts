
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

val gitLabDeployToken: String by settings

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = java.net.URI("https://gitlab.com/api/v4/groups/13544180/-/packages/maven")
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

rootProject.name = "Test"
include(":app")

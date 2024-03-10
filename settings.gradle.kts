pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}



dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://www.jitpack.io" )
            url = uri("https://maven.google.com" )
            url = uri("https://maven.pkg.github.com/GenerativeAI/generativeai")
            url = uri("https://jcenter.bintray.com")




        }

        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Nir"
include(":app")
 
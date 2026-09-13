pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Vbbs004 Mobile"

include(":app")

include(":core:domain")
include(":core:common")
include(":core:datastore")
include(":core:network")
include(":core:data")
include(":core:designsystem")
include(":core:navigation")

include(":feature:auth")
include(":feature:weather")
include(":feature:home")
include(":feature:profile")
include(":feature:location")

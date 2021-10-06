pluginManagement {
    repositories {
        mavenLocal()
        maven("https://plugins.gradle.org/m2/")
    }
    // The resolutionStrategy only needs to be configured for
    // local plugin development, specifically when using the
    // mavenLocal repository.
}
buildCache {
    local {
        isEnabled = true
        directory = File("$rootDir/.gradle", "build-cache")
    }
}
rootProject.name = "tsp-kotlin-ms-template"

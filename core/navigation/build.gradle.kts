// core/navigation/build.gradle.kts
plugins {
    id("vbbs.android.library")
}

android {
    namespace = "io.bbs.seva.vbbs004mobile.core.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)   // api: features build EntryProviderScope extensions
    implementation(libs.kotlinx.serialization.core)  // if keys are @Serializable
    implementation(kotlin("reflect"))
}

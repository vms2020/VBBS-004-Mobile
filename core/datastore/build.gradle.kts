plugins {
    id("vbbs.android.library")   // compileSdk 37, minSdk 24, Java 17, serialization plugin
    id("vbbs.android.hilt")      // hilt + ksp plugins
}

android {
    namespace = "io.bbs.seva.vbbs004mobile.core.datastore"
}

dependencies {
    implementation(project(":core:domain"))
    api(libs.androidx.datastore.preferences)   // api: consumers declare DataStore<T> params
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.annotation.experimental)
//    implementation(libs.hilt.android)
//    ksp(libs.hilt.compiler)
}

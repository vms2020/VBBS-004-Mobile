plugins {
    id("vbbs.android.library")
    id("vbbs.android.hilt")
}

android {
    namespace = "io.bbs.seva.vbbs004mobile.core.data"
}

dependencies {
    api(project(":core:domain"))        // api: features see repository interfaces through data
    implementation(project(":core:common"))     // SessionManager
    implementation(project(":core:datastore"))  // DataStores + qualifiers
    implementation(project(":core:network"))    // HttpClient + BASE_URL via network's BuildConfig

    implementation(libs.ktor.client.core)       // AuthRepositoryImpl builds ktor requests
    implementation(libs.osmdroid.android)       // GeoLocationRepositoryImpl
    implementation(libs.coil.compose)           // if picture/avatar code imports it — drop if not
//    implementation(libs.hilt.android)
//    ksp(libs.hilt.compiler)
}

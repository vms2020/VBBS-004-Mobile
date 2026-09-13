plugins {
    id("vbbs.android.library.compose")
    id("vbbs.android.hilt")
}
android { namespace = "io.bbs.seva.vbbs004mobile.feature.location" }
dependencies {
    implementation(project(":core:data"))          // GeoLocationRepository — OsmPickerViewModel injects it
    implementation(project(":core:navigation"))    // Destination, AppNavigator
    implementation(project(":core:designsystem"))
    implementation(libs.osmdroid.android)          // ← the map engine — moves OUT of :app!
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)

    // + whatever the compiler names (location runtime if GeoLocationTracker needs play-services? — check :app's deps)
}

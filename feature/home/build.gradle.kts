plugins {
    id("vbbs.android.library.compose")
    id("vbbs.android.hilt")   // now with folded-in hilt deps, thanks to your plugin work
}

android {
    namespace = "io.bbs.seva.vbbs004mobile.feature.home"
}

dependencies {
    implementation(project(":core:data"))          // WeatherRepository
    implementation(project(":core:navigation"))    // Destination keys
    implementation(project(":core:designsystem"))  // theme

    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.hilt.navigation.compose)
}

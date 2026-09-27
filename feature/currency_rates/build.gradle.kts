plugins {
    id("vbbs.android.library.compose")
    id("vbbs.android.hilt")
}

android {
    namespace = "io.bbs.seva.vbbs004mobile.feature.currency_rates"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))
    implementation(project(":core:designsystem"))
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.hilt.navigation.compose)
}

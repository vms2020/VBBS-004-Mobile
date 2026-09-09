// core/designsystem/build.gradle.kts
plugins {
    id("vbbs.android.library")
    id("vbbs.android.library.compose")   // ← first consumer of the Compose convention plugin
}

android {
    namespace = "io.bbs.seva.vbbs004mobile.core.designsystem"
}

dependencies {
    api(platform(libs.androidx.compose.bom))   // api: features need the same BOM
    api(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material.icons.extended)  // if theme/icons live here
}

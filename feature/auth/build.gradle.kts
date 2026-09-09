plugins {
    id("vbbs.android.library.compose")   // compose + base library config via convention
    id("vbbs.android.hilt")
}

android {
    namespace = "io.bbs.seva.vbbs004mobile.feature.auth"
}

dependencies {
    implementation(project(":core:data"))          // AuthRepository — api-exposed, brings domain types
    implementation(project(":core:navigation"))    // Destination keys
    implementation(project(":core:designsystem"))  // theme

    implementation(libs.androidx.compose.material.icons.extended)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    //ksp(libs.hilt.compiler)
}

import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}
//val localProperties = org.jetbrains.kotlin.konan.properties.Properties().apply {
//    val propertiesFile = rootProject.file("local.properties")
//    if (propertiesFile.exists()) {
//        propertiesFile.inputStream().use { load(it) }
//    }
//}
//val baseUrl = localProperties.getProperty("BASE_URL") ?: "\"\""
//

android {
    namespace = "io.bbs.seva.vbbs004mobile"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "io.bbs.seva.vbbs004mobile"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "alpha-v0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        val localProperties = Properties().apply {
//            val localPropertiesFile = rootProject.file("local.properties")
//            if (localPropertiesFile.exists()) {
//                localPropertiesFile.inputStream().use { load(it) }
//            }
//        }
//        val baseUrl = localProperties.getProperty("BASE_URL")
//        if (baseUrl.isNullOrEmpty()) {
//            error("❌ BUILD FAILED: 'BASE_URL' is missing or empty in local.properties. Please add 'BASE_URL=\"https://your-api.com\"' to your local.properties file.")
//        }
        //buildConfigField("String", "BASE_URL", "\"$baseUrlProperty\"")
//        buildConfigField("String", "BASE_URL", baseUrl)

    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
//        sourceCompatibility = JavaVersion.VERSION_21
//        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:datastore"))
    //implementation(project(":core:domain"))
    implementation(project(":core:common"))
    //implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.datastore.preferences)
    // implementation(libs.tink.android)

    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.osmdroid.android)

    implementation(libs.coil.compose)

    implementation(libs.ktor.client.auth)
    implementation(libs.slf4j.simple)
   // implementation(libs.logback.android)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.converter.serialization)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)


    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    //implementation(kotlin("reflect"))
}

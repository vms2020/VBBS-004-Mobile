import java.util.Properties

plugins {
    id("vbbs.android.library")   // compileSdk/minSdk/Java17/serialization — already inside
    id("vbbs.android.hilt")
}

// BASE_URL moves here from :app — network is its only real consumer
val localProperties = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
val baseUrl = localProperties.getProperty("BASE_URL") ?: "\"\""

android {
    namespace = "io.bbs.seva.vbbs004mobile.core.network"
    defaultConfig {
        buildConfigField("String", "BASE_URL", baseUrl)
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))     // SessionManager
    implementation(project(":core:datastore"))  // tokens DataStore + qualifiers

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.slf4j.simple)
//    implementation(libs.hilt.android)
//    ksp(libs.hilt.compiler)
}

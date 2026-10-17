// A1: app/build.gradle.kts. Only the parts marked "A1" are new; keep the rest of your generated file.
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization) // A1: for @Serializable routes and TMDB DTOs
    alias(libs.plugins.ksp)                  // A1: Room's annotation processor
}

// A1: read the TMDB key from local.properties so it never ends up in Git or the submitted ZIP.
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.example.moviewatchlist"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.moviewatchlist"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // A1: exposed to Kotlin as BuildConfig.TMDB_API_KEY
        buildConfigField(
            "String",
            "TMDB_API_KEY",
            "\"${localProperties.getProperty("TMDB_API_KEY", "")}\""
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true // A1: BuildConfig is off by default in new projects
    }
}

dependencies {
    // ... keep the dependencies Android Studio generated (core-ktx, compose BOM, material3, navigation) ...

    // A1: Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // A1: Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    // A1: used later in A5 (DataStore), A6 (ViewModels) and B3 (posters)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
}

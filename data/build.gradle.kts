import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "1.9.24"
}

val workoutProperties = Properties().apply {
    load(rootProject.file("workout.properties").inputStream())
}

android {
    namespace = "com.aking.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "convex_url", workoutProperties.getProperty("convex.prod_url"))
            resValue("string", "com_auth0_scheme", workoutProperties.getProperty("auth0.scheme"))
            resValue("string", "com_auth0_client_id", workoutProperties.getProperty("auth0.prod_client_id"))
            resValue("string", "com_auth0_domain", workoutProperties.getProperty("auth0.prod_domain"))
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "convex_url", workoutProperties.getProperty("convex.dev_url"))
            resValue("string", "com_auth0_scheme", workoutProperties.getProperty("auth0.scheme"))
            resValue("string", "com_auth0_client_id", workoutProperties.getProperty("auth0.dev_client_id"))
            resValue("string", "com_auth0_domain", workoutProperties.getProperty("auth0.dev_domain"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // convex
    api("dev.convex:android-convexmobile:0.4.1@aar") {
        isTransitive = true
    }
    api(libs.kotlinx.serialization.json)
    api(libs.android.convex.auth0)
    api(libs.auth0)
}
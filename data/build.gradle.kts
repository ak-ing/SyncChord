import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

val workoutProperties = Properties().apply {
    load(rootProject.file("workout.properties").inputStream())
}

android {
    namespace = "com.aking.data"
    compileSdk = 34

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

            manifestPlaceholders["auth0Domain"] = workoutProperties.getProperty("auth0.prod_domain")
            manifestPlaceholders["auth0Scheme"] = workoutProperties.getProperty("auth0.scheme")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("string", "convex_url", workoutProperties.getProperty("convex.dev_url"))
            resValue("string", "com_auth0_scheme", workoutProperties.getProperty("auth0.scheme"))
            resValue("string", "com_auth0_client_id", workoutProperties.getProperty("auth0.dev_client_id"))
            resValue("string", "com_auth0_domain", workoutProperties.getProperty("auth0.dev_domain"))

            manifestPlaceholders["auth0Domain"] = workoutProperties.getProperty("auth0.dev_domain")
            manifestPlaceholders["auth0Scheme"] = workoutProperties.getProperty("auth0.scheme")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.android.convex.auth0)
    implementation(libs.auth0)
}
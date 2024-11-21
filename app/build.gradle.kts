plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.araucaweathers"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.araucaweathers"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        var enabled = true
    }
    dataBinding {
        var enabled = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx) // Core KTX
    implementation(libs.androidx.appcompat) // AppCompat
    implementation(libs.material) // Material Design
    implementation(libs.androidx.activity) // Activity KTX
    implementation(libs.androidx.constraintlayout) // ConstraintLayout
    implementation(libs.okhttp) // OkHttp para solicitudes HTTP

    // Dependencias de prueba
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

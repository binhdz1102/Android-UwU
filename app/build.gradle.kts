plugins {
    alias(libs.plugins.androiduwu.android.application)
    alias(libs.plugins.androiduwu.android.application.compose)
    alias(libs.plugins.androiduwu.android.application.jacoco)
    alias(libs.plugins.androiduwu.android.hilt)
    alias(libs.plugins.androiduwu.android.navigation)
}

android {
    namespace = "com.binhdz.androiduwu"

    defaultConfig {
        applicationId = "com.binhdz.androiduwu"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
}

dependencies {
    // Compose (trong trường hợp lỗi plugin có thể do thiếu 2 lib dưới)
    implementation(libs.androidx.lifecycle.runtimeCompose)
//    implementation(libs.androidx.activity.compose)
//    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)


//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
}
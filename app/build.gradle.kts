plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }

android {
    namespace = "com.khairul.wormzone"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.khairul.wormzone"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            // Use Google's test IDs while developing to avoid invalid ad traffic.
            manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
            resValue("string", "admob_banner_ad_unit_id", "ca-app-pub-3940256099942544/6300978111")
        }
        release {
            isMinifyEnabled = false
            // Your real AdMob IDs are applied to release builds.
            manifestPlaceholders["admobAppId"] = "ca-app-pub-6620088049494024~7659674103"
            resValue("string", "admob_banner_ad_unit_id", "ca-app-pub-6620088049494024/6204701214")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("com.google.android.gms:play-services-ads:24.5.0")
}

plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
 namespace="com.khairul.wormzone"
 compileSdk=35
 defaultConfig { applicationId="com.khairul.wormzone"; minSdk=23; targetSdk=35; versionCode=1; versionName="1.0" }
 buildTypes { debug { isMinifyEnabled=false }; release { isMinifyEnabled=false } }
 compileOptions { sourceCompatibility=JavaVersion.VERSION_17; targetCompatibility=JavaVersion.VERSION_17 }
 kotlinOptions { jvmTarget="17" }
}
dependencies { implementation("androidx.core:core-ktx:1.15.0"); implementation("com.google.android.gms:play-services-ads:24.5.0") }

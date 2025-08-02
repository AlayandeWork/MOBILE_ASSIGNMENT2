plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.kotlin.android)
   id("com.google.gms.google-services") version "4.4.3" apply false
}

android {
   namespace = "ca.georgiancollege.assignment1"
   compileSdk = 35

   defaultConfig {
      applicationId = "ca.georgiancollege.assignment1"
      minSdk = 24
      targetSdk = 35
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
   buildFeatures {
      viewBinding = true
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

   implementation("androidx.core:core-ktx:1.12.0")
   implementation("androidx.appcompat:appcompat:1.6.1")
   implementation("com.google.android.material:material:1.11.0")
   implementation("androidx.activity:activity-ktx:1.8.2")
   implementation("androidx.constraintlayout:constraintlayout:2.1.4")

   // Firebase
   implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
   implementation("com.google.firebase:firebase-auth-ktx")
   implementation("com.google.firebase:firebase-firestore-ktx")

   // Lifecycle
   implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
   implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

   // OkHttp and Glide
   implementation("com.squareup.okhttp3:okhttp:4.12.0")
   implementation("com.github.bumptech.glide:glide:4.15.1")
   implementation(libs.androidx.activity)

   testImplementation("junit:junit:4.13.2")
   androidTestImplementation("androidx.test.ext:junit:1.1.5")
   androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

apply(plugin = "com.google.gms.google-services")

import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.oscar.estatehubcompose"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.oscar.estatehubcompose"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { localProperties.load(it) }
        }

        val baseUrl = localProperties.getProperty("BASE_URL") ?: ""
        val apiKey = localProperties.getProperty("API_KEY") ?: ""
        val geminiKey = localProperties.getProperty("GEMINI_KEY") ?: ""



        manifestPlaceholders["API_KEY"] = apiKey
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        buildConfigField("String","GEMINI_KEY", "\"$geminiKey\"")

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.3.2")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.0")
    implementation("androidx.compose.material:material-icons-extended:1.4.3")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.google.maps.android:maps-compose:4.4.1")
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.google.android.libraries.places:places:3.3.0")
    implementation("com.google.accompanist:accompanist-pager:0.32.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.32.0")
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.se104.passbookapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.se104.passbookapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "BACKEND_URL",
                "\"${project.findProperty("BACKEND_URL") ?: ""}\""

            )

        }
        release {
            buildConfigField(
                "String",
                "BACKEND_URL",
                "\"${project.findProperty("BACKEND_URL") ?: ""}\""
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
    flavorDimensions += "environment"

    productFlavors {
        create("customer") {
            dimension = "environment"

        }
        create("admin") {
            dimension = "environment"
            applicationIdSuffix = ".restaurant"



            resValue(
                type = "string",
                name = "app_name",
                value = "FA Restaurant"
            )
            resValue(
                "string",
                "app_description",
                "Quản lý đơn hàng, theo dõi doanh thu, tối ưu vận hành."
            )
        }
    }
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.properties"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation(libs.androidx.ui.text.google.fonts)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.compose.ui:ui-text-google-fonts:1.8.1")


    // SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")

// Accompanist Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

// Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")

// Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.navigation:navigation-compose:2.9.0")

// Lifecycle Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.0")

// Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

// Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

// DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore:1.1.6")



// Material Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

// Accompanist System UI Controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")

    //YChart
    implementation("co.yml:ycharts:2.1.0")

    // Room components
    implementation("androidx.room:room-runtime:2.7.0")
    ksp("androidx.room:room-compiler:2.7.0")
    implementation("androidx.room:room-ktx:2.7.0")
    implementation("androidx.room:room-paging:2.7.0")

    //Paging
    implementation("androidx.paging:paging-compose:3.3.6")

    //Swipe Action
    implementation("me.saket.swipe:swipe:1.3.0")

    //Bottom Bar Animated
    implementation("com.exyte:animated-navigation-bar:1.0.0")
}
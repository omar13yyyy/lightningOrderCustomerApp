import com.android.build.api.dsl.Packaging
val MAPS_API_KEY: String = if (project.hasProperty("MAPS_API_KEY")) {
    project.property("MAPS_API_KEY") as String
} else {
    ""
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {

    packaging {
        resources {
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/io.netty.versions.properties"

        }
    }
    namespace = "com.lightning.androidfrontend"
    compileSdk = 35

    defaultConfig {

        applicationId = "com.lightning.androidfrontend"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "MAPS_API_KEY", "\"$MAPS_API_KEY\"")
        resValue("string", "google_maps_key", MAPS_API_KEY)

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
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.compose.material:material-icons-extended:...")

    implementation("com.google.maps.android:maps-compose:2.11.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("org.maplibre.gl:android-sdk:11.10.1")
    implementation ("com.android.volley:volley:1.2.1")
   // implementation ("org.maplibre.gl:android-sdk:9.5.0")
    implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.accompanist:accompanist-pager:0.34.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.34.0")
    implementation ("androidx.compose.material:material:1.5.0") // أو آخر نسخة متوفرة
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation ("com.google.accompanist:accompanist-navigation-animation:0.33.2-alpha")
    implementation (libs.kotlinx.coroutines.jdk8)
    implementation(libs.kotlinx.coroutines.core)
   // implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation (libs.converter.gson)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.retrofit)
   // implementation (libs.androidx.core.ktx)
    implementation (libs.firebase.appdistribution.gradle)
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    implementation(libs.maps)
    implementation(libs.androidx.lifecycle.common.jvm)
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit)
    //androidTestImplementation (libs.androidx.espresso.core)

   // implementation("androidx.navigation:navigation-compose:2.8.0")
    //noinspection GradleDependency
    //noinspection GradleDependency
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.tanucode.practica2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tanucode.practica2"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if(localPropertiesFile.exists()){
            localProperties.load(localPropertiesFile.inputStream())
        }

        val mapsApiKey = localProperties.getProperty("MAPS_API_KEY")

        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
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
        viewBinding = true
    }

}


dependencies {
//Para retrofit y Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

//Adicional para el interceptor
    implementation(libs.logging.interceptor)

//Glide y Picasso
    implementation(libs.glide)
    implementation(libs.picasso)

//Para las corrutinas con alcance lifecycle (opcional)
    implementation(libs.androidx.lifecycle.runtime.ktx)

//Imágenes con bordes redondeados
    implementation(libs.roundedimageview)

    //Api de youtube
    implementation(libs.core)

    //Exoplayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    //Auth con Firebase
    implementation(libs.firebase.auth)

    //Para imagenes circulares
    implementation(libs.circleimageview)


    //Google Maps (Play services de Google Maps, tanto para vistas XML como para Compose)
    implementation(libs.play.services.maps)

//API'S opcionales para la ubicación (XML y Compose). Ej. Clase FusedLocationProviderClient
    implementation(libs.play.services.location)

//Para corrutinas con alcance viewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
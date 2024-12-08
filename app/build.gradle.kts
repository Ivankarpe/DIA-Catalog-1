plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.googleGmsGoogleServices)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.Practice.DIACataloge"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.Practice.DIACataloge"
        minSdk = 27
        targetSdk = 34
        versionCode = 8
        versionName = "2.8"

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
    viewBinding {
        enable = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.tbuonomo:dotsindicator:5.0")
    implementation ("com.google.firebase:firebase-firestore:24.0.2")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)


    implementation(libs.core.ktx)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)

    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
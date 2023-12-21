plugins {
    id("com.android.application")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.hashimte.hashbus1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hashimte.hashbus1"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

//repositories {
//    mavenCentral()
//}


dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation("androidx.navigation:navigation-ui:2.7.6")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
//    implementation("com.google.android.gms:play-services:12.0.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-analytics:18.0.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.mlkit:vision-common:17.3.0")
    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("com.google.android.material:material:1.12.0-alpha02")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.github.jd-alexander:library:1.1.0")
    implementation("com.google.maps.android:android-maps-utils:3.8.0")
    compileOnly("com.google.maps:google-maps-services:0.1.20")
    implementation("com.google.maps:google-maps-services:0.1.20")
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.journeyapps:zxing-android-embedded:4.1.0")
    implementation("com.google.zxing:core:3.3.3")

    //CameraX
    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-video:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    implementation("androidx.camera:camera-extensions:1.3.1")
}
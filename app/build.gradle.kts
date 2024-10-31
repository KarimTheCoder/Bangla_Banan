plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.google.mlkit.samples.vision.digitalink"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = "com.google.mlkit.samples.vision.digitalink"
        minSdk = 26
        targetSdk = 34
        multiDexEnabled = true
        versionCode = 1
        versionName = "1.0"
        setProperty("archivesBaseName", "vision-digital-ink")
        testApplicationId = "com.google.mlkit.vision.digitalink"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro", "proguard.cfg")
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf("-Xuse-experimental=androidx.compose.runtime.ExperimentalComposeApi")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    packaging {
        resources {
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}



//tasks.register<Delete>("clean") {
//    delete(rootProject.buildDir)
//}



dependencies {

    implementation ("com.airbnb.android:lottie-compose:6.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.0.21")
    implementation("androidx.paging:paging-common-android:3.3.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("androidx.navigation:navigation-compose:2.8.3")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.3")
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    implementation("com.google.auto.value:auto-value-annotations:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    annotationProcessor("com.google.auto.value:auto-value:1.11.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.google.guava:guava:33.3.1-android")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.mlkit:digital-ink-recognition:18.1.0")

}

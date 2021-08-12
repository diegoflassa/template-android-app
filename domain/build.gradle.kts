import io.github.diegoflassa.template_android_app.buildsrc.Config
import io.github.diegoflassa.template_android_app.buildsrc.Versions

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = Config.compileSdkVersion
    //compileSdkPreview = Config.compileSdkPreviewVersion
    buildToolsVersion = Config.buildToolsVersion

    defaultConfig {
        minSdk = Config.minimumSdkVersion
        targetSdk = Config.targetSdkVersion
        //targetSdkPreview = Config.targetSdkPreviewVersion
        //versionCode = Config.versionCode
        //versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(project(mapOf("path" to ":data")))

    implementation("androidx.core:core-ktx:${Versions.core_ktx}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("com.google.android.material:material:${Versions.material}")
    androidTestImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("androidx.test.ext:junit-ktx:${Versions.junit_ktx}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espresso}")
	
	// WorkManager
    // (Java only)
    implementation("androidx.work:work-runtime:${Versions.work_manager}")
    // Kotlin + coroutines
    implementation("androidx.work:work-runtime-ktx:${Versions.work_manager}")
    // optional - RxJava2 support
    implementation("androidx.work:work-rxjava2:${Versions.work_manager}")
    // optional - GCMNetworkManager support
    implementation("androidx.work:work-gcm:${Versions.work_manager}")
    // optional - Multiprocess support
    implementation("androidx.work:work-multiprocess:${Versions.work_manager}")
	// optional - Test helpers
    androidTestImplementation("androidx.work:work-testing:${Versions.work_manager}")
}
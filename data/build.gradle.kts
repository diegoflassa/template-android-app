import io.github.diegoflassa.template_android_app.buildsrc.Config
import io.github.diegoflassa.template_android_app.buildsrc.Versions

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
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
		
        javaCompileOptions {
            annotationProcessorOptions {
                arguments +=  mapOf(
					"room.schemaLocation" to "$projectDir/schemas".toString(),
                    "room.incremental" to "true",
					"room.expandProjection" to "true")
            }
        }
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

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation("androidx.core:core-ktx:${Versions.core_ktx}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("com.google.android.material:material:${Versions.material}")
    androidTestImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("androidx.test.ext:junit-ktx:${Versions.junit_ktx}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espresso}")
	
    //Retrofix 2
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit_adapter}")
    implementation("com.squareup.retrofit2:converter-moshi:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofit}")

    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:${Versions.moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}")

    //GSON
    implementation("com.google.code.gson:gson:${Versions.gson}")

    // RX Java 3
    implementation("io.reactivex.rxjava3:rxjava:${Versions.rxjava}")
    implementation("io.reactivex.rxjava3:rxandroid:${Versions.rxandroid}")
	
	// Room
    implementation("androidx.room:room-runtime:${Versions.room}")
    annotationProcessor("androidx.room:room-compiler:${Versions.room}")
    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:${Versions.room}")
    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:${Versions.room}")
    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:${Versions.room}")
    // optional - Test helpers
    testImplementation("androidx.room:room-testing:${Versions.room}")
}
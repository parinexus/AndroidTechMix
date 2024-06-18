import android.tech.mix.buildsrc.Libs
import android.tech.mix.buildsrc.Versions
import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

val apikeyProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "keys.properties")))
}

android {

    compileSdk = Versions.compileSdk

    defaultConfig {

        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        buildConfigField(
            "String",
            "ACC_WEATHER_API_KEY",
            ((apikeyProperties["ACC_WEATHER_API_KEY"] ?: "") as String)
        )
        buildConfigField(
            "String",
            "OPEN_WEATHER_API_KEY",
            ((apikeyProperties["OPEN_WEATHER_API_KEY"] ?: "") as String)
        )

        testInstrumentationRunner = Libs.AndroidX.Test.instrumentationRunner
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    packagingOptions {
        resources {
            excludes.add("**/attach_hotspot_windows.dll")
            excludes.add("META-INF/licenses/**")
            excludes.add("META-INF/AL2.0")
            excludes.add("META-INF/LGPL2.1")
        }
    }
}
dependencies {
    implementation(project(":domain"))

    implementation(Libs.AndroidX.appCompat)

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Kotlin.Coroutines.core)
    implementation(Libs.Kotlin.Coroutines.android)

    implementation(Libs.Square.okHttp)
    implementation(Libs.Square.Retrofit.retrofit)
    implementation(Libs.Square.Retrofit.gsonConverter)
    implementation(Libs.Square.Retrofit.logging)

    implementation(Libs.AndroidX.Room.runtime)
    kapt(Libs.AndroidX.Room.compiler)
    implementation(Libs.AndroidX.Room.ktx)

    implementation(Libs.Glide.core)
    kapt(Libs.Glide.compiler)

    implementation(Libs.Google.Hilt.android)
    kapt(Libs.Google.Hilt.hiltCompiler)

    implementation(Libs.AndroidX.DataStore.preferences)

    //Core testing source module it will hold the
    androidTestImplementation(project(":core-testing"))
    testImplementation(project(":core-testing"))
}
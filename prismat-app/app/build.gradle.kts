plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

private fun execCommand(command: String): String? {
    val cmd = command.split(" ").toTypedArray()
    val process = ProcessBuilder(*cmd)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .start()
    return process.inputStream.bufferedReader().readLine()?.trim()
}

val majorVersion = 1
val numberOfCommits = execCommand("git rev-list --count HEAD")?.toInt()
val minorVersion = numberOfCommits?.div(10)
val patchVersion = (minorVersion?.times(10)?.let { numberOfCommits?.minus(it) })

android {
    namespace = "com.ecsolution.prismat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ecsolution.prismat"
        minSdk = 33
        targetSdk = 34
        versionCode = majorVersion
        versionName = "$majorVersion.$minorVersion.$patchVersion"

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

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Compose
    implementation(libs.androidx.ui)            // UI elements
    implementation(libs.androidx.material)  // Material Design
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.activity.compose) // Integration
    implementation(libs.androidx.lifecycle.runtime.compose) // Viewmodel live update
    implementation(libs.androidx.runtime.livedata)


    // Retrofit, Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.material3.android)
}
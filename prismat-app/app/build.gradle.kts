plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
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
    namespace = "se.apps.arctic.prismat"
    compileSdk = 35

    defaultConfig {
        applicationId = "se.apps.arctic.prismat"
        minSdk = 33
        targetSdk = 35
        versionCode = numberOfCommits
        versionName = "$majorVersion.$minorVersion.$patchVersion"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    implementation(libs.androidx.ui)                    // UI elements
    implementation(libs.androidx.material)              // Material Design
    implementation(libs.androidx.activity.compose)      // Integration
    implementation(libs.androidx.ui.tooling)            // Preview - excluded when minimized
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Retrofit, Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.material3.android)
}
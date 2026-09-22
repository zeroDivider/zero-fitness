import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}

val localProperties =
    Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) {
            file.inputStream().use { load(it) }
        }
    }

fun requireSupabaseProperty(name: String): String {
    val fromLocal = localProperties.getProperty(name)?.trim()?.takeIf { it.isNotEmpty() }
    val fromProject = project.findProperty(name)?.toString()?.trim()?.takeIf { it.isNotEmpty() }
    return fromLocal
        ?: fromProject
        ?: error(
            "Missing Supabase config '$name'. Add it to local.properties or pass -P$name=...",
        )
}

fun String.escapeForBuildConfig(): String = replace("\\", "\\\\").replace("\"", "\\\"")

val supabaseUrl = requireSupabaseProperty("supabase.url").escapeForBuildConfig()
val supabaseKey = requireSupabaseProperty("supabase.key").escapeForBuildConfig()

android {
    namespace = "com.zero.fitness"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.zero.fitness"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SUPABASE_URL", "\"$supabaseUrl\"")
        buildConfigField("String", "SUPABASE_KEY", "\"$supabaseKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.javaTarget.get().toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(libs.bundles.core)
    implementation(libs.bundles.ui)
    implementation(platform(libs.androidx.compose.bom))
    ksp(libs.ksp)

    // hilt
    implementation(libs.androidx.hilt.viewmodel)
    kapt(libs.androidx.hilt.compiler)

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)
    implementation(libs.ktor.client.android)

    // room
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    debugImplementation(libs.bundles.debug)
}

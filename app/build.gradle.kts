plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.protobuf)
//    alias(libs.plugins.kapt)
    kotlin("kapt")
    // refered to libs.versions.toml
    // id("org.jetbrains.kotlin.android")
    // alias(libs.plugins.ksp)
    // alias(libs.plugins.compose.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

protobuf {
    protoc {
        val VERSION = libs.versions.protobuf.kotlin.get()
        artifact = "com.google.protobuf:protoc:$VERSION"
    }
    // 添加生成 Kotlin 和 Java 代码的任务
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

android {
    namespace = "top.cha01.geofence"
    compileSdk = 35

    defaultConfig {
        applicationId = "top.cha01.geofence"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    sourceSets {
        getByName("main") {
            // 添加 debug 目录到源码路径
            java.srcDir(
                "build/generated/source/proto/main/java" // 保留原有路径
            )
            kotlin.srcDir(
                "build/generated/source/proto/main/kotlin" // 保留原有路径
            )
        }

        getByName("debug") {
            java.srcDir("build/generated/source/proto/debug/java")
            kotlin.srcDir("build/generated/source/proto/debug/kotlin")
        }

        getByName("release") {
            java.srcDir("build/generated/source/proto/release/java")
            kotlin.srcDir("build/generated/source/proto/release/kotlin")
        }
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
    compileOptions {
        val VERSION = JavaVersion.VERSION_1_8
        sourceCompatibility = VERSION
        targetCompatibility = VERSION
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

//    ksp("androidx.room:room-compiler:2.5.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.adaptive.android)
    implementation(libs.androidx.adaptive.navigation.suite)
    implementation(libs.androidx.adaptive.navigation.android)
    implementation(libs.androidx.adaptive.layout.android)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.usb.serial)
    implementation(libs.protobuf.java)
    implementation(libs.protobuf.kotlin)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
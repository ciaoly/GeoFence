// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.google.protobuf) apply false
    kotlin("kapt") version "1.9.25"
//    alias(libs.plugins.ksp) apply false
//    alias(libs.plugins.kapt) apply false
//    alias(libs.plugins.compose.compiler) apply false
    // id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    // id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}
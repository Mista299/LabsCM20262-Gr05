plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "co.edu.udea.compumovil.gr05_20262.lab2"
    compileSdk = 35

    defaultConfig {
        applicationId = "co.edu.udea.compumovil.gr05_20262.lab2"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        // Configuracion de firma para el APK de entrega (release).
        // Las credenciales se leen de gradle.properties (local, no versionado)
        // o de variables de entorno, para no subir el keystore al repositorio.
        create("release") {
            val keystorePath = (project.findProperty("LAB2_KEYSTORE") as String?)
                ?: System.getenv("LAB2_KEYSTORE")
            if (keystorePath != null && file(keystorePath).exists()) {
                storeFile = file(keystorePath)
                storePassword = (project.findProperty("LAB2_STORE_PASSWORD") as String?)
                    ?: System.getenv("LAB2_STORE_PASSWORD")
                keyAlias = (project.findProperty("LAB2_KEY_ALIAS") as String?)
                    ?: System.getenv("LAB2_KEY_ALIAS")
                keyPassword = (project.findProperty("LAB2_KEY_PASSWORD") as String?)
                    ?: System.getenv("LAB2_KEY_PASSWORD")
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
            // Si no hay keystore configurado, se firma con la clave de debug
            // para que siempre se pueda generar un APK instalable.
            signingConfig = if (signingConfigs.getByName("release").storeFile != null) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.09.03")
    implementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    // AppCompat aporta la aplicacion de idioma por aplicacion en versiones
    // anteriores a Android 13, mediante AppCompatDelegate.
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")

    // Navigation (objetivo especifico: modulo de navigation de Compose)
    implementation("androidx.navigation:navigation-compose:2.8.2")

    // Material Design
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // WorkManager (objetivo especifico: uso de workers)
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    // Consumo de servicios REST (objetivo especifico: retrofit o volley)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Persistencia local de lo que descarga el worker
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    testImplementation("junit:junit:4.13.2")
}

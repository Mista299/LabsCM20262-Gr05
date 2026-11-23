# Laboratorio 2 — Workers y consumo de servicios REST

**Computación Móvil — Universidad de Antioquia — 2026-II — Grupo 05**

## Integrantes

| # | Nombre | Cuenta de GitHub | Aporte principal |
|---|--------|------------------|------------------|
| 1 | Michael Stiven Tabares Tobón | [`Mista299`](https://github.com/Mista299) | Configuración del proyecto, capa de datos (Room), Worker + WorkManager, servicio REST con Retrofit, ViewModel y lógica de negocio. |
| 2 | Maria Fernanda Atencia Oliva | `TU-USUARIO-AQUI` ⚠️ | Interfaz de usuario en Jetpack Compose: tema Material 3, pantalla de lista y pantalla de detalle. |
| 3 | Maria Camila Berrio Pavas | `TU-USUARIO-AQUI` ⚠️ | Navegación de Compose, recursos e internacionalización (español/inglés), íconos, pruebas y documentación. |

> ⚠️ Reemplazar los campos `TU-USUARIO-AQUI` por las cuentas reales de GitHub de las dos compañeras
> (ver la sección "Integrantes y cuentas de GitHub" de `ENTREGA.md`).


## Enunciado vs. implementación

| Objetivo del enunciado | Dónde se cumple |
|------------------------|-----------------|
| Usar el módulo de Navigation de Compose | `ui/AppNavHost.kt`, `ui/Routes.kt` (`NavHost`, `composable`, navegación con argumentos) |
| Usar librerías para consumo de servicios web (Retrofit o Volley) | `network/PostApiService.kt`, `network/RetrofitProvider.kt` (Retrofit + OkHttp + Gson) |
| Uso de workers | `worker/SyncPostsWorker.kt` (CoroutineWorker), `worker/SyncScheduler.kt` (tarea periódica cada 15 min) |
| Material Design + Jetpack Compose | `ui/theme/Theme.kt` (Material 3, claro y oscuro), todas las pantallas son Compose |
| 2 idiomas | `res/values/strings.xml` (es) y `res/values-en/strings.xml` (en) |
| Nomenclatura del laboratorio | Proyecto `LabsCM20262-Gr05`, módulo `Lab2-UI`, paquete `co.edu.udea.compumovil.gr05_20262.lab2` |

## Funcionalidades implementadas

1. **Tarea recurrente en segundo plano (Worker).** `SyncPostsWorker` es un `CoroutineWorker` que se programa cada 15 minutos con WorkManager, bajo la restricción de red conectada, con reintentos automáticos ante fallos de red transitorios.
2. **Consumo de un servicio REST.** El worker consulta `GET https://jsonplaceholder.typicode.com/posts` mediante Retrofit y persiste el resultado.
3. **Persistencia local (Room).** Los datos descargados por el worker se guardan en una base de datos Room y la UI los observa con `Flow` (reactiva), por lo que la lista se actualiza sola cuando el worker termina.
4. **Navegación Compose con dos pantallas.** Lista de publicaciones → detalle de la publicación, pasando el `id` como argumento de ruta.
5. **Sincronización manual.** Botón de actualización que encola una ejecución inmediata del worker (`OneTimeWorkRequest`).

## Estructura del proyecto

```
LabsCM20262-Gr05/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat
├── gradle/wrapper/
└── Lab2-UI/                          <- módulo de la aplicación (renombrado desde "app")
    ├── build.gradle.kts
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/co/edu/udea/compumovil/gr05_20262/lab2/
        │   ├── data/                  <- Room (entidad, DAO, base de datos)
        │   │   ├── PostEntity.kt
        │   │   ├── PostDao.kt
        │   │   └── DatabaseProvider.kt
        │   ├── network/               <- Retrofit (servicio REST)
        │   │   ├── PostApiService.kt
        │   │   └── RetrofitProvider.kt
        │   ├── worker/                <- WorkManager (tarea recurrente)
        │   │   ├── SyncPostsWorker.kt
        │   │   └── SyncScheduler.kt
        │   ├── viewmodel/
        │   │   └── PostsViewModel.kt
        │   └── ui/                    <- Compose + Navigation + Material 3
        │       ├── MainActivity.kt
        │       ├── AppNavHost.kt
        │       ├── Routes.kt
        │       ├── PostListScreen.kt
        │       ├── PostDetailScreen.kt
        │       └── theme/Theme.kt
        └── res/
            ├── values/strings.xml     <- español
            ├── values-en/strings.xml  <- inglés
            └── ...
```

## Cómo abrir y compilar

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Mista299/LabsCM20262-Gr05.git
   ```
2. Abrir la carpeta `LabsCM20262-Gr05` en Android Studio (Ladybug o superior).
3. Android Studio descargará las dependencias automáticamente. Si se requiere, crear `local.properties` con:
   ```properties
   sdk.dir=/ruta/a/tu/Android/Sdk
   ```
4. Compilar:
   - **Debug:** `./gradlew :Lab2-UI:assembleDebug`
   - **Release firmado (APK de entrega):** `./gradlew :Lab2-UI:assembleRelease`

### Generar el APK firmado

Crear un keystore (una sola vez):
```bash
keytool -genkeypair -v -keystore lab2-keystore.jks -keyalg RSA \
  -keysize 2048 -validity 10000 -alias lab2
```

Configurar `Lab2-UI/build.gradle.kts` (o `gradle.properties`) con las credenciales y firmar:
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("../lab2-keystore.jks")
            storePassword = System.getenv("STORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

```bash
export STORE_PASSWORD=... KEY_ALIAS=lab2 KEY_PASSWORD=...
./gradlew :Lab2-UI:assembleRelease
# APK: Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk
```

> **Nota de seguridad:** el keystore y las contraseñas **no** se suben al repositorio. Las credenciales reales se configuran localmente en `gradle.properties` (ignorado por git) o mediante variables de entorno.

## Pruebas

```bash
./gradlew :Lab2-UI:test
```

## Cómo se verifica el funcionamiento del worker

1. Abrir la app: la lista aparece vacía con un indicador de carga (el worker está descargando).
2. En unos segundos la lista se puebla sola, sin interacción del usuario.
3. Con la app abierta, ejecutar en la terminal:
   ```bash
   adb shell dumpsys jobscheduler | grep -i sync_posts
   ```
   o revisar el estado del trabajo en Android Studio → **View → Tool Windows → Background Task Inspector**.
4. En Android Studio, **View → Tool Windows → WorkManager** permite ver el job programado y ejecutarlo manualmente.

## Códigos de ejemplo (derechos de autor)

Las pantallas y la estructura siguen los patrones de los codelabs oficiales de Android:
- *Android Basics: Compose* (Unidad 5)
- *Kotlin Coroutines in Compose*
- *WorkManager with Compose*

El servicio REST utilizado es público y de prueba: [JSONPlaceholder](https://jsonplaceholder.typicode.com/).

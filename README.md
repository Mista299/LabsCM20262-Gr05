# Laboratorio 2 - Workers y consumo de servicios REST

Computacion Movil - Universidad de Antioquia - 2026-II - Grupo 05

## Integrantes

| Nombre | Cuenta de GitHub |
|--------|------------------|
| Michael Stiven Tabares Tobon | [Mista299](https://github.com/Mista299) |
| Maria Fernanda Atencia Oliva | [AtenciaMaria](https://github.com/AtenciaMaria) |
| Maria Camila Berrio Pavas | [mariacamilaberriopavas](https://github.com/mariacamilaberriopavas) |

## Que hace la aplicacion

La aplicacion descarga publicaciones de un servicio web REST y las muestra en dos
pantallas conectadas con Navigation de Compose. La descarga se realiza en segundo
plano mediante un worker periodico de WorkManager, no desde la interfaz.

1. Al abrir la app se programa un worker periodico cada 15 minutos.
2. El worker consulta `GET https://jsonplaceholder.typicode.com/posts` mediante Retrofit.
3. La respuesta se guarda en una base de datos local Room.
4. La interfaz observa la base de datos con un Flow, por lo que la lista se actualiza
   sola cuando el worker termina, sin intervencion del usuario.
5. Al tocar una publicacion se navega a la pantalla de detalle, pasando el
   identificador como argumento de ruta.
6. El boton de actualizar en la barra superior encola una ejecucion inmediata del
   worker.

## Requisitos del enunciado y donde se cumplen

| Requisito | Ubicacion |
|-----------|-----------|
| Modulo de Navigation de Compose | `Lab2-UI/src/main/java/co/edu/udea/compumovil/gr05_20262/lab2/ui/AppNavHost.kt` y `ui/Routes.kt` |
| Consumo de servicio web REST con Retrofit | `Lab2-UI/src/main/java/co/edu/udea/compumovil/gr05_20262/lab2/network/PostApiService.kt` y `network/RetrofitProvider.kt` |
| Uso de workers | `Lab2-UI/src/main/java/co/edu/udea/compumovil/gr05_20262/lab2/worker/SyncPostsWorker.kt` y `worker/SyncScheduler.kt` |
| Material Design con Jetpack Compose | `Lab2-UI/src/main/java/co/edu/udea/compumovil/gr05_20262/lab2/ui/theme/Theme.kt` y todas las pantallas en `ui/` |
| Dos idiomas | `Lab2-UI/src/main/res/values/strings.xml` (espanol) y `Lab2-UI/src/main/res/values-en/strings.xml` (ingles) |
| Nomenclatura del laboratorio | Proyecto `LabsCM20262-Gr05`, modulo `Lab2-UI`, paquete `co.edu.udea.compumovil.gr05_20262.lab2` |

## Estructura del proyecto

```
LabsCM20262-Gr05/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── gradle/wrapper/
└── Lab2-UI/
    ├── build.gradle.kts
    ├── proguard-rules.pro
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/co/edu/udea/compumovil/gr05_20262/lab2/
        │   │   ├── data/
        │   │   │   ├── PostEntity.kt
        │   │   │   ├── PostDao.kt
        │   │   │   └── DatabaseProvider.kt
        │   │   ├── network/
        │   │   │   ├── PostApiService.kt
        │   │   │   └── RetrofitProvider.kt
        │   │   ├── worker/
        │   │   │   ├── SyncPostsWorker.kt
        │   │   │   └── SyncScheduler.kt
        │   │   ├── viewmodel/
        │   │   │   └── PostsViewModel.kt
        │   │   └── ui/
        │   │       ├── MainActivity.kt
        │   │       ├── AppNavHost.kt
        │   │       ├── Routes.kt
        │   │       ├── PostListScreen.kt
        │   │       ├── PostDetailScreen.kt
        │   │       └── theme/Theme.kt
        │   └── res/
        │       ├── values/strings.xml
        │       ├── values-en/strings.xml
        │       ├── values/colors.xml
        │       ├── values/themes.xml
        │       ├── drawable/
        │       └── mipmap-anydpi-v26/
        └── test/java/co/edu/udea/compumovil/gr05_20262/lab2/
            └── Lab2UnitTest.kt
```

## Comandos de ejecucion

### Requisitos previos

- JDK 17 o superior
- Android Studio con Android SDK 35 instalado
- Dispositivo o emulador con Android 8.0 (API 26) o superior

### Clonar el repositorio

```bash
git clone https://github.com/Mista299/LabsCM20262-Gr05.git
cd LabsCM20262-Gr05
```

### Configurar la ruta del SDK

Crear el archivo `local.properties` en la raiz del proyecto:

```properties
sdk.dir=/ruta/a/tu/Android/Sdk
```

Este archivo no se versiona en el repositorio.

### Compilar la version de desarrollo

```bash
./gradlew :Lab2-UI:assembleDebug
```

El APK resultante queda en:

```
Lab2-UI/build/outputs/apk/debug/Lab2-UI-debug.apk
```

### Ejecutar las pruebas unitarias

```bash
./gradlew :Lab2-UI:test
```

El reporte queda en:

```
Lab2-UI/build/reports/tests/testDebugUnitTest/index.html
```

### Instalar en un dispositivo

```bash
./gradlew :Lab2-UI:installDebug
```

O manualmente:

```bash
adb install -r Lab2-UI/build/outputs/apk/debug/Lab2-UI-debug.apk
```

## Generacion del APK firmado de entrega

### Crear el almacen de claves

Ejecutar una sola vez:

```bash
keytool -genkeypair -v -keystore lab2-keystore.jks -alias lab2 \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -dname "CN=LabsCM20262-Gr05, OU=Universidad de Antioquia, O=Computacion Movil, C=CO"
```

### Configurar las credenciales

La configuracion de firma ya esta preparada en `Lab2-UI/build.gradle.kts`. Se leen
las variables de entorno `LAB2_KEYSTORE`, `LAB2_STORE_PASSWORD`, `LAB2_KEY_ALIAS` y
`LAB2_KEY_PASSWORD`.

```bash
export LAB2_KEYSTORE="$PWD/lab2-keystore.jks"
export LAB2_STORE_PASSWORD=tu_contrasena
export LAB2_KEY_ALIAS=lab2
export LAB2_KEY_PASSWORD=tu_contrasena
```

Si no se definen estas variables, la variante release se firma automaticamente con la
clave de depuracion, de modo que siempre se puede generar un APK instalable.

### Compilar la version firmada

```bash
./gradlew :Lab2-UI:assembleRelease
```

El APK resultante queda en:

```
Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk
```

### Publicar el release en GitHub

```bash
gh release create v1.0 Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk \
  --title "Lab2 v1.0 - Entrega (APK firmado)" \
  --notes "Laboratorio 2 - Workers y consumo de servicios REST. Grupo 05."
```

### Verificar la firma del APK

```bash
$ANDROID_HOME/build-tools/35.0.0/apksigner verify --print-certs \
  Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk
```

El almacen de claves y sus contrasenas no se versionan en el repositorio. estan
excluidos en `.gitignore`. El archivo `keystore.properties.example` documenta las
variables de entorno esperadas.

## Verificacion del funcionamiento del worker

La aplicacion se puede comprobar de tres formas.

### 1. Desde la interfaz

Al abrir la aplicacion, la lista aparece con un indicador de carga. En pocos
segundos se llena sola, porque los datos los descarga el worker y no la pantalla.
El boton de actualizar en la barra superior encola una ejecucion inmediata.

### 2. Desde la terminal

Con el dispositivo conectado:

```bash
adb shell dumpsys jobscheduler | grep -i sync_posts
```

### 3. Desde Android Studio

Abrir la ventana **View > Tool Windows > Background Task Inspector** para ver el
trabajo programado, su estado y ejecutarlo manualmente.

## Permisos de la aplicacion

| Permiso | Uso |
|---------|-----|
| `android.permission.INTERNET` | Permite las peticiones HTTP al servicio REST |
| `android.permission.ACCESS_NETWORK_STATE` | Permite a WorkManager verificar la conexion antes de ejecutar el worker |

## Servicio REST utilizado

[JSONPlaceholder](https://jsonplaceholder.typicode.com/), un servicio publico de
pruebas que no requiere registro ni clave de API. El endpoint consumido es
`GET /posts`, que devuelve 100 publicaciones de ejemplo.

## Referencias de implementacion

El proyecto sigue los patrones de los codelabs oficiales de Android:

- Android Basics: Compose, unidad 5
- Kotlin Coroutines in Compose
- WorkManager with Compose

# Resumen del Laboratorio 2

Workers y consumo de servicios REST. Computacion Movil, Universidad de Antioquia,
2026-II, Grupo 05.

Direccion del repositorio: https://github.com/Mista299/LabsCM20262-Gr05

---

## 1. Requisitos del enunciado y donde se cumplen

| Requisito | Ubicacion | Estado |
|-----------|-----------|--------|
| Crear el repositorio `LabsCM20262-Gr05` | https://github.com/Mista299/LabsCM20262-Gr05 | Listo |
| Proyecto `Labs20262-Gr05` con modulo `Lab2-UI` | Carpeta `Lab2-UI/` | Listo |
| Paquete `co.edu.udea.compumovil.gr05_20262.lab2` | `Lab2-UI/build.gradle.kts`, `namespace` y `applicationId` | Listo |
| Usar el modulo de Navigation de Compose | `ui/AppNavHost.kt`, `ui/Routes.kt` | Listo |
| Usar libreria para consumo de servicios web REST | `network/PostApiService.kt`, `network/RetrofitProvider.kt` | Listo |
| Uso de workers | `worker/SyncPostsWorker.kt`, `worker/SyncScheduler.kt` | Listo |
| Realizar el codelab de Compose, corrutinas y WorkManager | Estructura y patrones del proyecto | Listo |
| Aplicacion con Material Design y Jetpack Compose | `ui/theme/Theme.kt` y todas las pantallas | Listo |
| Dos idiomas | `res/values/strings.xml` (espanol), `res/values-en/strings.xml` (ingles) | Listo |
| Commits periodicos de cada integrante | Historial del repositorio | En curso |
| APK firmado | `Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk` | Ver seccion 6 |
| Archivo con la URL del repositorio y los integrantes con su cuenta | `ENTREGA.md` | Listo |

---

## 2. Como funciona la aplicacion

La aplicacion descarga publicaciones de un servicio web REST y las muestra en dos
pantallas conectadas con Navigation de Compose. La descarga ocurre en segundo plano
mediante un worker periodico de WorkManager, no desde la interfaz.

### Flujo completo

```
El usuario abre la app
        |
        v
PostsViewModel se inicializa
        |
        v
SyncScheduler.schedule()  ----->  WorkManager programa SyncPostsWorker
        |                                  (cada 15 minutos, con red conectada)
        |                                          |
        |                                          v
        |                          Retrofit: GET https://dummyjson.com/posts
        |                                          |
        |                                          v
        |                          Room: se guardan las 100 publicaciones
        |                                          |
        v                                          |
PostDao.observeAll()  <---- Flow reactivo --------+
        |
        v
PostListScreen muestra la lista
        |
        |  el usuario toca una publicacion
        v
navController.navigate("post_detail/{postId}")
        |
        v
PostDetailScreen lee la publicacion de Room y la muestra
```

### Worker en segundo plano

`SyncPostsWorker` es un `CoroutineWorker`. Cada quince minutos WorkManager lo
ejecuta, y lo hace unicamente cuando hay red disponible, segun la restriccion
`NetworkType.CONNECTED`. El worker consulta el servicio REST con Retrofit, guarda
el resultado en la base de datos Room y devuelve `Result.success()`. Si ocurre un
fallo de red transitorio devuelve `Result.retry()` hasta tres veces, y despues
`Result.failure()`.

La descarga no bloquea la interfaz. La pantalla observa la base de datos con un
`Flow`, de modo que la lista se actualiza sola cuando el worker termina.

El boton de actualizar de la barra superior encola una ejecucion inmediata del
worker mediante `OneTimeWorkRequest`.

### Consumo del servicio REST

`PostApiService` declara el endpoint `GET /posts` como una funcion suspendida, con
un parametro `limit`. `RetrofitProvider` construye el cliente con un `OkHttpClient`
de veinte segundos de timeout de conexion y de lectura, y un interceptor de log de
nivel `BASIC`.

El servicio utilizado es DummyJSON, un servicio publico de pruebas que no requiere
registro ni clave de API. Devuelve publicaciones con contenido real en ingles, y
cada una incluye etiquetas, reacciones y numero de vistas. La respuesta se
deserializa en `PostsResponse` y se convierte a entidad con la funcion de
extension `toEntity`.

### Persistencia local

`PostEntity` modela la respuesta del servicio. `PostDao` expone `observeAll`, que
devuelve un `Flow` para la observacion reactiva, `getById`, que recupera una
publicacion concreta, e `insertAll`, que guarda el resultado reemplazando los
registros que ya existen. `DatabaseProvider` mantiene la instancia unica de la
base de datos.

### Interfaz y navegacion

`AppNavHost` declara el `NavHost` con las rutas de `Routes`. La ruta de detalle
declara el argumento `postId` con tipo `NavType.IntType`, y el identificador se
pasa como argumento de navegacion.

`PostListScreen` muestra las publicaciones en una `LazyColumn` con tarjetas de
Material 3. Mientras la lista esta vacia presenta un indicador de carga, porque los
datos todavia no han llegado.

`PostDetailScreen` lee la publicacion por identificador desde la base de datos local
y muestra el titulo, el autor y el cuerpo completo.

`theme/Theme.kt` define la paleta clara y la paleta oscura. La aplicacion sigue el
modo claro o oscuro del sistema de forma automatica.

### Internacionalizacion

Todos los textos visibles se obtienen mediante `stringResource`, nunca escritos en
el codigo Kotlin. Por eso la aplicacion se adapta al idioma del dispositivo con
los archivos `values/strings.xml` y `values-en/strings.xml`.

La aplicacion declara sus idiomas soportados en `res/xml/locales_config.xml`, lo
que hace que Android muestre la entrada "Idioma" en los ajustes del sistema para
poder forzar el idioma sin cambiar el del telephone completo.

---

## 3. Verificacion realizada en un dispositivo real

La aplicacion se instalo y se ejecuto en un telefono real, un realme C67 con
Android 14 (API 34). Se comprobo lo siguiente.

| Comprobacion | Resultado |
|--------------|-----------|
| Instalacion del APK | Correcta, paquete `co.edu.udea.compumovil.gr05_20262.lab2` |
| Inicio de la aplicacion | Correcto, la lista aparece con indicador de carga |
| Worker programado en el sistema | Correcto, el trabajo figura como `RUNNABLE` en `dumpsys jobscheduler` |
| Restricciones del trabajo | Correcto, `TIMING_DELAY` y `CONNECTIVITY` |
| Descarga del servicio REST | Correcta, 100 publicaciones descargadas |
| Persistencia en Room | Correcta, tabla `posts` con 100 registros |
| Marca de tiempo de sincronizacion | Correcta, se registra la hora real de cada sincronizacion |
| Navegacion hacia el detalle | Correcta, muestra el post con su autor y su cuerpo |
| Retorno a la lista | Correcto |
| Modo oscuro | Correcto, la paleta oscura se aplica de forma automatica |
| Idioma ingles | Correcto, la interfaz cambia a ingles |
| Pruebas unitarias | Todas pasan |

Comandos usados para verificar el worker desde la terminal:

```bash
adb shell dumpsys jobscheduler | grep -i co.edu.udea.compumovil
```

---

## 4. Dos correcciones aplicadas durante la verificacion

La ejecucion en el dispositivo real permitio detectar dos errores que las pruebas
unitarias no cubrian. Ambos quedaron corregidos y verificados.

**Argumento de navegacion sin tipo declarado.** Al abrir la pantalla de detalle,
el identificador de la publicacion llegaba como `String` en lugar de `Integer`, por
lo que la busqueda devolvia cero y la pantalla no encontraba la publicacion. La
correccion fue declarar el tipo del argumento en el `NavHost`:

```kotlin
composable(
    route = Routes.DETAIL,
    arguments = listOf(
        navArgument(Routes.ARG_POST_ID) { type = NavType.IntType }
    )
) { entry -> ... }
```

**Marca de tiempo no registrada.** El campo `syncedAt` de la entidad tiene un valor
por defecto, pero Gson no aplica los valores por defecto de Kotlin al deserializar,
por lo que el campo quedaba en cero. La correccion fue asignar la marca de tiempo en
el worker, que es donde corresponde:

```kotlin
val syncedAt = System.currentTimeMillis()
insertAll(posts.map { it.copy(syncedAt = syncedAt) })
```

---

## 5. Estructura del proyecto

```
LabsCM20262-Gr05/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── gradle/wrapper/
├── ENTREGA.md
├── README.md
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

---

## 6. Comandos de ejecucion

### Compilar la version de desarrollo

```bash
./gradlew :Lab2-UI:assembleDebug
```

El APK queda en `Lab2-UI/build/outputs/apk/debug/Lab2-UI-debug.apk`.

### Ejecutar las pruebas unitarias

```bash
./gradlew :Lab2-UI:test
```

### Instalar en un dispositivo

```bash
./gradlew :Lab2-UI:installDebug
```

### Generar el APK firmado de entrega

```bash
keytool -genkeypair -v -keystore lab2-keystore.jks -alias lab2 \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -dname "CN=LabsCM20262-Gr05, OU=Universidad de Antioquia, O=Computacion Movil, C=CO"

export LAB2_KEYSTORE="$PWD/lab2-keystore.jks"
export LAB2_STORE_PASSWORD=...
export LAB2_KEY_ALIAS=lab2
export LAB2_KEY_PASSWORD=...

./gradlew :Lab2-UI:assembleRelease
```

El APK firmado queda en `Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk`.

### Publicar el release en GitHub

```bash
gh release create v1.0 Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk \
  --title "Lab2 v1.0 - Entrega (APK firmado)" \
  --notes "Laboratorio 2 - Workers y consumo de servicios REST. Grupo 05."
```

El almacen de claves y las contrasenas no se versionan en el repositorio. estan
excluidos en `.gitignore`. El archivo `keystore.properties.example` documenta las
variables de entorno esperadas.

---

## 7. Permisos y configuracion

| Permiso | Uso |
|---------|-----|
| `android.permission.INTERNET` | Permite las peticiones HTTP al servicio REST |
| `android.permission.ACCESS_NETWORK_STATE` | Permite a WorkManager verificar la conexion antes de ejecutar el worker |

| Parametro | Valor |
|-----------|-------|
| Paquete | `co.edu.udea.compumovil.gr05_20262.lab2` |
| compileSdk | 35 |
| targetSdk | 35 |
| minSdk | 26 |
| Version | 1.0 (versionCode 1) |
| Lenguajes | espanol e ingles |

---

## 8. Integrantes

| Nombre | Cuenta de GitHub |
|--------|------------------|
| Michael Stiven Tabares Tobon | [Mista299](https://github.com/Mista299) |
| Maria Fernanda Atencia Oliva | [AtenciaMaria](https://github.com/AtenciaMaria) |
| Maria Camila Berrio Pavas | [mariacamilaberriopavas](https://github.com/mariacamilaberriopavas) |

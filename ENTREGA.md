# Entrega - Laboratorio 2

Workers y consumo de servicios REST. Computacion Movil, Universidad de Antioquia,
2026-II, Grupo 05.

## Direccion del repositorio

https://github.com/Mista299/LabsCM20262-Gr05

## Integrantes

| Nombre | Cuenta de GitHub | Aporte |
|--------|------------------|--------|
| Michael Stiven Tabares Tobon | [Mista299](https://github.com/Mista299) | Configuracion del proyecto y del modulo `Lab2-UI`, capa de datos con Room, worker con WorkManager, consumo del servicio REST con Retrofit, ViewModel y generacion del APK firmado. |
| Maria Fernanda Atencia Oliva | [AtenciaMaria](https://github.com/AtenciaMaria) | Interfaz de usuario en Jetpack Compose, tema Material 3 con modo claro y oscuro, pantalla de lista y pantalla de detalle. |
| Maria Camila Berrio Pavas | [mariacamilaberriopavas](https://github.com/mariacamilaberriopavas) | Navegacion de Compose, recursos e internacionalizacion espanol e ingles, icono adaptativo, pruebas unitarias y documentacion. |

## Estructura del proyecto

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
        │   │   ├── network/
        │   │   ├── worker/
        │   │   ├── viewmodel/
        │   │   └── ui/
        │   └── res/
        └── test/java/co/edu/udea/compumovil/gr05_20262/lab2/
```

## Paquete de la aplicacion

| Paquete | Valor |
|---------|-------|
| Nombre del proyecto | LabsCM20262-Gr05 |
| Modulo | Lab2-UI |
| Namespace y applicationId | co.edu.udea.compumovil.gr05_20262.lab2 |
| compileSdk | 35 |
| targetSdk | 35 |
| minSdk | 26 |
| Version | 1.0 (versionCode 1) |

## Comandos de ejecucion

```bash
# Compilar la version de desarrollo
./gradlew :Lab2-UI:assembleDebug

# Ejecutar las pruebas unitarias
./gradlew :Lab2-UI:test

# Instalar en un dispositivo conectado
./gradlew :Lab2-UI:installDebug

# Generar el APK firmado de entrega
export LAB2_KEYSTORE="$PWD/lab2-keystore.jks"
export LAB2_STORE_PASSWORD=...
export LAB2_KEY_ALIAS=lab2
export LAB2_KEY_PASSWORD=...
./gradlew :Lab2-UI:assembleRelease
```

El APK firmado queda en `Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk`.

La documentacion completa de compilacion, firma y publicacion del release esta en el
archivo `README.md` de la raiz del repositorio.

## Componentes implementados

### Worker con WorkManager

`worker/SyncPostsWorker.kt` es un `CoroutineWorker` que consulta el servicio REST y
persiste el resultado en Room. Ejecuta las operaciones de red sobre el dispatcher
`Dispatchers.IO`. Ante un fallo de red transitorio devuelve `Result.retry()`, con un
limite de tres intentos, y `Result.failure()` si se agotan.

`worker/SyncScheduler.kt` programa el trabajo periodico con
`PeriodicWorkRequestBuilder` en un intervalo de 15 minutos, que es el minimo
aceptado por WorkManager para tareas periodicas. Define la restriccion
`NetworkType.CONNECTED` para que el worker solo se ejecute con red disponible, y
usa `ExistingPeriodicWorkPolicy.KEEP` para no duplicar el trabajo programado. La
funcion `syncNow` encola una ejecucion inmediata mediante `OneTimeWorkRequest`.

### Consumo del servicio REST con Retrofit

`network/PostApiService.kt` define el endpoint `GET /posts` como una funcion
suspendida. `network/RetrofitProvider.kt` construye el cliente Retrofit con un
`OkHttpClient` de 20 segundos de timeout de conexion y de lectura, y un
`HttpLoggingInterceptor` de nivel `BASIC`.

### Persistencia local con Room

`data/PostEntity.kt` modela la respuesta del servicio. `data/PostDao.kt` expone las
consultas: `observeAll` devuelve un `Flow` para observacion reactiva, `getById`
recupera una publicacion concreta y `insertAll` guarda el resultado con
`OnConflictStrategy.REPLACE`. `data/DatabaseProvider.kt` mantiene la instancia
unica de la base de datos.

### Interfaz y navegacion

`ui/AppNavHost.kt` declara el `NavHost` con las rutas definidas en `ui/Routes.kt`.
La ruta de detalle declara el argumento `postId` y el identificador se pasa como
argumento de navegacion. `ui/PostListScreen.kt` muestra las publicaciones en una
`LazyColumn` con tarjetas de Material 3 y un estado de carga mientras la lista esta
vacia. `ui/PostDetailScreen.kt` lee la publicacion por identificador desde la base
de datos local. `ui/theme/Theme.kt` define la paleta clara y la oscura.

### Observabilidad reactiva

`viewmodel/PostsViewModel.kt` expone las publicaciones como un `StateFlow` creado a
partir del `Flow` del DAO, con `SharingStarted.WhileSubscribed` y un valor inicial
vacio. Al inicializarse, programa la tarea recurrente.

## Permisos declarados

| Permiso | Uso |
|---------|-----|
| `android.permission.INTERNET` | Peticiones HTTP al servicio REST |
| `android.permission.ACCESS_NETWORK_STATE` | Verificacion de la conexion de red por parte de WorkManager |

## Idiomas

La aplicacion esta disponible en espanol e ingles.

| Archivo | Idioma |
|---------|--------|
| `Lab2-UI/src/main/res/values/strings.xml` | Espanol |
| `Lab2-UI/src/main/res/values-en/strings.xml` | Ingles |

Todos los textos visibles de la interfaz se obtienen mediante `stringResource`, por
lo que la aplicacion se adapta automaticamente al idioma del dispositivo.

## Pruebas

`Lab2-UI/src/test/java/co/edu/udea/compumovil/gr05_20262/lab2/Lab2UnitTest.kt`
contiene dos pruebas unitarias:

- Verifica que la entidad `PostEntity` conserve los campos devueltos por el
  servicio REST.
- Verifica que la ruta de detalle incluya el identificador de la publicacion.

Ejecucion:

```bash
./gradlew :Lab2-UI:test
```

## Publicacion del APK

```bash
gh release create v1.0 Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk \
  --title "Lab2 v1.0 - Entrega (APK firmado)" \
  --notes "Laboratorio 2 - Workers y consumo de servicios REST. Grupo 05."
```

El almacen de claves y las contrasenas de firma no se versionan en el repositorio.
Estan excluidos en `.gitignore`.

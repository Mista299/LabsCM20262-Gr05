# Laboratorio 2 - Workers y consumo de servicios REST

Computacion Movil - Universidad de Antioquia - 2026-II - Grupo 05

Direccion del repositorio: https://github.com/Mista299/LabsCM20262-Gr05

APK firmado de entrega: https://github.com/Mista299/LabsCM20262-Gr05/releases/tag/v1.0

---

## Integrantes

| Nombre | Cuenta de GitHub |
|--------|------------------|
| Michael Stiven Tabares Tobon | [Mista299](https://github.com/Mista299) |
| Maria Fernanda Atencia Oliva | [AtenciaMaria](https://github.com/AtenciaMaria) |
| Maria Camila Berrio Pavas | [mariacamilaberriopavas](https://github.com/mariacamilaberriopavas) |

---

# Parte 1. Cada punto de la guia y como se resolvio

La guia del laboratorio esta organizada en una seccion de actividad con nueve
apartados y una seccion de notas importantes con cuatro condiciones. A continuacion
se indica, punto por punto, que se hizo y donde esta.

## Punto 1. Crear un repositorio llamado LabsCMAAAAS-GrXX

**Requerido.** El repositorio existe con el nombre exacto que pide la guia, donde
`AAAA` es el ano 2026, `S` es el semestre 2 y `XX` es el grupo 05.

- Nombre real: `LabsCM20262-Gr05`
- Ubicacion: https://github.com/Mista299/LabsCM20262-Gr05
- Visibilidad: publico, para que el profesor pueda revisarlo sin pedir permisos

El repositorio se creo primero con la parte de Michael, y las otras dos
integrantes se agregaron despues como colaboradoras con permiso de escritura
para que cada una suba sus propios commits.

## Punto 2. Crear el proyecto con el nombre y el paquete correctos

**Requerido.** Se cumplen las tres condiciones:

| Condicion de la guia | Como se resolvio |
|----------------------|------------------|
| Nombre del proyecto `LabsAAAAS-GrXX` | `rootProject.name = "LabsCM20262-Gr05"` en `settings.gradle.kts` |
| Paquete `co.edu.udea.compumovil.grXX_AAAAS.lab2` | `namespace` y `applicationId` en `Lab2-UI/build.gradle.kts` |
| Modulo renombrado de `app` a `Lab1-UI` | El modulo se llamo `Lab2-UI`, que es la variante correspondiente a este laboratorio, y se declaro con `include(":Lab2-UI")` |

Sobre el paquete: la guia advierte que su nombre es importante porque el sistema
operativo lo usa para identificar la aplicacion. Por eso se uso exactamente
`co.edu.udea.compumovil.gr05_20262.lab2`, con el grupo y el semestre en el lugar
que indica la guia. La aplicacion queda registrada en el telefono con ese
identificador, como se verifico al instalarla.

## Punto 3. Agregar el proyecto al repositorio

**Requerido.** Hecho con la linea de comandos, que es la primera de las dos
opciones que ofrece la guia.

```bash
cd <ruta>/LabsCM20262-Gr05/
git add .
git commit -m "..."
git remote add origin <url del repositorio>
git push -u origin master
```

El historial tiene 18 commits. Los diez primeros son la base completa
(configuracion, capa de datos, capa de red, worker, ViewModel, recursos y
documentacion), y los siguientes corresponden a las correcciones detectadas al
ejecutar la aplicacion y a los archivos de las otras dos integrantes.

## Punto 4. Realizar los codelabs

**Requerido.** Los tres codelabs se aplicaron como base de la implementacion.

| Codelab | Como se aplica en el proyecto |
|---------|-------------------------------|
| Android Basics: Compose, unidad 5 | Toda la interfaz esta escrita en Jetpack Compose, con Material 3 y un solo `Activity` |
| Kotlin Coroutines in Compose | La funcion `getPosts()` es `suspend`, el DAO expone `Flow`, y el ViewModel usa `StateFlow` con `stateIn` |
| WorkManager with Compose | `SyncPostsWorker` es un `CoroutineWorker`, programado desde el ViewModel, y el resultado se observa en la interfaz mediante `Flow` |

## Punto 5. Correr los ejemplos del repositorio compose-samples

**Requerido.** Se tomo como referencia el patron de la muestra oficial de
Compose para una aplicacion de lista con detalle. La estructura adoptada es la
siguiente:

- Un solo `Activity` (`MainActivity`) que monta el arbol de Compose.
- La navegacion declarada en un `NavHost` con rutas nombradas.
- La lista en una `LazyColumn` con elementos componibles reutilizables.
- El detalle en una pantalla aparte, alimentado por un identificador que viaja
  como argumento de navegacion.
- La capa de datos fuera de la interfaz, con un repositorio local que actua como
  fuente unica para la pantalla.

## Punto 6. Implementar alguna funcionalidad del ejemplo usando un worker

**Requerido.** Es el nucleo del laboratorio.

La funcionalidad implementada es la **sincronizacion periodica de los datos en
segundo plano**. La aplicacion no pide los datos a la red desde la pantalla: es el
worker quien lo hace, por su cuenta, cada quince minutos.

Detalles de la implementacion:

- `SyncPostsWorker` extiende `CoroutineWorker` y ejecuta su trabajo sobre el
  dispatcher `Dispatchers.IO`.
- `SyncScheduler` programa el trabajo con `PeriodicWorkRequestBuilder` en un
  intervalo de 15 minutos, que es el minimo que acepta WorkManager para tareas
  periodicas.
- Se define la restriccion `NetworkType.CONNECTED`, de modo que el worker solo se
  ejecuta cuando hay red disponible.
- Se usa `ExistingPeriodicWorkPolicy.KEEP` para que abrir la app varias veces no
  duplique el trabajo programado.
- Ante un fallo de red transitorio el worker devuelve `Result.retry()`, con un
  limite de tres intentos, y `Result.failure()` si se agotan.
- El metodo `syncNow` encola una ejecucion inmediata con `OneTimeWorkRequest`. Es lo
  que hace el boton de actualizar de la barra superior.

## Punto 7. Consumir un servicio web REST para complementar los datos

**Requerido.** Consume el servicio publico JSONPlaceholder.

- `PostApiService` declara el endpoint `GET /posts` como una funcion `suspend`.
- `RetrofitProvider` construye el cliente con un `OkHttpClient` de veinte segundos
  de timeout de conexion y de lectura, y un interceptor de log de nivel `BASIC`.
- La conversion se hace con Gson mediante `GsonConverterFactory`.
- El servicio no requiere registro ni clave de API, lo que evita dependencias
  externas en la entrega.

Los datos descargados por el worker se guardan en una base de datos Room. La
interfaz observa esa base de datos con un `Flow`, por lo que la lista se actualiza
sola en cuanto el worker termina, sin intervencion del usuario y sin recargar la
pantalla.

## Punto 8. Porcentajes de evaluacion

| Criterio | Peso | Como se cubre |
|----------|------|---------------|
| Uso de workers y/o coroutine-workers | 30 % | `SyncPostsWorker` como `CoroutineWorker`, programado periodicamente con restricciones de red, con reintentos y con una ejecucion inmediata bajo demanda |
| Al menos dos funcionalidades del ejemplo seleccionado | 40 % | Listado de publicaciones con navegacion al detalle, sincronizacion periodica en segundo plano, actualizacion manual, modo claro y oscuro, e internacionalizacion |
| Realizar el punto 7 | 20 % | Consumo de JSONPlaceholder con Retrofit y persistencia en Room |
| Aspecto del diseno y el funcionamiento | 10 % | Material 3 con paleta propia, tarjetas, tipografia jerarquizada, estados de carga, y verificado en un dispositivo real |

## Punto 9. Notas importantes

### La aplicacion debe usar Material Design y Jetpack Compose

Cumplido. Toda la interfaz esta escrita en Compose y usa Material 3. El tema
define una paleta propia para modo claro y para modo oscuro, con una barra
superior `CenterAlignedTopAppBar`, tarjetas `Card` con elevacion, tipografia
jerarquizada de Material 3 y los iconos del paquete `material-icons-extended`.

### Debe estar disponible para dos idiomas

Cumplido. La aplicacion esta en espanol e ingles.

| Archivo | Idioma |
|---------|--------|
| `Lab2-UI/src/main/res/values/strings.xml` | Espanol |
| `Lab2-UI/src/main/res/values-en/strings.xml` | Ingles |

Todos los textos visibles de la interfaz se obtienen con `stringResource`, nunca
escritos dentro del codigo Kotlin. Se verifico en el dispositivo que al cambiar
el idioma de la aplicacion toda la interfaz cambia, incluidos el titulo, el
encabezado de la lista y las etiquetas de las pantallas.

### Deben realizarse commits periodicos acordes con los avances

Cumplido. El historial tiene 18 commits organizados por etapas: configuracion,
capa de datos, capa de red, worker, ViewModel, recursos, interfaz, pruebas,
documentacion y las correcciones finales. Cada commit corresponde a una unidad
funcional.

### Cada integrante debe enviar sus commits de acuerdo con su aporte

En curso. El repositorio contiene los commits de Michael Stiven Tabares Tobon con
su nombre y su correo. Los commits de Maria Fernanda Atencia Oliva y de Maria
Camila Berrio Pavas se incorporan cuando cada una ejecute el envio desde su
propia cuenta de GitHub, con el material que se les entrego.

### Debe agregarse un archivo con la URL del repositorio y los integrantes

Cumplido. El archivo `ENTREGA.md` contiene la direccion del repositorio, el
nombre completo de cada integrante y el enlace a su cuenta de GitHub. Ese archivo
es el que debe copiarse en la carpeta de laboratorios de la materia.

### Condiciones de entrega

| Condicion | Estado |
|-----------|--------|
| Repositorio con el nombre `LabsCMAAAAS-GrXX` | https://github.com/Mista299/LabsCM20262-Gr05 |
| Codigo en el repositorio asignado | Los 18 commits estan subidos a `master` |
| APK firmado | Generado, verificado y publicado como release `v1.0` |

---

# Parte 2. Como funciona la aplicacion

## Descripcion general

La aplicacion descarga publicaciones de un servicio web REST y las presenta en dos
pantallas conectadas con Navigation de Compose. La descarga se realiza en segundo
plano mediante un worker periodico de WorkManager, no desde la interfaz. Los datos
quedan almacenados en una base de datos local Room, y la pantalla los observa de
forma reactiva.

El resultado practical es que la aplicacion muestra contenido util de inmediato
tras la primera sincronizacion, y se mantiene actualizada sin que el usuario
tenga que hacer nada.

## Diagrama del flujo

```
   El usuario abre la aplicacion
              |
              v
   MainActivity monta la interfaz
              |
              v
   PostsViewModel se inicializa
              |
              +-----------------------------+
              |                             |
              v                             v
   SyncScheduler.schedule()        PostDao.observeAll()
              |                             |
              v                             |
   WorkManager programa                Flow reactivo
   SyncPostsWorker                      |
   (cada 15 minutos,                     |
    con red conectada)                   |
              |                           |
              v                           |
   Retrofit: GET jsonplaceholder...       |
              |                           |
              v                           |
   Room: se guardan las publicaciones     |
              |                           |
              +-------------+-------------+
                            |
                            v
              PostListScreen muestra la lista
                            |
                            |  el usuario toca una publicacion
                            v
              navController.navigate("post_detail/{postId}")
                            |
                            v
              PostDetailScreen lee de Room y muestra el detalle
```

## El worker en segundo plano

`SyncPostsWorker` es un `CoroutineWorker`. WorkManager lo ejecuta cada quince
minutos y lo hace unicamente cuando hay red disponible. El worker consulta el
servicio REST con Retrofit, guarda el resultado en Room y devuelve
`Result.success()`. Si ocurre un fallo de red transitorio devuelve `Result.retry()`
hasta tres veces, y `Result.failure()` si se agotan los intentos.

La descarga nunca bloquea la interfaz. La pantalla observa la base de datos con un
`Flow`, de modo que la lista se actualiza sola cuando el worker termina.

El boton de actualizar de la barra superior encola una ejecucion inmediata del
worker mediante `OneTimeWorkRequest`.

## Consumo del servicio REST

`PostApiService` declara el endpoint `GET /posts` como una funcion suspendida.
`RetrofitProvider` construye el cliente con un `OkHttpClient` de veinte segundos de
timeout de conexion y de lectura, y un interceptor de log de nivel `BASIC`.

El servicio utilizado es JSONPlaceholder, un servicio publico de pruebas que no
requiere registro ni clave de API. Devuelve cien publicaciones de ejemplo.

## Persistencia local

`PostEntity` modela la respuesta del servicio y guarda la marca de tiempo de la
sincronizacion. `PostDao` expone `observeAll`, que devuelve un `Flow` para la
observacion reactiva, `getById`, que recupera una publicacion concreta, e
`insertAll`, que guarda el resultado reemplazando los registros que ya existen.
`DatabaseProvider` mantiene la instancia unica de la base de datos.

## Interfaz y navegacion

`AppNavHost` declara el `NavHost` con las rutas definidas en `Routes`. La ruta de
detalle declara el argumento `postId` con tipo `NavType.IntType`, y el
identificador viaja como argumento de navegacion.

`PostListScreen` muestra las publicaciones en una `LazyColumn` con tarjetas de
Material 3. Mientras la lista esta vacia presenta un indicador de carga, porque los
datos todavia no han llegado. El boton de actualizar de la barra superior encola
una sincronizacion inmediata.

`PostDetailScreen` lee la publicacion por identificador desde la base de datos
local y muestra el titulo, el autor y el cuerpo completo, con un boton de volver
en la barra superior.

`theme/Theme.kt` define la paleta clara y la paleta oscura. La aplicacion sigue
el modo claro o oscuro del sistema de forma automatica.

## Internacionalizacion

Todos los textos visibles se obtienen mediante `stringResource`, nunca escritos en
el codigo Kotlin. Por eso la aplicacion se adapta al idioma del dispositivo con
los archivos `values/strings.xml` y `values-en/strings.xml`.

---

# Parte 3. Verificacion realizada en un dispositivo real

La aplicacion se instalo y se ejecuto en un telefono real, un realme C67 con
Android 14 (API 34). Se comprobo lo siguiente.

| Comprobacion | Resultado |
|--------------|-----------|
| Instalacion del APK con el paquete de la guia | Correcto, `co.edu.udea.compumovil.gr05_20262.lab2` |
| Inicio de la aplicacion | Correcto, la lista aparece con indicador de carga |
| Worker programado en el sistema | Correcto, el trabajo figura como `RUNNABLE` en `dumpsys jobscheduler` |
| Restricciones del trabajo | Correcto, `TIMING_DELAY` y `CONNECTIVITY` |
| Peticion al servicio REST | Correcto, `GET https://jsonplaceholder.typicode.com/posts` responde `200` |
| Resultado del worker | Correcto, `Worker result SUCCESS` |
| Descarga de las publicaciones | Correcto, 100 publicaciones |
| Persistencia en Room | Correcto, tabla `posts` con 100 registros |
| Marca de tiempo de sincronizacion | Correcto, se registra la hora real de cada sincronizacion |
| Navegacion hacia el detalle | Correcto, muestra el post con su autor y su cuerpo |
| Retorno a la lista | Correcto |
| Modo oscuro | Correcto, la paleta oscura se aplica de forma automatica |
| Idioma ingles | Correcto, la interfaz cambia a ingles |
| Version firmada de entrega | Correcto, se instalo y ejecuto con la firma de entrega |
| Pruebas unitarias | Todas pasan |

Comandos usados para verificar el worker desde la terminal:

```bash
adb shell dumpsys jobscheduler | grep -i co.edu.udea.compumovil
```

## Dos correcciones aplicadas durante la verificacion

La ejecucion en el dispositivo real permitio detectar dos errores que las pruebas
unitarias no cubrian. Ambos quedaron corregidos, verificados y subidos al
repositorio.

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
por lo que el campo quedaba en cero. La correccion fue asignar la marca de tiempo
en el worker, que es donde corresponde:

```kotlin
val syncedAt = System.currentTimeMillis()
insertAll(posts.map { it.copy(syncedAt = syncedAt) })
```

---

# Parte 4. Estructura del proyecto

```
LabsCM20262-Gr05/
├── build.gradle.kts              Configuracion de plugins de la raiz
├── settings.gradle.kts           Nombre del proyecto, repositorios y modulos
├── gradle.properties             Propiedades de Gradle y de Android
├── gradlew / gradlew.bat         Wrapper de Gradle 8.14.3
├── gradle/wrapper/
├── ENTREGA.md                    Datos de la entrega e integrantes
├── RESUMEN.md                    Resumen de requisitos y funcionamiento
├── README.md                     Este documento
└── Lab2-UI/                      Modulo de la aplicacion
    ├── build.gradle.kts          Dependencias, SDK y configuracion de firma
    ├── proguard-rules.pro
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/co/edu/udea/compumovil/gr05_20262/lab2/
        │   │   ├── data/
        │   │   │   ├── PostEntity.kt         Entidad Room
        │   │   │   ├── PostDao.kt            Consultas y Flow reactivo
        │   │   │   └── DatabaseProvider.kt   Instancia unica de la base de datos
        │   │   ├── network/
        │   │   │   ├── PostApiService.kt     Endpoint GET /posts
        │   │   │   └── RetrofitProvider.kt   Cliente Retrofit y OkHttp
        │   │   ├── worker/
        │   │   │   ├── SyncPostsWorker.kt    CoroutineWorker que consume el REST
        │   │   │   └── SyncScheduler.kt      Programacion del trabajo periodico
        │   │   ├── viewmodel/
        │   │   │   └── PostsViewModel.kt     StateFlow y programacion del worker
        │   │   └── ui/
        │   │       ├── MainActivity.kt       Activity unica con Compose
        │   │       ├── AppNavHost.kt         NavHost y rutas
        │   │       ├── Routes.kt             Definicion de rutas
        │   │       ├── PostListScreen.kt     Pantalla de lista
        │   │       ├── PostDetailScreen.kt   Pantalla de detalle
        │   │       └── theme/Theme.kt        Tema Material 3, claro y oscuro
        │   └── res/
        │       ├── values/strings.xml        Textos en espanol
        │       ├── values-en/strings.xml     Textos en ingles
        │       ├── values/colors.xml
        │       ├── values/themes.xml
        │       ├── drawable/                 Vector del icono
        │       └── mipmap-anydpi-v26/        Icono adaptativo
        └── test/java/co/edu/udea/compumovil/gr05_20262/lab2/
            └── Lab2UnitTest.kt               Pruebas unitarias
```

---

# Parte 5. Comandos de ejecucion

## Requisitos previos

- JDK 17 o superior
- Android Studio con Android SDK 35 instalado
- Dispositivo o emulador con Android 8.0 (API 26) o superior

## Clonar el repositorio

```bash
git clone https://github.com/Mista299/LabsCM20262-Gr05.git
cd LabsCM20262-Gr05
```

## Configurar la ruta del SDK

Crear el archivo `local.properties` en la raiz del proyecto:

```properties
sdk.dir=/ruta/a/tu/Android/Sdk
```

Este archivo no se versiona en el repositorio.

## Compilar la version de desarrollo

```bash
./gradlew :Lab2-UI:assembleDebug
```

El APK resultante queda en:

```
Lab2-UI/build/outputs/apk/debug/Lab2-UI-debug.apk
```

## Ejecutar las pruebas unitarias

```bash
./gradlew :Lab2-UI:test
```

El reporte queda en:

```
Lab2-UI/build/reports/tests/testDebugUnitTest/index.html
```

## Instalar en un dispositivo

```bash
./gradlew :Lab2-UI:installDebug
```

O manualmente:

```bash
adb install -r Lab2-UI/build/outputs/apk/debug/Lab2-UI-debug.apk
```

## Generar el APK firmado de entrega

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
export LAB2_STORE_PASSWORD=...
export LAB2_KEY_ALIAS=lab2
export LAB2_KEY_PASSWORD=...
```

Si no se definen estas variables, la variante release se firma automaticamente con
la clave de depuracion, de modo que siempre se puede generar un APK instalable.

### Compilar la version firmada

```bash
./gradlew :Lab2-UI:assembleRelease
```

El APK resultante queda en:

```
Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk
```

### Verificar la firma del APK

```bash
$ANDROID_HOME/build-tools/35.0.0/apksigner verify --print-certs \
  Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk
```

### Publicar el release en GitHub

```bash
gh release create v1.0 Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk \
  --title "Lab2 v1.0 - Entrega (APK firmado)" \
  --notes "Laboratorio 2 - Workers y consumo de servicios REST. Grupo 05."
```

> **Nota de seguridad:** el almacen de claves y las contrasenas **no** se suben al
> repositorio. estan excluidos en `.gitignore`. El archivo `keystore.properties.example`
> documenta las variables de entorno esperadas.

---

# Parte 6. Verificacion del funcionamiento del worker

La aplicacion se puede comprobar de tres formas.

## Desde la interfaz

Al abrir la aplicacion, la lista aparece con un indicador de carga. En pocos
segundos se llena sola, porque los datos los descarga el worker y no la pantalla.
El boton de actualizar en la barra superior encola una ejecucion inmediata.

## Desde la terminal

Con el dispositivo conectado:

```bash
adb shell dumpsys jobscheduler | grep -i co.edu.udea.compumovil
```

## Desde Android Studio

Abrir la ventana **View > Tool Windows > Background Task Inspector** para ver el
trabajo programado, su estado y ejecutarlo manualmente.

---

# Parte 7. Permisos y parametros

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

# Parte 8. Referencias de implementacion

El proyecto sigue los patrones de los codelabs oficiales de Android:

- Android Basics: Compose, unidad 5
- Kotlin Coroutines in Compose
- WorkManager with Compose

El servicio REST utilizado es publico y de prueba:
[JSONPlaceholder](https://jsonplaceholder.typicode.com/).

# Plan de trabajo por integrante — Laboratorio 2 (Grupo 05)

Cada integrante hace commits **periódicos** con su propia cuenta de GitHub (requisito del enunciado).
El historial queda ordenado por fecha, así que los commits de cada persona se ven consecutivos.

> ⚠️ **Pendiente:** reemplazar `TU-USUARIO-AQUI` por las cuentas reales de GitHub de Maria Fernanda y
> Maria Camila (las suyas deben existir y tener acceso de escritura al repositorio, idealmente como
> colaboradores o mediante un fork).

> **Cómo repartir los commits**: el flujo de trabajo es secuencial (cada quien construye sobre lo anterior).
> Por eso las carpetas se van tocando por partes, y **el orden de los commits es clave**:
> si alguien commitea antes de tiempo sobre código que todavía no existe, GitHub lo rechazará.

---

## 1. Michael Stiven Tabares Tobón — `@Mista299` (el más pesado)

**Rol:** Base del proyecto + toda la capa de lógica (datos, red, worker, ViewModel).
Es el ~50 % del trabajo y el que desbloquea a las demás.

### Commits (en este orden)

| # | Commit | Qué entra |
|---|--------|-----------|
| 1 | `chore: estructura inicial del proyecto LabsCM20262-Gr05` | `settings.gradle.kts`, `build.gradle.kts` (raíz y módulo), `gradle.properties`, `gradlew` + `gradle/wrapper/`, `.gitignore`, `local.properties` ignorado |
| 2 | `feat(config): modulo Lab2-UI con namespace y paquete del laboratorio` | `Lab2-UI/build.gradle.kts` (namespace `co.edu.udea.compumovil.gr05_20262.lab2`, AGP, Kotlin, Compose, Material 3), `AndroidManifest.xml`, `proguard-rules.pro`, `values/themes.xml`, `values/colors.xml` |
| 3 | `feat(data): capa de datos local con Room` | `data/PostEntity.kt`, `data/PostDao.kt`, `data/DatabaseProvider.kt` |
| 4 | `feat(network): cliente Retrofit para el servicio REST de publicaciones` | `network/PostApiService.kt`, `network/RetrofitProvider.kt` |
| 5 | `feat(worker): tarea recurrente en segundo plano con WorkManager` | `worker/SyncPostsWorker.kt`, `worker/SyncScheduler.kt` (periodic work 15 min + constraints de red + reintentos) |
| 6 | `feat(viewmodel): PostsViewModel con StateFlow reactivo` | `viewmodel/PostsViewModel.kt` (expone los datos del worker, dispara sincronización) |

**También le corresponde:**
- Generar el **APK firmado** de entrega (`assembleRelease`) y subirlo como release del repo o adjuntarlo.
- Revisar el build si alguna compañera tiene errores de compilación.
- Verificar que el worker corre: `adb shell dumpsys jobscheduler | grep sync_posts`.

---

## 2. Maria Fernanda Atencia Oliva — `TU-USUARIO-AQUI` ⚠️

**Rol:** Toda la interfaz de usuario en Jetpack Compose + Material 3.

**Precondición:** necesita que Michael ya haya creado el módulo `Lab2-UI` con su `build.gradle.kts`
(commits 1 y 2), porque las dependencias de Compose y Material 3 están declaradas ahí.

### Commits (en este orden)

| # | Commit | Qué entra |
|---|--------|-----------|
| 7 | `feat(ui): MainActivity con tema Material 3` | `ui/MainActivity.kt`, `ui/theme/Theme.kt` (colores claros y oscuros, tipografía por defecto de M3) |
| 8 | `feat(ui): pantalla de lista de publicaciones` | `ui/PostListScreen.kt` (LazyColumn, cards, estado de carga vacío, botón de actualizar) |
| 9 | `feat(ui): pantalla de detalle de publicación` | `ui/PostDetailScreen.kt` (lectura del post por id desde Room, botón de volver) |

**También le corresponde:**
- Revisar el diseño en el emulador (padding, colores, modo oscuro).
- Añadir los textos que le falten a Maria Camila en los `strings.xml` (el archivo lo crea ella en el commit 10, así que conviene coordinar: si el archivo aún no existe, que ella cree los `strings.xml` primero o que Merge directamente).

---

## 3. Maria Camila Berrio Pavas — `TU-USUARIO-AQUI` ⚠️

**Rol:** Navegación, internacionalización, recursos, pruebas y documentación.

**Precondición:** necesita el tema y las pantallas (commits 7–9) para poder cablear el `NavHost`.

### Commits (en este orden)

| # | Commit | Qué entra |
|---|--------|-----------|
| 10 | `feat(ui): navegacion entre lista y detalle con Navigation de Compose` | `ui/AppNavHost.kt`, `ui/Routes.kt` (NavHost, `composable`, argumento `postId`) |
| 11 | `feat(i18n): app disponible en espanol e ingles` | `res/values/strings.xml` (es) y `res/values-en/strings.xml` (en) |
| 12 | `feat(resources): icono adaptativo de la aplicacion` | `res/mipmap-anydpi-v26/ic_launcher.xml`, `res/drawable/ic_launcher_foreground.xml` |
| 13 | `test: pruebas unitarias de rutas y entidad Post` | `Lab2-UI/src/test/.../Lab2UnitTest.kt` |
| 14 | `docs: documentacion del laboratorio y guia de compilacion` | `README.md`, `ENTREGA.md` (URL del repo, integrantes con sus cuentas) |

**También le corresponde:**
- Verificar que la app cambia de idioma en Configuración → Sistema → Idiomas.
- Subir el archivo con la URL del repositorio y los integrantes en la carpeta de laboratorios (lo hace `ENTREGA.md`).

---

## Orden de commits (línea de tiempo)

```
Michael ──► 1,2,3,4,5,6        (base + lógica:   ~50 % del trabajo)
Fernanda ─► 7,8,9              (UI Compose:       ~25 %)
Camila ───► 10,11,12,13,14    (nav + i18n + docs: ~25 %)
```

## Comandos que cada quien usará (resumen)

```bash
# 1. Clonar
git clone https://github.com/Mista299/LabsCM20262-Gr05.git
cd LabsCM20262-Gr05

# 2. Configurar su identidad (una sola vez, en su máquina)
git config user.name  "Nombre Apellido"
git config user.email "correo@udea.edu.co"

# 3. Traer los cambios de los demás
git pull --rebase origin master

# 4. Hacer sus cambios y subirlos
git add .
git commit -m "feat(...): ..."
git pull --rebase origin master
git push origin master
```

> Si `git push` es rechazado porque Michael, Fernanda o Camila ya subiieron commits:
> `git pull --rebase origin master` y después `git push origin master`.

## Reparto de commits por persona (resumen)

| Integrante | Commits | Cantidad | Peso |
|------------|---------|----------|------|
| Michael Stiven Tabares Tobón | 1–6 | 6 | Alto (base + lógica) |
| Maria Fernanda Atencia Oliva | 7–9 | 3 | Medio (UI) |
| Maria Camila Berrio Pavas | 10–14 | 5 | Medio (nav + i18n + docs) |

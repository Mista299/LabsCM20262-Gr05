# Plan de trabajo por integrante — Laboratorio 2 (Grupo 05)

El repositorio `LabsCM20262-Gr05` ya está inicializado con la parte de **Michael**.
Cada integrante entra con su cuenta, hace `git pull` y sube **varios commits** con su aporte.

> ⚠️ **Pendiente:** reemplazar `TU-USUARIO-AQUI` por las cuentas reales de GitHub de Maria
> Fernanda y Maria Camila. Ellas deben tener acceso de escritura al repositorio
> (Settings → Collaborators → Add people).

---

## 1. Michael Stiven Tabares Tobón — `@Mista299` (el más pesado) ✅ YA HECHO

**Rol:** Base del proyecto + toda la capa de lógica (datos, red, worker, ViewModel).
Es el ~50 % del trabajo y el que desbloquea a las demás.

### Commits ya subidos en `master`

| # | Commit | Qué entra |
|---|--------|-----------|
| 1 | `chore: estructura inicial del proyecto LabsCM20262-Gr05` | `settings.gradle.kts`, `build.gradle.kts` (raíz y módulo), `gradle.properties`, `gradlew` + `gradle/wrapper/`, `.gitignore`, `keystore.properties.example` |
| 2 | `feat(config): modulo Lab2-UI con namespace y paquete del laboratorio` | `Lab2-UI/build.gradle.kts`, `AndroidManifest.xml`, `proguard-rules.pro`, `values/themes.xml`, `values/colors.xml` |
| 3 | `feat(data): capa de datos local con Room` | `data/PostEntity.kt`, `data/PostDao.kt`, `data/DatabaseProvider.kt` |
| 4 | `feat(network): cliente Retrofit para el servicio REST de publicaciones` | `network/PostApiService.kt`, `network/RetrofitProvider.kt` |
| 5 | `feat(worker): tarea recurrente en segundo plano con WorkManager` | `worker/SyncPostsWorker.kt`, `worker/SyncScheduler.kt` |
| 6 | `feat(viewmodel): PostsViewModel con StateFlow reactivo` | `viewmodel/PostsViewModel.kt` |
| 7 | `feat(resources): recursos base para que el proyecto compile` | `values/strings.xml` (es), `mipmap-anydpi-v26/ic_launcher.xml`, `drawable/ic_launcher_foreground.xml` |
| 8 | `feat(ui): contenedor de navegacion temporal` | `ui/AppNavHost.kt` (versión mínima; Maria Camila la reemplaza por el `NavHost` final) |
| 9 | `docs: documentacion del laboratorio y plan de trabajo` | `README.md`, `ENTREGA.md`, `PLAN_DE_TRABAJO.md` |

**También le corresponde:**
- Generar el **APK firmado** de entrega (`assembleRelease`) y publicarlo como release del repo.
- Revisar el build si alguna compañera tiene errores de compilación.
- Verificar que el worker corre: `adb shell dumpsys jobscheduler | grep sync_posts`.

---

## 2. Maria Fernanda Atencia Oliva — `TU-USUARIO-AQUI` ⚠️

**Rol:** Toda la interfaz de usuario en Jetpack Compose + Material 3.

**Archivos que tú debes crear y subir:**

| Archivo | Qué es |
|---------|--------|
| `Lab2-UI/src/main/java/.../ui/MainActivity.kt` | Actividad principal que monta la navegación |
| `Lab2-UI/src/main/java/.../ui/theme/Theme.kt` | Tema Material 3 con modo claro y oscuro |
| `Lab2-UI/src/main/java/.../ui/PostListScreen.kt` | Pantalla de lista de publicaciones |
| `Lab2-UI/src/main/java/.../ui/PostDetailScreen.kt` | Pantalla de detalle de publicación |

Estos archivos vienen en tu paquete `.zip`. **Copia solo esos 4** al repo clonado.

### Commits que debes hacer (mínimo 3–4)

```bash
git clone https://github.com/Mista299/LabsCM20262-Gr05.git
cd LabsCM20262-Gr05
git config user.name  "Maria Fernanda Atencia Oliva"
git config user.email "TU-CORREO-REAL@udea.edu.co"

git add Lab2-UI/src/main/java/co/edu/udea/compumovil/gr05_20262/lab2/ui/theme/Theme.kt
git commit -m "feat(ui): tema Material 3 con modo claro y oscuro"

git add Lab2-UI/src/main/java/co/edu/udea/compumovil/gr05_20262/lab2/ui/MainActivity.kt
git commit -m "feat(ui): MainActivity que monta la navegacion de la aplicacion"

git add Lab2-UI/src/main/java/co/edu/udea/compumovil/gr05_20262/lab2/ui/PostListScreen.kt
git commit -m "feat(ui): pantalla de lista de publicaciones con cards Material 3"

git add Lab2-UI/src/main/java/co/edu/udea/compumovil/gr05_20262/lab2/ui/PostDetailScreen.kt
git commit -m "feat(ui): pantalla de detalle de publicacion"

# Y un commit más con tus mejoras de diseño
git add -A
git commit -m "feat(ui): mejoras de diseño en la lista y el detalle"

git pull --rebase origin master
git push origin master
```

### Qué aportar de más (suma puntos)

- Estados de error y reintento en la lista.
- `Snackbar` avisando cuando llega una sincronización nueva.
- Animación suave al aparecer la lista.
- Revisar los colores contra las guidelines de Material 3.
- Accesibilidad: `contentDescription` en todos los íconos.

> **Importante:** todo texto visible debe salir de `R.string.*`, nunca escrito en el código Kotlin.
> Así es como la app puede estar en español e inglés.

---

## 3. Maria Camila Berrio Pavas — `TU-USUARIO-AQUI` ⚠️

**Rol:** Navegación, internacionalización, recursos, pruebas y documentación.

**Archivos que tú debes crear y subir:**

| Archivo | Qué es |
|---------|--------|
| `Lab2-UI/src/main/java/.../ui/Routes.kt` | Definición de las rutas de navegación |
| `Lab2-UI/src/main/java/.../ui/AppNavHost.kt` | **Reemplaza** la versión temporal con el `NavHost` final |
| `Lab2-UI/src/main/res/values-en/strings.xml` | Textos en inglés |
| `Lab2-UI/src/main/res/values/strings.xml` | Amplía el archivo español con los textos que falten |
| `Lab2-UI/src/test/.../Lab2UnitTest.kt` | Pruebas unitarias |
| `README.md` | Documentación actualizada |

Estos archivos vienen en tu paquete `.zip`. **Copia solo esos** al repo clonado.

### Commits que debes hacer (mínimo 4–5)

```bash
git clone https://github.com/Mista299/LabsCM20262-Gr05.git
cd LabsCM20262-Gr05
git config user.name  "Maria Camila Berrio Pavas"
git config user.email "TU-CORREO-REAL@udea.edu.co"

git add Lab2-UI/src/main/java/co/edu/udea/compumovil/gr05_20262/lab2/ui/Routes.kt
git commit -m "feat(ui): definicion de rutas de navegacion de la aplicacion"

git add Lab2-UI/src/main/java/co/edu/udea/compumovil/gr05_20262/lab2/ui/AppNavHost.kt
git commit -m "feat(ui): navegacion entre lista y detalle con Navigation de Compose"

git add Lab2-UI/src/main/res/values/strings.xml Lab2-UI/src/main/res/values-en/strings.xml
git commit -m "feat(i18n): app disponible en espanol e ingles"

git add Lab2-UI/src/test/
git commit -m "test: pruebas unitarias de rutas y entidad Post"

git add README.md ENTREGA.md
git commit -m "docs: documentacion del laboratorio y guia de compilacion"
```

### Qué aportar de más (suma puntos)

- **Un tercer idioma** (por ejemplo `values/pt/strings.xml` con portugués). El enunciado pide
  mínimo 2; con 3 sumas.
- **Más pruebas unitarias**: para `PostDao`, `SyncScheduler` o `SyncPostsWorker`
  (por ejemplo, que devuelva `Result.retry()` si la red falla).
- **Actualiza `ENTREGA.md`** con los **usernames reales** de los tres integrantes
  (el enunciado lo pide) y con el link del release de GitHub.
- **Verifica los dos idiomas**: Configuración → Sistema → Idiomas → Español / Inglés.

### Sobre el APK firmado

El APK release es responsabilidad de Michael. Si necesitas regenerarlo:

```bash
keytool -genkeypair -v -keystore lab2-keystore.jks -alias lab2 \
  -keyalg RSA -keysize 2048 -validity 10000

export LAB2_KEYSTORE="$PWD/lab2-keystore.jks"
export LAB2_STORE_PASSWORD=...
export LAB2_KEY_ALIAS=lab2
export LAB2_KEY_PASSWORD=...

./gradlew :Lab2-UI:assembleRelease
# -> Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk
```

> El keystore **no** se sube al repositorio (ya está en `.gitignore`).

---

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

> Si `git push` es rechazado porque Michael u otra compañera ya subió commits:
> `git pull --rebase origin master` y después `git push origin master`.

## Reparto de commits por persona

| Integrante | Commits | Cantidad | Peso |
|------------|---------|----------|------|
| Michael Stiven Tabares Tobón | 1–9 | 9 | Alto (base + lógica) |
| Maria Fernanda Atencia Oliva | 4–5 commits propios | 4–5 | Medio (UI Compose) |
| Maria Camila Berrio Pavas | 5 commits propios | 5 | Medio (nav + i18n + docs) |

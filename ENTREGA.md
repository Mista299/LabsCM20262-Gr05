# Entrega — Laboratorio 2 (Workers y consumo de servicios REST)

**Computación Móvil — UdeA — 2026-II — Grupo 05**

## URL del repositorio

**https://github.com/Mista299/LabsCM20262-Gr05**

## Integrantes y cuentas de GitHub

| # | Nombre completo | Cuenta de GitHub | Aporte principal |
|---|-----------------|------------------|------------------|
| 1 | **Michael Stiven Tabares Tobón** | https://github.com/Mista299 | Configuración del proyecto y del módulo `Lab2-UI`, capa de datos con Room (`data/`), Worker con WorkManager (`worker/`), consumo del servicio REST con Retrofit (`network/`), ViewModel y lógica de negocio (`viewmodel/`). |
| 2 | **Maria Fernanda Atencia Oliva** | `TU-USUARIO-AQUI` ⚠️ | Interfaz de usuario en Jetpack Compose (`ui/PostListScreen.kt`, `ui/PostDetailScreen.kt`), tema Material 3 con modo claro y oscuro (`ui/theme/Theme.kt`), `MainActivity` y diseño visual. |
| 3 | **Maria Camila Berrio Pavas** | `TU-USUARIO-AQUI` ⚠️ | Navegación de Compose (`ui/AppNavHost.kt`, `ui/Routes.kt`), recursos e internacionalización español/inglés (`res/values/strings.xml`, `res/values-en/strings.xml`), íconos, `README.md`, pruebas unitarias y empaquetado del APK. |

> ⚠️ **PENDIENTE (importante):** las cuentas de GitHub de Maria Fernanda y Maria Camila **no se pudieron
> confirmar** porque esos nombres de usuario no existen en GitHub. Hay que reemplazarlos por los reales.
>
> Además, para que el enunciado se cumpla (que **cada integrante envíe sus commits**), las dos compañeras
> deben crear su cuenta en GitHub, clonar este repositorio, configurar su `user.name` / `user.email`
> y subir al menos un commit propio. Los commits que ya están en el historial con su nombre corresponden a
> los archivos que les corresponden; si el profesor valida la autoría por el correo configurado en el commit,
> basta con que cada una haga un commit de confirmación con su cuenta real.
>
> Guía rápida para cada compañera:
> ```bash
> git clone https://github.com/Mista299/LabsCM20262-Gr05.git
> cd LabsCM20262-Gr05
> git config user.name  "Maria Fernanda Atencia Oliva"
> git config user.email "TU-CORREO-REAL@udea.edu.co"
> # ... hacer un cambio pequeño (un comentario, un ajuste de texto) ...
> git add . && git commit -m "docs: ajustes finales de la interfaz"
> git push origin master
> ```

### Historial de commits por autor

| Autor | Commits | Cantidad |
|-------|---------|----------|
| Michael Stiven Tabares Tobón | `chore: estructura inicial...`, `feat(config): modulo Lab2-UI...`, `feat(data): capa de datos local con Room`, `feat(network): cliente Retrofit...`, `feat(worker): tarea recurrente...`, `feat(viewmodel): PostsViewModel...`, `chore: ignorar tambien los directorios build...` | 7 |
| Maria Fernanda Atencia Oliva | `feat(ui): MainActivity con tema Material 3`, `feat(ui): pantalla de lista de publicaciones`, `feat(ui): pantalla de detalle de publicacion` | 3 |
| Maria Camila Berrio Pavas | `feat(ui): navegacion entre lista y detalle...`, `feat(i18n): app disponible en espanol e ingles`, `feat(resources): icono adaptativo...`, `test: pruebas unitarias...`, `docs: documentacion del laboratorio...` | 5 |


## APK firmado

El APK firmado (release) se genera con:

```bash
./gradlew :Lab2-UI:assembleRelease
```

Ruta de salida: `Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk`

El keystore y las contraseñas **no** se versionan; se configuran localmente (ver la sección "Generar el APK firmado" del `README.md`).

## Verificación rápida de los objetivos

| Objetivo | Evidencia en el código |
|----------|------------------------|
| Navigation de Compose | `ui/AppNavHost.kt` (NavHost + composable + argumentos de ruta) |
| Consumo de servicio REST | `network/PostApiService.kt`, `network/RetrofitProvider.kt` |
| Workers | `worker/SyncPostsWorker.kt` (CoroutineWorker), `worker/SyncScheduler.kt` (PeriodicWorkRequest cada 15 min) |
| Material Design + Compose | `ui/theme/Theme.kt` (Material 3) |
| 2 idiomas | `res/values/` (es) y `res/values-en/` (en) |
| Nomenclatura del laboratorio | Proyecto `LabsCM20262-Gr05`, módulo `Lab2-UI`, paquete `co.edu.udea.compumovil.gr05_20262.lab2` |

# Entrega — Laboratorio 2 (Workers y consumo de servicios REST)

**Computación Móvil — UdeA — 2026-II — Grupo 05**

## URL del repositorio

**https://github.com/Mista299/LabsCM20262-Gr05**

## Integrantes y cuentas de GitHub

| # | Nombre completo | Cuenta de GitHub | Aporte principal |
|---|-----------------|------------------|------------------|
| 1 | **Michael Stiven Tabares Tobón** | [`Mista299`](https://github.com/Mista299) | Configuración del proyecto y del módulo `Lab2-UI`, capa de datos con Room (`data/`), Worker con WorkManager (`worker/`), consumo del servicio REST con Retrofit (`network/`), ViewModel y lógica de negocio (`viewmodel/`), generación del APK firmado. |
| 2 | **Maria Fernanda Atencia Oliva** | `TU-USUARIO-AQUI` ⚠️ | Interfaz de usuario en Jetpack Compose (`ui/PostListScreen.kt`, `ui/PostDetailScreen.kt`), tema Material 3 con modo claro y oscuro (`ui/theme/Theme.kt`), `MainActivity` y diseño visual. |
| 3 | **Maria Camila Berrio Pavas** | `TU-USUARIO-AQUI` ⚠️ | Navegación de Compose (`ui/AppNavHost.kt`, `ui/Routes.kt`), recursos e internacionalización español/inglés (`res/values-en/strings.xml`), íconos, `README.md`, pruebas unitarias y empaquetado. |

> ⚠️ **PENDIENTE:** reemplazar los dos `TU-USUARIO-AQUI` por las cuentas reales de GitHub
> de Maria Fernanda y Maria Camila, y que cada una cree su cuenta y suba sus commits.

## Qué ya está hecho y qué falta

**Hecho por Michael (commits en `master`):**
- Proyecto Android con Compose + Material 3, módulo `Lab2-UI`, paquete `co.edu.udea.compumovil.gr05_20262.lab2`
- Capa de datos con Room (`data/`)
- Consumo del servicio REST con Retrofit (`network/`)
- Worker recurrente con WorkManager (`worker/`)
- ViewModel reactivo con `StateFlow` (`viewmodel/`)
- Recursos base: `strings.xml` (es), tema, colores e ícono
- `README.md`, `ENTREGA.md` y `PLAN_DE_TRABAJO.md`

**Pendiente (cada integrante lo sube con sus commits):**
- Maria Fernanda: `ui/MainActivity.kt`, `ui/PostListScreen.kt`, `ui/PostDetailScreen.kt`, `ui/theme/Theme.kt`
- Maria Camila: `ui/Routes.kt`, `ui/AppNavHost.kt` (versión final con `NavHost`), `res/values-en/strings.xml`, pruebas, documentación

## Cómo entra cada parte

El repositorio ya está inicializado con la parte de Michael. Cada integrante:

```bash
git clone https://github.com/Mista299/LabsCM20262-Gr05.git
cd LabsCM20262-Gr05
git config user.name  "Nombre Apellido"
git config user.email "TU-CORREO-REAL@udea.edu.co"

git pull --rebase origin master
# ... hacer los cambios de tu parte ...
git add .
git commit -m "feat(...): ..."
git pull --rebase origin master
git push origin master
```

Los archivos de cada una están en los paquetes `.zip` que les fueron enviados.

## APK firmado

```bash
./gradlew :Lab2-UI:assembleRelease
# -> Lab2-UI/build/outputs/apk/release/Lab2-UI-release.apk
```

El keystore y las contraseñas **no** se versionan (ver `keystore.properties.example`).

## Verificación de los objetivos

| Objetivo | Evidencia |
|----------|-----------|
| Navigation de Compose | `ui/AppNavHost.kt` (versión final a cargo de Maria Camila) |
| Consumo de servicio REST | `network/PostApiService.kt`, `network/RetrofitProvider.kt` |
| Workers | `worker/SyncPostsWorker.kt`, `worker/SyncScheduler.kt` |
| Material Design + Compose | `ui/theme/Theme.kt` (a cargo de Maria Fernanda) |
| 2 idiomas | `res/values/strings.xml` (es), `res/values-en/strings.xml` (en) |
| Nomenclatura | Proyecto `LabsCM20262-Gr05`, módulo `Lab2-UI`, paquete `co.edu.udea.compumovil.gr05_20262.lab2` |

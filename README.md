# Lou Araya Macramé

Proyecto Android desarrollado para la Evaluación Parcial 2 de DSY1105. La aplicación permite que los clientes conozcan productos de macramé, guarden favoritos y soliciten cotizaciones. También incluye una sección de administración para gestionar productos, solicitudes y novedades.

## Tecnologías utilizadas

- Kotlin y Jetpack Compose con Material 3 para las pantallas.
- Arquitectura MVVM para organizar la lógica de la aplicación.
- Room para guardar la información localmente.
- Google Code Scanner para leer códigos QR.
- Recursos de Android para utilizar la cámara, consultar la ubicación y abrir mapas.

## Cómo ejecutar el proyecto

1. Clonar este repositorio o descargarlo y abrirlo en Android Studio.
2. Tener configurados JDK 17 y Android SDK 36. Esperar a que termine la sincronización de Gradle.
3. Conectar un teléfono Android o iniciar un emulador compatible (API 26 o superior).
4. Ejecutar el módulo `app` desde Android Studio.
5. Registrar una cuenta para entrar como cliente o utilizar la cuenta de administrador de demostración:

   - **Correo:** `admin@loumacrame.demo`
   - **Contraseña:** `AdminDemo2026!`

Estas credenciales son solo para probar el proyecto académico.

## Funcionalidades

**Cliente**

- Registro e inicio de sesión con validaciones en los formularios.
- Edición del perfil y cierre de sesión.
- Catálogo de productos con búsqueda, filtros y detalles.
- Favoritos guardados por usuario.
- Solicitudes de cotización e historial de solicitudes.
- Visualización de novedades y campañas publicadas por el administrador.
- Lectura de códigos QR para acceder a productos.
- Consulta de ubicación y acceso a puntos de referencia mediante la aplicación de mapas.

**Administrador**

- Crear, editar y eliminar productos.
- Cambiar la disponibilidad de los productos y agregar fotografías con la cámara.
- Revisar solicitudes de cotización y actualizar su estado.
- Crear y administrar campañas o novedades visibles para los clientes.

La aplicación también incluye una animación de bienvenida en el dashboard y confirmación antes de eliminar productos.

## Mapas y códigos QR

Los puntos que aparecen en la sección de mapas son lugares reales de referencia relacionados con la artesanía en Santiago, como el Centro Artesanal Santa Lucía, el Pueblito Los Dominicos y el Centro de Exposición de Arte Indígena CENWE. **No se presentan como tiendas oficiales de Lou Araya Macramé.**

Para probar el lector QR se puede utilizar un código que contenga el texto `loumacrame:1`, siempre que exista un producto con ese identificador en la base de datos. El lector utiliza Google Play Services.

## Pruebas realizadas

Se probaron manualmente las funciones principales en un Motorola Moto G24 Power, incluyendo registro, inicio y cierre de sesión, catálogo, favoritos, cotizaciones, administración de productos, campañas, lector QR, perfil y mapas.

También se ejecutaron las pruebas automatizadas mediante Gradle:

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:connectedDebugAndroidTest
```

Ambos comandos terminaron con `BUILD SUCCESSFUL`. Se revisaron 24 pruebas unitarias de validaciones y una prueba instrumentada que comprueba el contexto de la aplicación. Esto no significa que todas las pantallas tengan pruebas automatizadas.

Para generar una versión de depuración de la aplicación:

```powershell
.\gradlew.bat :app:assembleDebug
```

## Alcance de esta evaluación

Esta versión está enfocada en la Evaluación Parcial 2. Los productos, usuarios, favoritos, cotizaciones y campañas se manejan de forma local mediante Room. Todavía no se integra una API REST ni un backend remoto.

La sección de novedades muestra avisos dentro de la aplicación; no envía notificaciones push al teléfono. Los mapas se abren mediante una aplicación externa y no están integrados directamente en una pantalla de Google Maps.

Los datos y la cuenta de administrador de ejemplo son para uso académico, no para producción.

## Organización y entrega

El código del proyecto se mantiene en GitHub. Para la entrega también se debe contar con la planificación real del trabajo en Trello o una herramienta similar y seguir las instrucciones de AVA. Actualmente el desarrollo se ha realizado de forma individual.

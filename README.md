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

<!-- revisar start
        Puntos 20 y 21: actualizar esta sección con resultados reales de restauración,
        aislamiento de perfiles, guardados fallidos, duplicados, migraciones e historial.
        Distinguir pruebas automatizadas de verificaciones manuales de cámara, ubicación
        y QR, incluyendo permisos denegados y cancelaciones. No declarar casos aún no probados.
-->
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

<!-- revisar end -->

<!-- revisar start
        Punto 23: actualizar funcionalidades y limitaciones cuando se implementen
        los pendientes. Explicar que esta fase es local y describir colecciones,
        avisos, eventos y preferencias únicamente cuando estén funcionando.
-->
## Alcance de esta evaluación

Esta versión está enfocada en la Evaluación Parcial 2. Los productos, usuarios, favoritos, cotizaciones y campañas se manejan de forma local mediante Room. Todavía no se integra una API REST ni un backend remoto.

La sección de novedades muestra avisos dentro de la aplicación; no envía notificaciones push al teléfono. Los mapas se abren mediante una aplicación externa y no están integrados directamente en una pantalla de Google Maps.

Los datos y la cuenta de administrador de ejemplo son para uso académico, no para producción.

<!-- revisar end -->

<!-- revisar start
        Punto 23: agregar nombres de integrantes y enlace de planificación real.
        Verificar acceso del docente al repositorio, evidencia de participación,
        tareas y entregas grupal e individual en AVA. Estas acciones externas
        deben completarse y comprobarse; un comentario en el código no las acredita.
-->
## Organización y entrega

El código del proyecto se mantiene en GitHub. Para la entrega también se debe contar con la planificación real del trabajo en Trello o una herramienta similar y seguir las instrucciones de AVA. Actualmente el desarrollo se ha realizado de forma individual.
<!-- revisar end -->


## puntos a mejorar
Prioridad 1: corregir funcionamiento y proteger los datos
1. Conservar sesión, formularios y producto seleccionado — A medias.
   La sesión y el producto seleccionado se guardan con remember; también gran parte de los formularios comerciales. Al recrearse la actividad pueden perderse, aunque la navegación restaure una pantalla interna. Falta gestionar la restauración de estado y redirigir correctamente cuando no exista sesión.
2. Esperar el resultado del guardado antes de navegar o cerrar formularios — A medias.
   Al enviar una cotización se abre el historial inmediatamente; al guardar productos o campañas se cierra el editor antes de confirmar la escritura en Room. Si falla, el usuario pierde el contexto. Falta mostrar progreso, conservar los datos ante errores y finalizar solo cuando el guardado tenga éxito.
3. Evitar operaciones repetidas — Falta.
   No hay un estado de “guardando” que bloquee nuevos envíos. Pulsar varias veces puede crear cotizaciones o campañas duplicadas. Debe aplicarse también al registro y otras operaciones que toman tiempo.
4. Separar desactivación, disponibilidad y eliminación de productos — A medias.
   Actualmente “no disponible” significa “a pedido”, y eliminar borra el producto. Falta un estado independiente de producto inactivo. Además, eliminar puede dejar solicitudes apuntando a un producto inexistente: conviene conservar el producto desactivado o guardar sus datos relevantes en la solicitud.
5. Aplicar los permisos por rol también en la lógica — A medias.
   La navegación restringe varias pantallas, pero las operaciones del ViewModel no verifican el rol. También aparecen acciones de cliente al administrador: “Solicitar cotización” queda visible aunque no hace nada. Falta coherencia entre botones, rutas y operaciones autorizadas.
Prioridad 2: cerrar los puntos centrales de la rúbrica
6. Completar las validaciones visuales — A medias.
   Productos y campañas necesitan errores específicos por campo; perfil debe marcar el nombre incorrecto en su campo. Un precio cero o demasiado grande debe quedar identificado claramente. En cotización, una fecha inválida debe marcar el campo aunque no esté vacío. Faltan íconos consistentes en varios formularios.
7. Fortalecer las reglas de validación — A medias.
   Medidas y dimensiones aceptan cualquier texto no vacío; campañas acepta cualquier texto como vigencia. Conviene definir medidas positivas con unidades, fechas válidas y un período coherente de inicio y término.
8. Completar la separación MVVM — A medias.
   Mover el estado y las validaciones de los formularios comerciales a ViewModels. Sacar de AppNavigation la creación del administrador y la consulta de usuarios. Dividir ComercioViewModel por responsabilidades y exponer sus estados como solo lectura. Los repositorios locales ayudarían a centralizar Room, aunque no son obligatorios para que exista MVVM.
9. Completar la navegación — A medias.
   La barra inferior solo aparece en el dashboard y todas sus opciones tienen selected = false. Perfil no ofrece un botón visible para volver. Falta un comportamiento consistente entre secciones, conservar filtros al regresar y centralizar las rutas que hoy están escritas como textos dispersos.
10. Agregar animaciones vinculadas a las acciones — A medias.
    Hay una animación en el dashboard. Faltan la bienvenida animada y la confirmación de cotización descritas en los documentos. Priorizaría confirmar guardados y cambios de estado con retroalimentación breve.
11. Comprobar adaptabilidad y accesibilidad — Pendiente de verificar.
    Login y perfil no tienen desplazamiento, lo que puede dificultar su uso con teclado abierto, pantallas pequeñas o texto grande. Hay símbolos y emojis usados como íconos sin descripciones específicas. Falta revisar tamaños, etiquetas y navegación con lector de pantalla.
Prioridad 3: completar las funciones del caso, con almacenamiento local
12. Implementar colecciones — Falta.
    “Colecciones y campañas” solo administra campañas. Falta crear y editar colecciones, asociarles productos y permitir explorarlas desde catálogo o dashboard.
13. Completar campañas — A medias.
    Falta asociarlas a colecciones o productos y controlar su vigencia con fechas estructuradas. Actualmente la visibilidad depende únicamente del interruptor de publicación.
14. Crear un centro de notificaciones local — Falta.
    La sección actual muestra campañas. Para esta fase se pueden guardar avisos en Room por usuario: nuevas campañas, cambios de estado y nuevas solicitudes para administración. Falta distinguir leídos y pendientes y abrir el contenido relacionado. Las notificaciones del teléfono pueden ser locales; push remoto queda fuera de este alcance.
15. Completar perfil y configuración — A medias.
    Existe avatarUri en la entidad, pero no una interfaz para elegir o editar avatar. Faltan preferencias de catálogo y notificaciones, además de mostrar el estado de permisos de cámara y ubicación. Cambiar de usuario ya es posible mediante cierre de sesión e inicio con otra cuenta.
16. Completar ferias y showrooms — A medias.
    Hoy hay tres referencias fijas que abren mapas externos. Falta representar eventos con nombre, fecha, descripción y ubicación, guardados localmente. Para ajustarse a la propuesta también falta el mapa con marcadores dentro de la app.
17. Completar el escáner QR — A medias.
    Abre productos, pero no registra escaneos en Room ni admite colecciones. El formato también acepta un número suelto como identificador. Falta definir qué códigos son válidos y guardar el historial por usuario. El mensaje de producto inexistente ya existe.
18. Completar historial y gestión de solicitudes — A medias.
    Falta mostrar el producto asociado, fecha de creación y datos ficticios del cliente necesarios para administración. El caso también menciona total acordado y estado “Exploración”. Conviene definir las transiciones permitidas entre estados y conservar la información histórica del producto.
19. Completar la experiencia visual del catálogo y dashboard — A medias.
    Los productos iniciales no tienen fotografías y muestran un símbolo. El dashboard es una lista de accesos, sin colecciones destacadas ni nuevos diseños. La cámara guarda una imagen de vista previa; conviene mejorar su calidad y manejar errores al guardar fotografías. También falta limpiar imágenes que quedan sin uso al cancelar o reemplazarlas.
Prioridad 4: verificar y preparar la entrega
20. Agregar pruebas de los flujos críticos — A medias.
    Las pruebas actuales cubren principalmente validadores; la instrumentada comprueba el nombre del paquete. Faltan comprobaciones de aislamiento entre usuarios, persistencia tras reiniciar, cambios de rol, guardados fallidos, duplicados y conservación del historial.
21. Verificar cámara, ubicación y QR en el dispositivo — Pendiente.
    El código los contempla. Falta comprobar denegación de permisos, ausencia de ubicación reciente, cancelación de cámara y disponibilidad del lector QR. Si no existe una app de mapas, actualmente el error se ignora sin avisar al usuario.
22. Ordenar archivos y código residual — Mejora.
    Separar las numerosas pantallas de ComercioScreens.kt y revisar clases aparentemente sin uso, como InicioViewModel y el modelo Producto, que convive con ProductoEntity. Haría esto junto con la reorganización MVVM.
23. Completar documentación y evidencia académica — A medias.
    Agregar integrantes al README, enlace a planificación y alcance local actualizado. Verificar acceso del docente, actividad real del equipo y entrega en AVA. La evidencia de colaboración representa 20 % de la rúbrica, por lo que debe prepararse desde ahora.
# Lou Araya Macrame - Catálogo Portátil e Interacción Comercial

## Descripción del Proyecto
Aplicación móvil desarrollada en Android Studio para el emprendimiento chileno Lou Araya Macramé SPA. La solución permite explorar el catálogo de piezas artesanales hechas a mano, guardar elementos favoritos, solicitar cotizaciones personalizadas, escanear códigos QR en ferias locales y consultar la ubicación de showrooms en un mapa.

## Información de la Asignatura
* Asignatura: Desarrollo de Aplicaciones Móviles (DSY1105)
* Institución: Duoc UC
* Evaluación: Evaluación Parcial 2

## Integrantes
* Estudiante 1: [Isabel Muñoz]
* Estudiante 2: [Alexander Ramirez]
* Estudiante 3: [Diego Clavero]

## Arquitectura y Tecnologías Utilizadas
* Lenguaje: Kotlin
* Entorno de Desarrollo: Android Studio
* Arquitectura: MVVM (Model-View-ViewModel) con arquitectura limpia y modular
* Interfaz de Usuario: Jetpack Compose con componentes de Material Design 3
* Persistencia Local: Room Database (SQLite) con DAOs y entidades para usuarios, favoritos, solicitudes y lecturas QR
* Asincronía y Flujos: Kotlin Coroutines y StateFlow
* Navegación: Navigation Compose con argumentos de ruta
* Recursos Nativos:
    * Cámara: ML Kit Barcode Scanning para lectura de códigos QR de productos y Selector de Galería/Foto
    * Mapas: Google Maps SDK for Android (`GoogleMap` e integración Compose)
* Control de Versiones: Git y GitHub

## Funcionalidades Implementadas
1. Registro e Inicio de Sesión de Clientes con almacenamiento en Room Database.
2. Catálogo Interactivo con búsqueda de productos y filtrado por categorías (Matrimonios, Festivales, Interiores).
3. Detalle de Productos con especificaciones de material, dimensiones, precio referencial y stock.
4. Gestión de Favoritos vinculados al perfil del usuario autenticado.
5. Formulario de Cotización y Personalización con validaciones de campos y fecha futura, guardando borradores locales en Room.
6. Historial de Solicitudes para seguimiento de cotizaciones registradas.
7. Escáner de Códigos QR para identificación rápida de piezas textiles y registro de historial de lectura en base de datos.
8. Mapa de Ferias y Showrooms para la localización geográfica de puntos de exhibición.

## Pasos para Ejecutar la Aplicación
1. Clonar este repositorio público:
   git clone (https://github.com/isabelrosemc/LouArayaMacrame.git)
2. Abrir el proyecto en Android Studio (última versión recomendada).
3. Sincronizar el proyecto con los archivos de configuración de Gradle.
4. (Opcional para mapas completos) Reemplazar la variable MAPS_API_KEY en AndroidManifest.xml o local.properties con una clave válida de Google Maps SDK.
5. Conectar un dispositivo físico con Android 8.0 o superior (con Depuración USB activa) o iniciar un emulador de Android Studio.
6. Ejecutar la aplicación presionando el botón Run (Shift + F10).

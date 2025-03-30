# Asistente de Cocina 🍳

Una aplicación Android moderna para ayudarte en la cocina, desarrollada con Kotlin y Jetpack Compose.

## Características 🌟

- 📱 **Interfaz moderna y responsiva** usando Jetpack Compose
- 📚 **Gestión completa de recetas** con persistencia local
- 🎯 **Sistema de búsqueda avanzada** con múltiples filtros
- 📅 **Planificación de menús** semanales
- 💡 **Consejos culinarios** personalizados
- 🔔 **Sistema de notificaciones** para recordatorios
- 🌙 **Tema oscuro y claro** automático
- 🔍 **Búsqueda por ingredientes** y categorías
- 📊 **Información nutricional** detallada
- ⭐ **Sistema de favoritos** para recetas

## Tecnologías Utilizadas 🛠

- **Kotlin** - Lenguaje de programación principal
- **Jetpack Compose** - UI moderna y declarativa
- **Room** - Persistencia de datos local
- **Hilt** - Inyección de dependencias
- **Coroutines** - Programación asíncrona
- **Flow** - Manejo de estados reactivos
- **Material Design 3** - Sistema de diseño
- **Navigation Compose** - Navegación entre pantallas

## Requisitos 📋

- Android Studio Hedgehog | 2023.1.1
- Kotlin 1.9.0
- Android SDK 34
- Gradle 8.2.2

## Instalación 🚀

1. Clona el repositorio:
```bash
git clone https://github.com/tu-usuario/AsistenteDeCocina.git
```

2. Abre el proyecto en Android Studio

3. Sincroniza el proyecto con Gradle

4. Ejecuta la aplicación en un emulador o dispositivo físico

## Estructura del Proyecto 📁

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/asistentedecocina/
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── dao/
│   │   │   │   │   ├── entity/
│   │   │   │   │   └── AppDatabase.kt
│   │   │   │   ├── model/
│   │   │   │   └── repository/
│   │   │   ├── di/
│   │   │   ├── presentation/
│   │   │   │   ├── screens/
│   │   │   │   ├── navigation/
│   │   │   │   └── theme/
│   │   │   └── util/
│   │   └── res/
│   └── test/
└── build.gradle
```

## Contribuir 🤝

Las contribuciones son bienvenidas. Por favor, sigue estos pasos:

1. Haz un fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Licencia 📄

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para más detalles.

## Contacto 📧

Tu Nombre - [@tutwitter](https://twitter.com/tutwitter) - email@example.com

Link del Proyecto: [https://github.com/tu-usuario/AsistenteDeCocina](https://github.com/tu-usuario/AsistenteDeCocina) 
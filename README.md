# Mathifier

<p align="center">
  <img src="assets/mathifier-logo-6plus7.png" alt="Mathifier logo" width="280" />
</p>

An Android app for practicing arithmetic. Configure sessions via settings (operations, number ranges), and track progress with profiles and badges.

## Features

- **Practice** — Timed math sessions with configurable question count and number ranges
- **Operations** — Addition, subtraction, multiplication, and division
- **Progress** — Session history, scoring, and earned badges
- **Settings** — Preferences and profile management

## Tech Stack

- **Kotlin** with **Jetpack Compose** (Material 3)
- **Room** for local persistence (sessions, profiles, progress)
- **DataStore** for preferences
- **Navigation Compose** for in-app navigation
- **NDK** — Optional C++/OpenGL components (e.g. renderer)

## Requirements

- Android SDK 26+ (target 35)
- JDK 11
- Android Studio or Gradle 8.x+

## Build & Run

```bash
# Build debug APK
./gradlew assembleDebug

# Run on connected device or emulator
./gradlew installDebug
```

On Windows:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

Open the project in Android Studio and use **Run** (or Shift+F10) to build and launch on a device or emulator.

## Project Structure

```
mathifier/
├── app/
│   ├── src/main/
│   │   ├── java/.../mathifier/
│   │   │   ├── data/          # Repositories, Room, DataStore
│   │   │   ├── domain/        # Models, ProblemGenerator, Scoring, Badges
│   │   │   └── ui/            # Compose screens, navigation, theme
│   │   └── cpp/               # Native (OpenGL/renderer)
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

## License

See repository or project for license terms.

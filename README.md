# Titanium

[![Build](https://github.com/BazZziliuS/Titanium/actions/workflows/build.yml/badge.svg)](https://github.com/BazZziliuS/Titanium/actions/workflows/build.yml)
[![License](https://img.shields.io/github/license/BazZziliuS/Titanium)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://adoptium.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21-green)](https://minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.40+-blue)](https://neoforged.net/)
[![GitHub Stars](https://img.shields.io/github/stars/BazZziliuS/Titanium?style=flat)](https://github.com/BazZziliuS/Titanium/stargazers)
[![GitHub Issues](https://img.shields.io/github/issues/BazZziliuS/Titanium)](https://github.com/BazZziliuS/Titanium/issues)

Titanium is a library mod for Minecraft that provides a powerful framework for creating machine-based mods with NeoForge.

## Features

- **Component System** - Modular components for building complex machines:
  - Inventory management (sided, multi-slot)
  - Fluid tanks (sided, multi-tank)
  - Energy storage
  - Progress bars
  - Buttons and UI controls
  - Redstone control

- **GUI Framework** - Easy-to-use screen addon system for creating machine interfaces

- **Block Network System** - Network graph implementation for connected block systems

- **NBT Handling** - Automatic serialization with `@Save` annotation

- **Recipe System** - Generic serializer for custom recipe types

- **Config System** - Annotation-based configuration with `@ConfigFile` and `@ConfigVal`

- **Sided Component Manager** - Handle input/output configuration per block face

## Requirements

- Minecraft 1.21
- NeoForge 21.1.40+
- Java 21

## Building

```bash
# Clone the repository
git clone https://github.com/BazZziliuS/Titanium.git
cd Titanium

# Build the project
./gradlew build
```

The compiled JAR will be in `build/libs/`.

## Usage

### Option 1: JitPack

Add JitPack repository and dependency in your `build.gradle`:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.BazZziliuS:Titanium:${titanium_version}'
}
```

### Option 2: Local Build

1. Clone and build Titanium:
```bash
git clone https://github.com/BazZziliuS/Titanium.git
cd Titanium
./gradlew build publishToMavenLocal
```

2. Add to your project's `build.gradle`:
```groovy
repositories {
    mavenLocal()
}

dependencies {
    implementation "com.hrznstudio:titanium:${titanium_version}"
}
```

## License

See [LICENSE](LICENSE) for details.

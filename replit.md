# CasinoPlugin

## Overview
CasinoPlugin is a Minecraft server plugin (Paper/Spigot) that adds casino gaming features to Minecraft servers. The plugin includes support for Blackjack and Baccarat games with AI opponents and side bets.

**Current Status**: Base plugin structure implemented with placeholder classes for game logic.

## Project Details
- **Version**: 1.0.0
- **Minecraft API**: Paper 1.20.4
- **Java Version**: 19 (GraalVM)
- **Build Tool**: Apache Maven
- **Dependencies**: Paper API, VaultAPI

## Project Structure
```
src/main/java/com/aidean/casinoplugin/
├── CasinoPlugin.java    # Main plugin class
├── Blackjack.java       # Blackjack game logic (placeholder)
├── Baccarat.java        # Baccarat game logic (placeholder)
└── GUIManager.java      # GUI management (placeholder)

src/main/resources/
└── plugin.yml           # Plugin configuration
```

## Building the Plugin
The plugin JAR can be built using Maven:
```bash
mvn clean package
```

The compiled JAR will be located at `target/CasinoPlugin-1.0.0.jar`

## Installation on Minecraft Server
1. Build the plugin JAR file
2. Copy `target/CasinoPlugin-1.0.0.jar` to your Minecraft server's `plugins` folder
3. Ensure VaultAPI plugin is installed (dependency)
4. Restart your Minecraft server
5. The plugin will load and display "CasinoPlugin Enabled!" in the console

## Development Notes
- The plugin requires VaultAPI for economy integration
- Game logic classes (Blackjack, Baccarat, GUIManager) are currently placeholders
- Designed for Paper/Spigot 1.20.4 servers
- Compatible with Java 19+

## Recent Changes
- 2025-10-31: Initial Replit environment setup
- Added plugin.yml configuration file
- Configured build system for Java 19 compatibility
- Updated Paper API from 1.21.4 to 1.20.4 for Java version compatibility

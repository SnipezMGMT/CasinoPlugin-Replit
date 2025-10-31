# CasinoPlugin

## Overview
CasinoPlugin is a fully-featured Minecraft server plugin (Paper/Spigot) that adds casino gaming to Minecraft servers. The plugin includes complete implementations of Blackjack and Baccarat games with interactive GUIs, Vault economy integration, and realistic game mechanics.

**Current Status**: Fully implemented and functional with working commands, game logic, and economy integration.

## Project Details
- **Version**: 1.0.0
- **Minecraft API**: Paper 1.20.4
- **Java Version**: 19 (GraalVM)
- **Build Tool**: Apache Maven
- **Dependencies**: Paper API, VaultAPI

## Project Structure
```
src/main/java/com/aidean/casinoplugin/
├── CasinoPlugin.java       # Main plugin class with Vault integration
├── Card.java               # Card representation (suits, ranks, values)
├── Deck.java               # Deck management with shuffle and draw
├── Blackjack.java          # Complete Blackjack game logic
├── BlackjackCommand.java   # Blackjack command handler
├── Baccarat.java           # Complete Baccarat game logic with third card rules
├── BaccaratCommand.java    # Baccarat command handler
└── GUIManager.java         # Interactive GUI system for games

src/main/resources/
└── plugin.yml              # Plugin configuration with commands
```

## Features

### Blackjack
- Interactive GUI with Hit/Stand buttons
- Realistic card dealing and hand calculations
- Automatic Ace value adjustment (11 or 1)
- Dealer AI that follows standard casino rules (stands on 17+)
- Blackjack pays 2.5:1, regular wins pay 2:1
- Push (tie) returns the bet

**Commands:**
- `/blackjack play <bet>` - Start a game with your bet amount
- `/blackjack rules` - View game rules
- Alias: `/bj`

### Baccarat
- Bet on Player, Banker, or Tie
- Complete third-card drawing rules implementation
- Realistic payouts: Player 1:1, Banker 0.95:1, Tie 8:1
- Automated game flow with timed reveals
- Proper baccarat scoring (modulo 10)

**Commands:**
- `/baccarat play <player|banker|tie> <bet>` - Start a game
- `/baccarat rules` - View game rules
- Alias: `/bc`

### Economy Integration
- Full Vault economy support
- Balance checking before bets
- Automatic win/loss processing
- Detailed profit/loss reporting

## Building the Plugin
The plugin JAR can be built using Maven:
```bash
mvn clean package
```

The compiled JAR will be located at `target/CasinoPlugin-1.0.0.jar`

## Installation on Minecraft Server
1. Install Vault plugin on your server
2. Install an economy plugin (e.g., EssentialsX)
3. Build the plugin JAR file
4. Copy `target/CasinoPlugin-1.0.0.jar` to your Minecraft server's `plugins` folder
5. Restart your Minecraft server
6. The plugin will load and connect to Vault economy

## Usage Examples
```
/blackjack play 100      # Bet $100 on blackjack
/bj rules                # View blackjack rules

/baccarat play banker 50 # Bet $50 on banker
/bc play tie 25          # Bet $25 on tie
```

## Development Notes
- Requires Vault and an economy plugin
- Uses Paper API 1.20.4 features
- GUI uses inventory click events
- Game state managed per-player
- Compatible with Java 19+

## Recent Changes
- 2025-10-31: Complete plugin implementation
  - Implemented full Blackjack game with dealer AI
  - Implemented full Baccarat game with third-card rules
  - Added interactive GUI system for player actions
  - Integrated Vault economy for betting system
  - Added command handlers for both games
  - Registered commands in plugin.yml
  - Created Card and Deck system for realistic gameplay
- 2025-10-31: Initial Replit environment setup
  - Added plugin.yml configuration file
  - Configured build system for Java 19 compatibility
  - Updated Paper API from 1.21.4 to 1.20.4 for Java version compatibility

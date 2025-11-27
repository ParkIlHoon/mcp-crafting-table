# MCP Crafting Table

A Model Context Protocol (MCP) server built with Kotlin that provides Minecraft crafting recipes.

## Overview

This MCP server exposes Minecraft crafting recipes as MCP tools, allowing AI assistants to access and provide crafting information for various items.

## Features

- Provides crafting recipes in JSON format
- Two MCP tools: `get_recipe` and `list_recipes`
- Built with the official Kotlin MCP SDK (v0.8.0)
- STDIO transport for seamless integration with MCP clients

## Requirements

- JDK 11 or higher
- Gradle 8.x

## Building

```bash
./gradlew build
```

## Running

```bash
./gradlew run
```

The server will start and communicate via STDIO, making it compatible with Claude Desktop and other MCP clients.

## Available Tools

### `get_recipe`
Get a Minecraft crafting recipe for a specific item.

**Parameters:**
- `item` (string, required): The name of the item (e.g., 'wooden_pickaxe', 'crafting_table', 'stick')

**Example:**
```json
{
  "item": "wooden_pickaxe"
}
```

### `list_recipes`
List all available Minecraft crafting recipes.

**Parameters:** None

## Supported Recipes

Currently supported crafting recipes:
- `wooden_pickaxe` - Wooden Pickaxe
- `crafting_table` - Crafting Table
- `stick` - Stick
- `wooden_sword` - Wooden Sword
- `chest` - Chest

## Project Structure

```
mcp-crafting-table/
├── src/main/kotlin/com/mcp/craftingtable/
│   └── Main.kt              # Main server implementation
├── build.gradle.kts         # Gradle build configuration
├── settings.gradle.kts      # Gradle settings
└── README.md               # This file
```

## Adding New Recipes

To add new crafting recipes, add a new case to the `getRecipe` function in `Main.kt`:

```kotlin
"your_item" -> """
    {
      "item": "your_item",
      "displayName": "Your Item",
      "pattern": [
        "...",
        "...",
        "..."
      ],
      "key": {
        "X": "material"
      },
      "result": {
        "count": 1
      }
    }
""".trimIndent()
```

Don't forget to update the `list_recipes` tool output to include your new recipe!

## License

MIT

package com.mcp.craftingtable

import io.ktor.utils.io.streams.asInput
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import io.modelcontextprotocol.kotlin.sdk.types.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.buffered
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

fun main() {
    val server = Server(
        Implementation(
            name = "mcp-crafting-table",
            version = "1.0.0",
        ),
        ServerOptions(
            capabilities = ServerCapabilities(
                tools = ServerCapabilities.Tools(listChanged = true)
            ),
        ),
    )

    // Register crafting recipe tools
    registerTools(server)

    val transport = StdioServerTransport(
        System.`in`.asInput(),
        System.out.asSink().buffered(),
    )

    runBlocking {
        val session = server.createSession(transport)
        val done = Job()
        session.onClose {
            done.complete()
        }
        done.join()
    }
}

fun registerTools(server: Server) {
    // Tool to get a specific crafting recipe
    server.addTool(
        name = "get_recipe",
        description = "Get a Minecraft crafting recipe for a specific item",
        inputSchema = ToolSchema(
            properties = buildJsonObject {
                putJsonObject("item") {
                    put("type", "string")
                    put("description", "The name of the item to get the recipe for (e.g., 'wooden_pickaxe', 'crafting_table', 'stick')")
                }
            },
            required = listOf("item"),
        ),
    ) { request ->
        val item = request.arguments?.get("item")?.jsonPrimitive?.content
            ?: return@addTool CallToolResult(
                content = listOf(TextContent("The 'item' parameter is required."))
            )

        val recipe = getRecipe(item)
        CallToolResult(content = listOf(TextContent(recipe)))
    }

    // Tool to list all available recipes
    server.addTool(
        name = "list_recipes",
        description = "List all available Minecraft crafting recipes",
        inputSchema = ToolSchema(
            properties = buildJsonObject { },
            required = emptyList(),
        ),
    ) { _ ->
        val recipeList = """
            Available Minecraft Crafting Recipes:

            1. wooden_pickaxe - Wooden Pickaxe
            2. crafting_table - Crafting Table
            3. stick - Stick
            4. wooden_sword - Wooden Sword
            5. chest - Chest

            Use the 'get_recipe' tool with the item name to get detailed crafting instructions.
        """.trimIndent()

        CallToolResult(content = listOf(TextContent(recipeList)))
    }
}

fun getRecipe(item: String): String {
    return when (item.lowercase().replace(" ", "_")) {
        "wooden_pickaxe" -> """
            {
              "item": "wooden_pickaxe",
              "displayName": "Wooden Pickaxe",
              "pattern": [
                "PPP",
                " S ",
                " S "
              ],
              "key": {
                "P": "wooden_planks",
                "S": "stick"
              },
              "result": {
                "count": 1
              }
            }
        """.trimIndent()

        "crafting_table" -> """
            {
              "item": "crafting_table",
              "displayName": "Crafting Table",
              "pattern": [
                "PP",
                "PP"
              ],
              "key": {
                "P": "wooden_planks"
              },
              "result": {
                "count": 1
              }
            }
        """.trimIndent()

        "stick" -> """
            {
              "item": "stick",
              "displayName": "Stick",
              "pattern": [
                "P",
                "P"
              ],
              "key": {
                "P": "wooden_planks"
              },
              "result": {
                "count": 4
              }
            }
        """.trimIndent()

        "wooden_sword" -> """
            {
              "item": "wooden_sword",
              "displayName": "Wooden Sword",
              "pattern": [
                "P",
                "P",
                "S"
              ],
              "key": {
                "P": "wooden_planks",
                "S": "stick"
              },
              "result": {
                "count": 1
              }
            }
        """.trimIndent()

        "chest" -> """
            {
              "item": "chest",
              "displayName": "Chest",
              "pattern": [
                "PPP",
                "P P",
                "PPP"
              ],
              "key": {
                "P": "wooden_planks"
              },
              "result": {
                "count": 1
              }
            }
        """.trimIndent()

        else -> """
            {
              "error": "Recipe not found",
              "message": "No crafting recipe found for '$item'. Use the 'list_recipes' tool to see available recipes."
            }
        """.trimIndent()
    }
}

plugins {
    kotlin("jvm") version "2.2.0"
    application
}

group = "com.mcp"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.modelcontextprotocol:kotlin-sdk:0.8.0")
    implementation("io.ktor:ktor-client-cio:3.0.3")
    implementation("io.ktor:ktor-server-netty:3.0.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("com.mcp.craftingtable.MainKt")
}

import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.PluginDependenciesSpecScope
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version

fun PluginDependenciesSpecScope.applyJVMPlugin() {
    kotlin("jvm") version jvmVersion
}

fun PluginDependenciesSpecScope.applyKtorPlugin() {
    id("io.ktor.plugin") version ktorPluginVersion
}

fun PluginDependenciesSpecScope.applyKotlinSerializationPlugin() {
    id("org.jetbrains.kotlin.plugin.serialization") version serializationPluginVersion
}

fun PluginDependenciesSpecScope.applyJIBPlugin() {
    id("com.google.cloud.tools.jib") version jibToolsPlugin
}

fun PluginDependenciesSpecScope.applySQLDelightlugin() {
    id("app.cash.sqldelight") version sqlDelightPlugin
}
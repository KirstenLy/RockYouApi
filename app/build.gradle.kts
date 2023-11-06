plugins {
    applyJVMPlugin()
    applyKtorPlugin()
    applyKotlinSerializationPlugin()
    applyJIBPlugin()
    idea
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

// TODO: Это нужно отключать в проде
tasks.named("compileTestKotlin", org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class.java) {
    compilerOptions {
        freeCompilerArgs.add("-Xdebug")
    }
}

application {
    mainClass.set("rockyouapi.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    docker {
        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { "kirstenly/webserver" },
                username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
            )
        )

        jib {
            from {
                image = "openjdk:17-jdk-alpine"
            }
            to {
                image = "kirstenly/webserver"
            }

            localImageName.set("kirstenly/webserver")
            imageTag.set("0.0.6")
        }
    }
}

repositories {
    google()
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    systemProperties["junit.jupiter.execution.parallel.enabled"] = true
    systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}

dependencies {
    implementation(project(":common"))
    implementation(project(":database"))

    implementation(Dependencies.Log.Logback.classic)

    implementation(Dependencies.Ktor.serverCoreJVM)
    implementation(Dependencies.Ktor.serverNettyJVM)
    implementation(Dependencies.Ktor.serverAutoHeadResponce)
    implementation(Dependencies.Ktor.serverContentNegotiationJVM)
    implementation(Dependencies.Ktor.serverResources)
    implementation(Dependencies.Ktor.serverDoubleReceive)
    implementation(Dependencies.Ktor.serializationJSON)
    implementation(Dependencies.Ktor.serverAuth)
    implementation(Dependencies.Ktor.serverAuthJWT)
    implementation(Dependencies.Ktor.serverSession)

    testImplementation(platform(Dependencies.Testing.jUnitBOM))
    testImplementation(Dependencies.Ktor.serverTestJVM)
    testImplementation(Dependencies.Testing.jUnitJupiter)
    testImplementation(Dependencies.Testing.coroutines)
    testImplementation(Dependencies.Testing.jUnitJupiterMockito)
}
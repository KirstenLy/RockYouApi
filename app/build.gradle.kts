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
            imageTag.set("1.0")
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":database"))
    implementation(project(":declaration"))
    implementation(project(":test"))

    implementation(Dependencies.Log.Logback.classic)

    implementation(Dependencies.Ktor.serverCoreJVM)
    implementation(Dependencies.Ktor.serverNettyJVM)
    implementation(Dependencies.Ktor.serverAutoHeadResponce)
    implementation(Dependencies.Ktor.serverContentNegotiationJVM)
    implementation(Dependencies.Ktor.serverResources)
    implementation(Dependencies.Ktor.serializationJSON)
    implementation(Dependencies.Ktor.serverAuth)
    implementation(Dependencies.Ktor.serverSession)

    testImplementation(Dependencies.Ktor.serverTestJVM)
    testImplementation(Dependencies.Kotlin.testJUnit)

    implementation(Dependencies.Database.MySQL.connectorJava)
}
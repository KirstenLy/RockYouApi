object Dependencies {

    object Kotlin {
        const val testJUnit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
    }

    object Ktor {
        const val serverCoreJVM = "io.ktor:ktor-server-core-jvm:$ktorVersion"
        const val serverNettyJVM = "io.ktor:ktor-server-netty-jvm:$ktorVersion"
        const val serverResources = "io.ktor:ktor-server-resources:$ktorVersion"
        const val serverAutoHeadResponce = "io.ktor:ktor-server-auto-head-response:$ktorVersion"
        const val serverContentNegotiationJVM = "io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion"
        const val serverTestJVM = "io.ktor:ktor-server-tests-jvm:$ktorVersion"
        const val serverAuth = "io.ktor:ktor-server-auth:$ktorVersion"
        const val serverSession = "io.ktor:ktor-server-sessions:$ktorVersion"
        const val serializationJSON = "io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion"
    }

    object Database {

        object Exposed {
            const val core = "org.jetbrains.exposed:exposed-core:$exposedVersion"
            const val dao = "org.jetbrains.exposed:exposed-dao:$exposedVersion"
            const val jdbc = "org.jetbrains.exposed:exposed-jdbc:$exposedVersion"
        }

        object MySQL {
            const val connectorJava = "mysql:mysql-connector-java:$mysqlConnectorJavaVersion"
        }

        object H2 {
            const val database = "com.h2database:h2:$h2DatabaseVersion"
        }
    }

    object Log {

        object Logback {
            const val classic = "ch.qos.logback:logback-classic:$logbackVersion"
        }
    }
}
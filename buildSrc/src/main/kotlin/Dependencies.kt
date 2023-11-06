object Dependencies {

    object Kotlin {
        const val testJUnit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
        const val serializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinJSONSerializationVersion"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion"
    }

    object Ktor {
        const val serverCoreJVM = "io.ktor:ktor-server-core-jvm:$ktorVersion"
        const val serverNettyJVM = "io.ktor:ktor-server-netty-jvm:$ktorVersion"
        const val serverResources = "io.ktor:ktor-server-resources:$ktorVersion"
        const val serverAutoHeadResponce = "io.ktor:ktor-server-auto-head-response:$ktorVersion"
        const val serverContentNegotiationJVM = "io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion"
        const val serverDoubleReceive = "io.ktor:ktor-server-double-receive:$ktorVersion"
        const val serverTestJVM = "io.ktor:ktor-server-tests-jvm:$ktorVersion"
        const val serverAuth = "io.ktor:ktor-server-auth:$ktorVersion"
        const val serverAuthJWT = "io.ktor:ktor-server-auth-jwt:$ktorVersion"
        const val serverSession = "io.ktor:ktor-server-sessions:$ktorVersion"
        const val serializationJSON = "io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion"
    }

    object Database {

        object SQLDelight {
            const val jdbcDriver = "app.cash.sqldelight:jdbc-driver:$sqlDelightVersion"
            const val sqliteDriver = "app.cash.sqldelight:sqlite-driver:$sqlDelightVersion"
            const val coroutinesExtenstions = "app.cash.sqldelight:coroutines-extensions:$sqlDelightVersion"
        }

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

    object Security {
        const val bCrypt = "at.favre.lib:bcrypt:$bCryptVersion"
    }

    object Log {

        object Logback {
            const val classic = "ch.qos.logback:logback-classic:$logbackVersion"
        }
    }

    object Testing {
        const val jUnitBOM = "org.junit:junit-bom:$jUnitBOMVersion"
        const val jUnitJupiter = "org.junit.jupiter:junit-jupiter"
        const val jUnitJupiterMockito = "org.mockito:mockito-junit-jupiter:$jUnitMockitoVersion"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion"
    }
}
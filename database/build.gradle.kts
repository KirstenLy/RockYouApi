plugins {
    applyJVMPlugin()
    applyKotlinSerializationPlugin()
    applySQLDelightlugin()
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
    applyParallelTest()
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("rockyouapi")
            dialect("app.cash.sqldelight:mysql-dialect:2.0.0")
            deriveSchemaFromMigrations = true
        }
    }
}

dependencies {
    implementation(project(":common"))

    implementation(Dependencies.Kotlin.serializationJson)

    implementation(Dependencies.Security.bCrypt)

    implementation(Dependencies.Database.MySQL.connectorJava)
    implementation(Dependencies.Database.SQLDelight.jdbcDriver)
    implementation(Dependencies.Database.SQLDelight.coroutinesExtenstions)

    testImplementation(platform(Dependencies.Testing.jUnitBOM))
    testImplementation(Dependencies.Testing.jUnitJupiter)
    testImplementation(Dependencies.Testing.coroutines)
}
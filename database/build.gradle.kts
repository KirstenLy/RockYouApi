plugins {
    applyJVMPlugin()
    id("app.cash.sqldelight") version "2.0.0"
}

repositories {
    google()
    mavenCentral()
}

sqldelight {
    databases {
        create("DBTest") {
            packageName.set("rockyouapi")
            dialect("app.cash.sqldelight:mysql-dialect:2.0.0")
        }
    }
}

dependencies {
    implementation(project(":declaration"))
    implementation(project(":common"))

    implementation(Dependencies.Database.Exposed.core)
    implementation(Dependencies.Database.Exposed.dao)
    implementation(Dependencies.Database.Exposed.jdbc)

    implementation(Dependencies.Database.H2.database)

    implementation(Dependencies.Database.MySQL.connectorJava)

    implementation("app.cash.sqldelight:jdbc-driver:2.0.0")
    implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")
    implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
    testImplementation("app.cash.sqldelight:sqlite-driver:2.0.0")
}
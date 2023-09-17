plugins {
    applyJVMPlugin()
    applyKotlinSerializationPlugin()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Dependencies.Ktor.serializationJSON)
}
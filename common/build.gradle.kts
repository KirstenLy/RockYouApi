plugins {
    applyJVMPlugin()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Dependencies.Kotlin.coroutines)
    testImplementation(platform(Dependencies.Testing.jUnitBOM))
}
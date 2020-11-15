plugins {
    java
    kotlin("jvm") version "1.4.10"
}

group = "pl.poznan.put.edu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.openpnp:opencv:4.3.0-2")
}

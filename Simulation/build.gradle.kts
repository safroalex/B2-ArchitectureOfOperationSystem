plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.9"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("org.safroalex.Main")
}

group = "org.safroalex"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(platform("org.junit:junit-bom:5.9.1"))
    implementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

plugins {
    id("java")
    id("application")
}

application {
    mainClass.set("org.safroalex.Main")
}

group = "org.safroalex"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")

}

tasks.test {
    useJUnitPlatform()
}

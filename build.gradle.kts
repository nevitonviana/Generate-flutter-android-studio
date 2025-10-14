plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.nevitonviana.generateflutter"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
//    version = "2023.3.5"
//  version.set("2023.3.5")
    version.set("2024.1")

//  type.set("AI") // Target IDE Platform
    type.set("IC") // Target IDE Platform
//  plugins.set(listOf("Dart",))
    plugins.set(listOf("Dart:241.17502", "com.intellij.java"))
//    plugins.set(listOf("Dart:241.17502", "com.intellij.java"))

//  plugins.set(listOf("Dart", "Flutter"))
//  plugins.set(listOf(
//    "org.jetbrains.plugins.dart:233.14475.28",
//    "io.flutter:233.14475.18"
//  ))
}

//sourceSets {
//    main {
//        java.srcDirs("src/main/java", "src/main/kotlin")
//    }
//}


tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("243.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

}

plugins {
  kotlin("jvm") version "2.2.0"
}

group = "mod.blurple"
version = "1.0.0"

repositories {
  maven(url = "https://repo.papermc.io/repository/maven-public/") {
    name = "papermc"
  }
}

dependencies {
  implementation(project(":common"))
  compileOnly("io.papermc.paper:paper-api:26.3.build.+")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.test {
  useJUnitPlatform()
}

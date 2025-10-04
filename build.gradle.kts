plugins {
	java
	id("org.springframework.boot") version "4.0.0-M2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "pl.oczadly.kotlin-app"
version = "0.0.1-SNAPSHOT"
description = "Hackyeah 2025 setup"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("software.amazon.awssdk:dynamodb:2.34.0")
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.34.0")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

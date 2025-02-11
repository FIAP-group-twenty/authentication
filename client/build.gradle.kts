plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.sonarqube") version "6.0.1.5171"
	id("jacoco")
}

group = "com.authentication"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
	mavenCentral()
	gradlePluginPortal()
}

jacoco {
	toolVersion = "0.8.11"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.4.2")


}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "group-twenty_authentication")
        property("sonar.organization", "group-twenty")
        property("sonar.host.url", project.findProperty("SONAR_HOST_URL") ?: "")
        property("sonar.login", project.findProperty("SONAR_TOKEN") ?: "")
        property("sonar.kotlin.language.level", "1.9")
        property("sonar.sources", "src/main/kotlin")
        property("sonar.tests", "src/test/kotlin")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
	useJUnitPlatform()

	finalizedBy(tasks.jacocoTestReport)
	testLogging {
		events("passed", "failed", "skipped")
	}
	reports {
		junitXml.required.set(true)
		junitXml.outputLocation.set(file("${project.projectDir}/test-results/test"))
		html.required.set(true)
		html.outputLocation.set(file("${project.projectDir}/test-results/test"))
	}
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
		html.required.set(true)
		html.outputLocation.set(file("${project.projectDir}/reports/jacoco"))
	}
}

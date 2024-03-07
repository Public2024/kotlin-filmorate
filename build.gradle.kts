import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.2"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.jetbrains.dokka") version "1.6.21"
	id("io.freefair.lombok") version "8.1.0"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
//	kotlin("plugin.lombok") version "1.9.22"
}

group = "ru.devmark"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.9")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.9")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.2.2")
	// https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.reflections:reflections:0.10.2")



}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.dokkaHtml.configure{
	outputDirectory.set(file("src/Documentation"))
	moduleName.set("kotlin-filmorate application")

	dokkaSourceSets.configureEach{
		includeNonPublic.set(true)
		includes.from("module.md")
	}

}

tasks.withType<Test> {
	useJUnitPlatform()
}




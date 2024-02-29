import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// todo добавить dto и конвертеры
// todo дополнительные данные для пользователя?
// todo таска градла сборки и отправки на гелиос?

plugins {
    id("org.springframework.boot") version "2.5.7"
    id("io.spring.dependency-management") version "1.1.0"
    id("war")
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    kotlin("plugin.jpa") version "1.8.0"
}

group = "com.itmo.blps.bankiru"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mustache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core:5.2.0")
    implementation("io.springfox:springfox-swagger-ui:2.8.0")
    implementation("io.springfox:springfox-swagger2:2.8.0")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("javax.servlet:javax.servlet-api")
    implementation("javax.transaction:jta:1.1")
    implementation("org.jboss.narayana.jta:narayana-jta:5.12.0.Final")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")


    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}

configurations["implementation"].exclude("org.springframework.boot", "spring-boot-starter-tomcat")

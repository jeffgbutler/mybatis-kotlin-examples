import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}

group = "com.github.jeffgbutler"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation ("org.mybatis:mybatis:3.5.5")
    implementation ("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.2.1")
    testImplementation ("org.assertj:assertj-core:3.17.2")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation ("org.hsqldb:hsqldb:2.5.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

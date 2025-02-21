import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.0"
}

group = "com.github.jeffgbutler"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

sourceSets {
    main {
        resources {
            srcDir("src/main/kotlin")
            include("**/*.xml")
        }
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation ("org.mybatis:mybatis:3.5.19")
    implementation ("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.5.2")
    testImplementation ("org.assertj:assertj-core:3.27.2")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    testImplementation ("org.hsqldb:hsqldb:2.7.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
}

group = "com.github.jeffgbutler"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

sourceSets {
    main {
        java {
            srcDirs("src/main/kotlin")
        }
        resources {
            srcDirs("src/main/kotlin")
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
    implementation ("org.mybatis:mybatis:3.5.10")
    implementation ("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.4.0")
    testImplementation ("org.assertj:assertj-core:3.23.1")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation ("org.hsqldb:hsqldb:2.7.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xcontext-receivers")
        jvmTarget = "11"
    }
}

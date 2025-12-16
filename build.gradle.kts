import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.0"
}

group = "com.github.jeffgbutler"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

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
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

dependencies {
    implementation ("org.mybatis:mybatis:3.6.0-SNAPSHOT")
    implementation ("org.mybatis.dynamic-sql:mybatis-dynamic-sql:2.0.0-SNAPSHOT")
    testImplementation ("org.assertj:assertj-core:3.27.6")
    testImplementation ("org.hsqldb:hsqldb:2.7.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.2.21")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xcontext-parameters")
    }
}

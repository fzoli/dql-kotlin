/*
 * Copyright 2022 Zoltan Farkas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    kotlin("plugin.spring") version "1.7.20"
}

val hikaricp_version: String by project
val testcontainers_bom_version: String by project

dependencies {
    implementation(project(":sample-rest-server"))
    implementation(project(":sample-repository-pgsql"))
    implementation(project(":dql-query-parser"))
    implementation(project(":sample-dql-query-parser-extension"))
    implementation("org.springframework:spring-tx")
    implementation("org.springframework:spring-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.postgresql:postgresql")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql")
    testImplementation(project(":sample-service-client"))
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:$testcontainers_bom_version")
    }
}

tasks.getByName<BootJar>("bootJar") {
    manifest {
        attributes(
            "Implementation-Version" to project.version
        )
    }
}

tasks.withType<Test> {
    if (!project.hasProperty("sample-test") || "false" != project.property("sample-test")) {
        useJUnitPlatform()
        dependsOn(":sample-docker-pgsql:docker")
    }
}

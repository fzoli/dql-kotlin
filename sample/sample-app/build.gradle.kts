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
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":dql-query-parser"))
    implementation(project(":sample-dql-query-parser-extension"))
    implementation(project(":sample-repository-pgsql"))
    implementation(project(":sample-rest-server"))
    implementation(libs.flyway.core)
    implementation(libs.hikaricp)
    implementation(libs.jdbc.postgresql)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.jdbc)
    implementation(libs.spring.tx)
    testImplementation(project(":sample-service-client"))
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.testcontainers.postgresql)
}

repositories {
    mavenCentral()
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

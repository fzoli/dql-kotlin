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

val kotlin_version: String by project
val junit_jupiter_version: String by project
val slf4j_version: String by project

plugins {
    `java-library`
}

dependencies {
    api(project(":query-api"))
    api(project(":dql-model"))
    api(project(":dql-resolver"))
    implementation(project(":data-type"))
    implementation(kotlin("stdlib-jdk8", kotlin_version))
    implementation(kotlin("reflect", kotlin_version))
    testImplementation(project(":query-querydsl"))
    testImplementation(project(":query-kt"))
    testImplementation("org.slf4j:slf4j-nop:$slf4j_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_jupiter_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_jupiter_version")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

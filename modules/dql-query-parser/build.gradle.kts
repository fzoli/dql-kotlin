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

plugins {
    `java-library`
}

dependencies {
    api(project(":query-api"))
    api(project(":dql-model"))
    api(project(":dql-resolver"))
    implementation(project(":data-type"))
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.reflect)
    testImplementation(project(":query-querydsl"))
    testImplementation(project(":query-kt"))
    testImplementation(libs.slf4j.nop)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

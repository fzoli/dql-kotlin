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

val springframework_version: String by project
val querydsl_version: String by project

apply(from = "build.dslgen.gradle")
apply(from = "build.flyway.gradle")

dependencies {
    api(project(":sample-repository-api"))
    implementation(project(":query-querydsl"))
    implementation(project(":sample-query-extension-querydsl"))
    implementation("com.querydsl:querydsl-sql:$querydsl_version")
    implementation("org.springframework:spring-tx:$springframework_version")
    implementation("org.springframework:spring-jdbc:$springframework_version")
    implementation("org.springframework:spring-context:$springframework_version")
    implementation("org.springframework:spring-beans:$springframework_version")
}

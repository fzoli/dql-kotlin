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

val okhttp3_version: String by project
val retrofit_version: String by project
val retrofit_kotlinx_serialization_version: String by project
val kotlinx_coroutines_version: String by project
val kotlinx_serialization_version: String by project

dependencies {
    api(project(":sample-service-api"))
    implementation(project(":sample-rest-util"))
    implementation(project(":query-dql"))
    implementation(project(":sample-query-extension-dql"))
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp3_version")
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines_version")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofit_version")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofit_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_version")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:$retrofit_kotlinx_serialization_version")
}

repositories {
    mavenCentral()
}

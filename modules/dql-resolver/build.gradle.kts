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
val antlr4_version: String by project
val slf4j_version: String by project
val commons_text_version: String by project
val junit_jupiter_version: String by project

plugins {
    `java-library`
    antlr
}

dependencies {
    api(project(":dql-model"))
    implementation(project(":data-type"))
    implementation(kotlin("stdlib-jdk8", kotlin_version))
    implementation("org.slf4j:slf4j-api:$slf4j_version")
    implementation("org.apache.commons:commons-text:$commons_text_version")
    implementation("org.antlr:antlr4-runtime:$antlr4_version")
    antlr("org.antlr:antlr4:$antlr4_version")
    testImplementation("org.slf4j:slf4j-nop:$slf4j_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_jupiter_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_jupiter_version")
}

sourceSets {
    main {
        java {
            srcDir(file("build/gen/antlr/src/main/java"))
        }
    }
}

tasks {

    generateGrammarSource {
        maxHeapSize = "64m"
        arguments = arguments + listOf("-package", "com.farcsal.dql.resolver.generated")
        outputDirectory = file("build/gen/antlr/src/main/java/com/farcsal/dql/resolver/generated")
    }

    compileJava {
        dependsOn(generateGrammarSource)
    }

    compileKotlin {
        dependsOn(generateGrammarSource)
    }

    compileTestKotlin {
        dependsOn(generateTestGrammarSource)
    }

    withType<Jar> {
        dependsOn(generateGrammarSource)
    }

    withType<org.jetbrains.dokka.gradle.DokkaTask> {
        dependsOn(generateGrammarSource)
    }

    withType<Test> {
        useJUnitPlatform()
    }

}

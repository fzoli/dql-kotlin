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
    antlr
}

dependencies {
    api(project(":dql-model"))
    implementation(project(":data-type"))
    implementation(libs.antlr4.runtime)
    implementation(libs.commons.text)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.slf4j.api)
    antlr(libs.antlr4)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.slf4j.nop)
    testRuntimeOnly(libs.junit.jupiter.engine)
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

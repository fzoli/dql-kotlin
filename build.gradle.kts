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

buildscript {

    val kotlin_version: String by project

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = kotlin_version))
    }

}

plugins {
    id("org.ajoberstar.grgit").version("5.0.0")
}

val git = org.ajoberstar.grgit.Grgit.open(mapOf("currentDir" to project.rootDir))
ext["revision"] = git.head().id
ext["dirty"] = !git.status().isClean
apply(from = "build.version.gradle")

group = "com.farcsal.dql"
version = ext["projectVersion"] as String

subprojects {

    group = rootProject.group
    version = rootProject.version

    buildscript {

        val kotlin_version: String by project

        repositories {
            google()
            mavenCentral()
        }

        dependencies {
            classpath(kotlin("gradle-plugin", version = kotlin_version))
        }

    }

    apply(plugin = "kotlin")

    repositories {
        google()
        mavenCentral()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11" // LTS; extended support until September 2026
        }
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    if (!name.startsWith("sample")) {
        apply<MavenPublishPlugin>()

        val sourcesJar by tasks.creating(Jar::class) {
            val sourceSets: SourceSetContainer by project
            from(sourceSets["main"].allSource)
            archiveClassifier.set("sources")
        }

        val javadoc by tasks.getting(Javadoc::class) {
            val sourceSets: SourceSetContainer by project
            source = sourceSets["main"].java
        }
        val javadocJar by tasks.creating(Jar::class) {
            from(javadoc)
            archiveClassifier.set("javadoc")
        }

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>(project.name) {
                    from(components["java"])
                    artifact(sourcesJar)
                    artifact(javadocJar)
                }
            }
        }
    }

}

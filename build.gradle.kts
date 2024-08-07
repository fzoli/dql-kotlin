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

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }

}

plugins {
    alias(libs.plugins.grgit)
    alias(libs.plugins.dokka)
}

val signingKey: String? by project
val signingPassphrase: String? by project
val ossrhUsername = System.getenv("OSSRH_USERNAME") ?: ""
val ossrhPassword = System.getenv("OSSRH_PASSWORD") ?: ""

val git = org.ajoberstar.grgit.Grgit.open(mapOf("currentDir" to project.rootDir))
ext["revision"] = git.head().id
ext["dirty"] = !git.status().isClean
apply(from = "build.version.gradle")

group = "com.farcsal.dql"
version = ext["projectVersion"] as String

val snapshot = ext["snapshot"] as Boolean

subprojects {

    group = rootProject.group
    version = rootProject.version

    buildscript {

        repositories {
            mavenCentral()
        }

    }

    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    if (!name.startsWith("sample")) {
        apply<MavenPublishPlugin>()
        apply<SigningPlugin>()
        apply<org.jetbrains.dokka.gradle.DokkaPlugin>()

        val sourcesJar by tasks.creating(Jar::class) {
            val sourceSets: SourceSetContainer by project
            from(sourceSets["main"].allSource)
            archiveClassifier.set("sources")
        }

        val dokkaJavadoc by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
            dokkaSourceSets {
                named("main") {
                    noAndroidSdkLink.set(false)
                }
            }
        }
        val dokkaJavadocJar by tasks.creating(Jar::class) {
            from(dokkaJavadoc)
            archiveClassifier.set("javadoc")
        }

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>(project.name) {
                    from(components["java"])
                    artifact(sourcesJar)
                    artifact(dokkaJavadocJar)
                    pom {
                        name.set("DQL ${project.name}")
                        description.set("DQL library for Kotlin. Module: ${project.name}")
                        url.set("https://github.com/fzoli/dql-kotlin")
                        licenses {
                            license {
                                name.set("Apache-2.0")
                                url.set("https://opensource.org/licenses/Apache-2.0")
                            }
                        }
                        developers {
                            developer {
                                id.set("progfarkas")
                                name.set("Zoltan Farkas")
                                email.set("progfarkas@gmail.com")
                            }
                        }
                        scm {
                            connection.set("scm:git:https://github.com/fzoli/dql-kotlin")
                            developerConnection.set("scm:git:https://github.com/fzoli/")
                            url.set("https://github.com/fzoli/dql-kotlin")
                        }
                    }
                }
            }

            repositories {
                if (snapshot) {
                    maven {
                        name = "mavenCentral"
                        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                        credentials {
                            username = ossrhUsername
                            password = ossrhPassword
                        }
                    }
                } else {
                    maven {
                        name = "mavenCentral"
                        url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                        credentials {
                            username = ossrhUsername
                            password = ossrhPassword
                        }
                    }
                }
            }
        }

        configure<SigningExtension> {
            if (signingKey != null && signingPassphrase != null) {
                useInMemoryPgpKeys(signingKey, signingPassphrase)
            } else {
                useGpgCmd()
            }
            sign(extensions.getByType<PublishingExtension>().publications)
        }
    }

}

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

rootProject.name = "dql-kotlin"

module("data-type")
module("dql-model")
module("dql-resolver")
module("dql-string-builder")
module("dql-query-parser")
module("query-api")
module("query-dql")
module("query-querydsl")
module("query-es")
module("query-kt")

sample("sample-app")
sample("sample-rest-util")
sample("sample-rest-server")
sample("sample-service-api")
sample("sample-service-client")
sample("sample-service-server")
sample("sample-repository-api")
sample("sample-repository-pgsql")
sample("sample-docker-pgsql")
sample("sample-query-extension-api")
sample("sample-query-extension-dql")
sample("sample-query-extension-querydsl")
sample("sample-dql-query-parser-extension")

fun module(name: String) {
    include(name)
    project(":$name").projectDir = file("modules/$name")
}

fun sample(name: String) {
    include(name)
    project(":$name").projectDir = file("sample/$name")
}

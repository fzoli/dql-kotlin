# DQL

[<img src="https://img.shields.io/maven-central/v/com.farcsal.dql/data-type.svg?label=latest%20release"/>](https://central.sonatype.dev/publisher/com.farcsal.dql)
![GitHub](https://img.shields.io/github/license/fzoli/dql-kotlin)
![Test](https://github.com/fzoli/dql-kotlin/workflows/Test/badge.svg)

## Modules

- **data-type:** Internal module. Contains reused data types and utilities.
- **query-api:** Query API interface. Independent module. Can be used in repository layers.
  - `Criteria`: This is the filter condition.
  - `Field`: **Filter** field. Can not be used for ordering. Available methods are based on the data type. See `BooleanField`, `StringField` etc.
  - `OrderField`: **Order** field. Can not be used for filtering. Selecting ascending or descending mode produces `Order`.
  - `FilterFunction`: A function that receives a schema object (that contains `Field` objects) and translates it to a `Criteria`.
  - `OrderFunction`: A function that receives a schema object (that contains `OrderField` objects) and translates it to a `Order` **list**.
- **query-querydsl:** Query API implementation. Adapter for `QueryDSL` so it can be used in the `where` condition of the query builder. Independent of *DQL* modules. (Server side.)
- **query-es:** Query API implementation. Adapter for `Elasticsearch`. Independent of *DQL* modules. (Server side.)
- **query-dql:** Query API implementation. Adapter for `dql-string-builder` so it produces a DQL query `String`. (Client side.)
- **query-kt:** Query API in-memory implementation. Each expression is nullable. Independent of *DQL* modules. (Server side.)
- **dql-model:** Contains the raw, parsed model of the DQL language. See `DqlExpression`. Independent module.
- **dql-resolver:** Resolves the DQL query `String` to `DqlExpression`. Independent of *query* modules.
- **dql-query-parser:** Translates the DQL model `DqlExpression` to the Query API `Criteria`.
- **dql-string-builder:** A DQL query `String` builder. Used by `query-dql`.

## Sample

In the `sample` directory you can find example codes for both **server** and **client**.
The server and the client use the same service interface.
The application server based on **Spring Boot** and the repository layer uses **PostgreSQL** via **QueryDSL** that uses blocking **JDBC**.
This is why the service interface is not `async`.
Note that for UI based clients the service interface should be `async`, but here we use the client in tests.
Async service is not required for this demonstration.
Anyway the client uses **Retrofit** with **suspending functions**.

During SpringBoot integration tests PostgreSQL is running in **Docker**.
**Gradle** builds the test container before executing tests.
No manual action is required. Just install Docker and set the required permission for your user.

The database schema is initiated at startup with **Flyway**.
There is no `JPA` in the sample code, but QueryDSL supports it too.

### Start here:
`sample/sample-app/src/test/kotlin/com/farcsal/sample/service/UserServiceTest.kt`

You can run the server too if you set up a database and configure the server.
Just copy `sample/sample-app/application.sample.yml` to `sample/sample-app/application.yml` and set the config parameters.

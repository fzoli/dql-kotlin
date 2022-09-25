package com.farcsal.sample.repository.postgresql.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QFlywaySchemaHistory is a Querydsl query type for QFlywaySchemaHistory
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QFlywaySchemaHistory extends com.querydsl.sql.RelationalPathBase<QFlywaySchemaHistory> {

    private static final long serialVersionUID = -1382752248;

    public static final QFlywaySchemaHistory flywaySchemaHistory = new QFlywaySchemaHistory("flyway_schema_history");

    public final NumberPath<Integer> checksum = createNumber("checksum", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> executionTime = createNumber("executionTime", Integer.class);

    public final StringPath installedBy = createString("installedBy");

    public final DateTimePath<java.time.LocalDateTime> installedOn = createDateTime("installedOn", java.time.LocalDateTime.class);

    public final NumberPath<Integer> installedRank = createNumber("installedRank", Integer.class);

    public final StringPath script = createString("script");

    public final BooleanPath success = createBoolean("success");

    public final StringPath type = createString("type");

    public final StringPath version = createString("version");

    public QFlywaySchemaHistory(String variable) {
        super(QFlywaySchemaHistory.class, forVariable(variable), "public", "flyway_schema_history");
        addMetadata();
    }

    public QFlywaySchemaHistory(String variable, String schema, String table) {
        super(QFlywaySchemaHistory.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QFlywaySchemaHistory(String variable, String schema) {
        super(QFlywaySchemaHistory.class, forVariable(variable), schema, "flyway_schema_history");
        addMetadata();
    }

    public QFlywaySchemaHistory(Path<? extends QFlywaySchemaHistory> path) {
        super(path.getType(), path.getMetadata(), "public", "flyway_schema_history");
        addMetadata();
    }

    public QFlywaySchemaHistory(PathMetadata metadata) {
        super(QFlywaySchemaHistory.class, metadata, "public", "flyway_schema_history");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(checksum, ColumnMetadata.named("checksum").withIndex(6).ofType(Types.INTEGER).withSize(10));
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(200).notNull());
        addMetadata(executionTime, ColumnMetadata.named("execution_time").withIndex(9).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(installedBy, ColumnMetadata.named("installed_by").withIndex(7).ofType(Types.VARCHAR).withSize(100).notNull());
        addMetadata(installedOn, ColumnMetadata.named("installed_on").withIndex(8).ofType(Types.TIMESTAMP).withSize(29).withDigits(6).notNull());
        addMetadata(installedRank, ColumnMetadata.named("installed_rank").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(script, ColumnMetadata.named("script").withIndex(5).ofType(Types.VARCHAR).withSize(1000).notNull());
        addMetadata(success, ColumnMetadata.named("success").withIndex(10).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(type, ColumnMetadata.named("type").withIndex(4).ofType(Types.VARCHAR).withSize(20).notNull());
        addMetadata(version, ColumnMetadata.named("version").withIndex(2).ofType(Types.VARCHAR).withSize(50));
    }

}


package com.farcsal.sample.repository.postgresql.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QUser is a Querydsl query type for QUser
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QUser extends com.querydsl.sql.RelationalPathBase<QUser> {

    private static final long serialVersionUID = 1319379802;

    public static final QUser user = new QUser("user");

    public final DateTimePath<java.time.LocalDateTime> creationTime = createDateTime("creationTime", java.time.LocalDateTime.class);

    public final StringPath emailAddress = createString("emailAddress");

    public final SimplePath<java.util.UUID> id = createSimple("id", java.util.UUID.class);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath passwordHash = createString("passwordHash");

    public QUser(String variable) {
        super(QUser.class, forVariable(variable), "public", "user");
        addMetadata();
    }

    public QUser(String variable, String schema, String table) {
        super(QUser.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QUser(String variable, String schema) {
        super(QUser.class, forVariable(variable), schema, "user");
        addMetadata();
    }

    public QUser(Path<? extends QUser> path) {
        super(path.getType(), path.getMetadata(), "public", "user");
        addMetadata();
    }

    public QUser(PathMetadata metadata) {
        super(QUser.class, metadata, "public", "user");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(creationTime, ColumnMetadata.named("creation_time").withIndex(2).ofType(Types.TIMESTAMP).withSize(26).withDigits(3).notNull());
        addMetadata(emailAddress, ColumnMetadata.named("email_address").withIndex(5).ofType(Types.DISTINCT).withSize(2147483647).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.OTHER).withSize(2147483647).notNull());
        addMetadata(level, ColumnMetadata.named("level").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(4).ofType(Types.VARCHAR).withSize(2147483647).notNull());
        addMetadata(passwordHash, ColumnMetadata.named("password_hash").withIndex(6).ofType(Types.VARCHAR).withSize(2147483647));
    }

}


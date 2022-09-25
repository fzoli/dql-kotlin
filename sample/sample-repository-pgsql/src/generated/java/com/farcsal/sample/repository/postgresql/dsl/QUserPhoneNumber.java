package com.farcsal.sample.repository.postgresql.dsl;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QUserPhoneNumber is a Querydsl query type for QUserPhoneNumber
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QUserPhoneNumber extends com.querydsl.sql.RelationalPathBase<QUserPhoneNumber> {

    private static final long serialVersionUID = -46497123;

    public static final QUserPhoneNumber userPhoneNumber = new QUserPhoneNumber("user_phone_number");

    public final StringPath type = createString("type");

    public final SimplePath<java.util.UUID> userId = createSimple("userId", java.util.UUID.class);

    public final StringPath value = createString("value");

    public QUserPhoneNumber(String variable) {
        super(QUserPhoneNumber.class, forVariable(variable), "public", "user_phone_number");
        addMetadata();
    }

    public QUserPhoneNumber(String variable, String schema, String table) {
        super(QUserPhoneNumber.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QUserPhoneNumber(String variable, String schema) {
        super(QUserPhoneNumber.class, forVariable(variable), schema, "user_phone_number");
        addMetadata();
    }

    public QUserPhoneNumber(Path<? extends QUserPhoneNumber> path) {
        super(path.getType(), path.getMetadata(), "public", "user_phone_number");
        addMetadata();
    }

    public QUserPhoneNumber(PathMetadata metadata) {
        super(QUserPhoneNumber.class, metadata, "public", "user_phone_number");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(type, ColumnMetadata.named("type").withIndex(3).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(1).ofType(Types.OTHER).withSize(2147483647).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(2).ofType(Types.VARCHAR).withSize(2147483647).notNull());
    }

}


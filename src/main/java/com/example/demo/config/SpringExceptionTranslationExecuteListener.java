package com.example.demo.config;

import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultExecuteListener;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

public class SpringExceptionTranslationExecuteListener
        extends DefaultExecuteListener {

  @Override
  public void exception(ExecuteContext ctx) {
    SQLDialect             dialect    = ctx.configuration().dialect();
    SQLExceptionTranslator translator = getSqlExceptionTranslator(dialect);
    DataAccessException jOOQ = translator.translate("jOOQ", ctx.sql(), ctx.sqlException());
    ctx.exception(jOOQ);
  }

  private SQLExceptionTranslator getSqlExceptionTranslator(SQLDialect dialect) {
    return (dialect != null)
            ? new SQLErrorCodeSQLExceptionTranslator(dialect.name())
            : new SQLStateSQLExceptionTranslator();
  }
}
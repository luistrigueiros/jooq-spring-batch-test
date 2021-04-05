package com.example.demo.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration implements DisposableBean {
    private EmbeddedDatabase database;
    @Bean
    public EmbeddedDatabase embeddedDatabase() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        database = builder.setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
//                .addScript("data.sql")
                .build();
        return database;

    }

    @Bean
    @Primary
    public PlatformTransactionManager dbTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
    public void destroy() throws Exception {
        database.shutdown();
    }
}

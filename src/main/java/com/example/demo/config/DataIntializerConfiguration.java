package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DataIntializerConfiguration {
    @Autowired
    private DataSource dataSource;

    @Autowired
    @Value("/test-data.sql")
    private Resource data;

    @Autowired
    @Value("/schema.sql")
    private Resource schema;

    public void initSchema() throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), schema);
    }


    public void initData() throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), data);
    }
}

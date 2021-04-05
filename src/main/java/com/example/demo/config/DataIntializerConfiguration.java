package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DataIntializerConfiguration {
    @Autowired
    private DataSource dataSource;

    @Autowired
    @Value("/test-data.sql")
    private Resource resource;

    @PostConstruct
    private void initData() throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
    }
}

package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.springframework.jdbc.datasource.init.ScriptUtils.executeSqlScript;

@Slf4j
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

    private static Boolean hasDoneDataInit = false;

    public void initSchema() throws SQLException {
        executeSqlScript(dataSource.getConnection(), schema);
    }


    public void initData() throws SQLException {
        synchronized (this) {
            if (hasDoneDataInit == false) {
                log.info("About to execute script {}", data.getFilename());
                executeSqlScript(dataSource.getConnection(), data);
                hasDoneDataInit = true;
            }else {
                log.info("Data init already done");
            }
        }
    }
}

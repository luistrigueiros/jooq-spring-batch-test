package com.example.demo.config;

import com.example.demo.batch.*;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@Configuration
@Import({ImportCsvConfiguration.class, PersonItemWriter.class})
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataIntializerConfiguration dataIntializerConfiguration;

    @Value("${initData:false}")
    private Boolean initData;

    @Autowired
    private PersonItemWriter personItemWriter;

    @Autowired
    private PersonItemReader personItemReader;

    @Autowired
    private PersonItemProcessor processor;

    private PlatformTransactionManager transactionManager;

    public BatchConfiguration(PlatformTransactionManager transactionManager) throws SQLException {
        this.transactionManager = transactionManager;
    }


    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }


    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(10)
//                .reader(fileItemReader)
                .reader(personItemReader)
                .processor(processor)
                .writer(personItemWriter)
                .build();
    }


    @SneakyThrows
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        if (initData) {
            dataIntializerConfiguration.initData();
        }
    }
}

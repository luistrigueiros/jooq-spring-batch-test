package com.example.demo.config;

import com.example.demo.batch.Person;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ImportCsvConfiguration {
    @Bean
    @Qualifier("fileReader")
    public FlatFileItemReader<Person> fileReader() {
        BeanWrapperFieldSetMapper<Person> mapper = new BeanWrapperFieldSetMapper<>() {{
            setTargetType(Person.class);
        }};
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("AuthorType.csv"))
                .linesToSkip(1)
                .delimited()
                .names("firstName", "lastName")
                .fieldSetMapper(mapper)
                .build();
    }
}

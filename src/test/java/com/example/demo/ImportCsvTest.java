package com.example.demo;

import com.example.demo.batch.Person;
import com.example.demo.batch.PersonItemReader;
import com.example.demo.batch.PersonItemWriter;
import com.example.demo.config.DatabaseConfiguration;
import com.example.demo.config.ImportCsvConfiguration;
import com.example.demo.config.JooqConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
@Import({PersonItemWriter.class, PersonItemReader.class})
@ContextConfiguration(classes = {
        TestPropertiesConfigure.class,
        ImportCsvConfiguration.class,
        DatabaseConfiguration.class,
        JooqConfiguration.class
})
@Sql(scripts = {"/schema.sql"})
@ExtendWith(SpringExtension.class)
public class ImportCsvTest {

    @Autowired
    private FlatFileItemReader<Person> fileItemReader;

    @Autowired
    private PersonItemWriter personItemWriter;

    @Autowired
    private PersonItemReader personItemReader;

    @BeforeEach
    public void init() {
        fileItemReader.open(new ExecutionContext());
    }

    @AfterEach
    public void close() {
        fileItemReader.close();
    }

    @Test
    public void simpleTest() throws Exception {
        Person person;
        List<Person> people = new ArrayList<>();
        do {
            person = fileItemReader.read();
            if (person != null) {
                log.debug("Person = {}", person);
                if(person.getFirstName() == null) {
                   log.warn( "Got null first name " + person.toString());
                }
                if (person.getLastName() == null) {
                    log.warn("Got null last name " + person.toString());
                }
                people.add(person);
            }
        } while (person != null);
        personItemWriter.write(people);
        log.info("Saved people to database");
        int count = 0;
        do {
            person = personItemReader.read();
            count ++;
        }while (person != null);
        assertTrue(count == 34);
    }
}

package com.example.demo;

import com.example.demo.batch.Person;
import com.example.demo.batch.PersonItemWriter;
import com.example.demo.config.DatabaseConfiguration;
import com.example.demo.config.JooqConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@Import(PersonItemWriter.class)
@ContextConfiguration(classes = {
        ImportCsvTestConfiguration.class,
        DatabaseConfiguration.class,
        JooqConfiguration.class
})
@ExtendWith(SpringExtension.class)
@Slf4j
public class ImportCsvTest {

    @Autowired
    private FlatFileItemReader<Person> personItemReader;

    @Autowired
    private PersonItemWriter personItemWriter;

    @BeforeEach
    public void init() {
        personItemReader.open(new ExecutionContext());
    }

    @AfterEach
    public void close() {
        personItemReader.close();
    }

    @Test
    public void simpleTest() throws Exception {
        Person person;
        List<Object> people = new ArrayList<>();
        do {
            person = personItemReader.read();
            if (person != null) {
                log.debug("Person = {}", person);
                if(person.getFirstName() == null) {
                   log.warn( "Got null first name " + person.toString());
                };
                if (person.getLastName() == null) {
                    log.warn("Got null last name " + person.toString());
                }
                people.add(person);
            }
        } while (person != null);
        personItemWriter.writeItems(people);
        log.info("Saved people to database");
    }
}

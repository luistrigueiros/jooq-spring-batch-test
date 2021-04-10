package com.example.demo;

import com.example.demo.batch.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = {FileItemReaderTestConfiguration.class})
@ExtendWith(SpringExtension.class)
public class FileItemReaderTest {
    private static final Logger log = LoggerFactory.getLogger(FileItemReaderTest.class);

    @Autowired
    private FlatFileItemReader<Person> personItemReader;

    @BeforeEach
    public void init() {
        personItemReader.open( new ExecutionContext());
    }

    @AfterEach
    public void close() {
        personItemReader.close();
    }

    @Test
    public void simpleTest() throws Exception {
        Person person;
        do {
            person = personItemReader.read();
            log.debug( "Person = {}", person);
        }while (person != null);
    }
}

package com.example.demo.batch;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

import static ie.luist.sample.public_.Tables.AUTHOR;

@Slf4j
@Component
public class PersonItemWriter implements ItemWriter<Person> {

    private final DSLContext dsl;

    public PersonItemWriter(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void write(List<? extends Person> items) throws Exception {
        for (Person person: items) {
            dsl.insertInto(AUTHOR)
                    .set(AUTHOR.FIRST_NAME, person.getFirstName())
                    .set(AUTHOR.LAST_NAME, person.getLastName())
                    .execute();
        }
    }
}

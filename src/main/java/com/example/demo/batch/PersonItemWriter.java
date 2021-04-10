package com.example.demo.batch;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import javax.batch.api.chunk.AbstractItemWriter;
import java.util.List;

import static ie.luist.sample.public_.Tables.AUTHOR;

@Slf4j
@Component
public class PersonItemWriter extends AbstractItemWriter {

    private final DSLContext dsl;

    public PersonItemWriter(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void writeItems(List<Object> items) throws Exception {
        for (Object obj: items) {
            Person person = (Person) obj;
            dsl.insertInto(AUTHOR)
                    .set(AUTHOR.FIRST_NAME, person.getFirstName())
                    .set(AUTHOR.LAST_NAME, person.getLastName())
                    .execute();
        }
    }
}

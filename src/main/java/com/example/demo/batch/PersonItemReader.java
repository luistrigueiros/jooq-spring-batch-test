package com.example.demo.batch;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static ie.luist.sample.public_.Tables.AUTHOR;

@Component
@Qualifier("databaseReader")
public class PersonItemReader extends AbstractPagingItemReader<Person> {
    private static final Logger log = LoggerFactory.getLogger(PersonItemReader.class);

    private final DSLContext dsl;

    public PersonItemReader(DSLContext dsl) {
        this.dsl = dsl;
    }

    private static Person apply(Record3<Integer, String, String> r) {
        Person p = new Person();
        p.setFirstName(r.get(AUTHOR.FIRST_NAME));
        p.setLastName(r.get(AUTHOR.LAST_NAME));
        return p;
    }

    @Override
    protected void doReadPage() {
        int start = getPage() * getPageSize();
        log.debug("Reading from start=[{}]", start);
        List<Person> personList = dsl.select(AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
                .from(AUTHOR)
                .orderBy(AUTHOR.ID)
                .seek(start)
                .limit(getPageSize())
                .fetch()
                .stream()
                .map(PersonItemReader::apply)
                .collect(Collectors.toList());
        log.debug("Fetch from=[{}], got=[{}] from db", start,personList.size());
        results = new CopyOnWriteArrayList<>(personList);
    }

    @Override
    protected void doJumpToPage(int itemIndex) {

    }
}

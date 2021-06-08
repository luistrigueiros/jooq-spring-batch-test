package com.example.demo.batch;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import static ie.luist.sample.public_.tables.Author.AUTHOR;

@Slf4j
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private final DSLContext dsl;

	public JobCompletionNotificationListener(DSLContext dsl) {
		this.dsl = dsl;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

			dsl.selectFrom(AUTHOR).fetch()
					.forEach(authorRecord -> log.info("Found <" + authorRecord.getFirstName() + " " + authorRecord.getLastName() + "> in the database."));
		}
	}
}

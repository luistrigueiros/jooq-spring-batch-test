package com.example.demo.config;

import com.example.demo.batch.JobCompletionNotificationListener;
import com.example.demo.batch.Person;
import com.example.demo.batch.PersonItemProcessor;
import com.example.demo.batch.PersonItemReader;
import org.jooq.DSLContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration  extends DefaultBatchConfigurer {
	private PlatformTransactionManager transactionManager;

	public BatchConfiguration( PlatformTransactionManager transactionManager, DataIntializerConfiguration dataIntializerConfiguration) {
		this.transactionManager = transactionManager;
	}

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}


//	@Bean
//	public FlatFileItemReader<Person> reader() {
//		return new FlatFileItemReaderBuilder<Person>()
//			.name("personItemReader")
//			.resource(new ClassPathResource("sample-data.csv"))
//			.delimited()
//			.names(new String[]{"firstName", "lastName"})
//			.fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
//				setTargetType(Person.class);
//			}})
//			.build();
//	}

	@Bean
	public ItemReader<Person> reader(DSLContext dslContext) {
		return new PersonItemReader(dslContext);
	}

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Person>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
			.dataSource(dataSource)
			.build();
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
	public Step step1(JdbcBatchItemWriter<Person> writer, ItemReader<Person> itemReader) {
		return stepBuilderFactory.get("step1")
			.<Person, Person> chunk(10)
			.reader(itemReader)
			.processor(processor())
			.writer(writer)
			.build();
	}
}

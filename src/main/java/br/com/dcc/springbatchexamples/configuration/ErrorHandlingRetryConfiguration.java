package br.com.dcc.springbatchexamples.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.ListItemReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import br.com.dcc.springbatchexamples.exception.ErrorHandlingException;
import br.com.dcc.springbatchexamples.listener.SimpleJobListener;
import br.com.dcc.springbatchexamples.processor.ErrorHandlingRetryItemProcessor;
import br.com.dcc.springbatchexamples.writer.ErrorHandlingRetryItemWriter;

@Configuration
public class ErrorHandlingRetryConfiguration {

	@Bean
	public ListItemReader<String> errorHandlingRetryReader() {

		List<String> items = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			items.add(String.valueOf(i));
		}

		return new ListItemReader<>(items);

	}

	@Bean
	@StepScope
	public ErrorHandlingRetryItemProcessor errorHandlingRetryItemProcessor(@Value("#{jobParameters['retry']}") String retry) {
		ErrorHandlingRetryItemProcessor errorHandlingRetryItemProcessor = new ErrorHandlingRetryItemProcessor();
		errorHandlingRetryItemProcessor.setRetry(StringUtils.hasText(retry) && retry.equalsIgnoreCase("processor"));
		return errorHandlingRetryItemProcessor;
	}

	@Bean
	@StepScope
	public ErrorHandlingRetryItemWriter errorHandlingRetryWriter(@Value("#{jobParameters['retry']}") String retry) {
		ErrorHandlingRetryItemWriter errorHandlingRetryItemWriter = new ErrorHandlingRetryItemWriter();
		errorHandlingRetryItemWriter.setRetry(StringUtils.hasText(retry) && retry.equalsIgnoreCase("writer"));
		return errorHandlingRetryItemWriter;
	}

	@Bean
	public Step errorHandlingRetryStep1(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("ErrorHandlingRetryStep1")
				.<String, String>chunk(10)
				.reader(errorHandlingRetryReader())
				.processor(errorHandlingRetryItemProcessor(null))
				.writer(errorHandlingRetryWriter(null))
				.faultTolerant()
				.retry(ErrorHandlingException.class)
				.retryLimit(15)
				.build();
	}

	@Bean
	public Job errorHandlingRetryJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		return jobBuilderFactory.get("ErrorHandlingRetryJob")
				.start(errorHandlingRetryStep1(stepBuilderFactory))
				.listener(new SimpleJobListener())
				.build();

	}

}

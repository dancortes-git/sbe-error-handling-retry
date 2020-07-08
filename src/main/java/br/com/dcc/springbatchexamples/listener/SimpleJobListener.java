package br.com.dcc.springbatchexamples.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info(">>> This is a Job Listener Before Job: {}", jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("<<< This is a Job Listener After Job: {}", jobExecution.getJobInstance().getJobName());
	}

}

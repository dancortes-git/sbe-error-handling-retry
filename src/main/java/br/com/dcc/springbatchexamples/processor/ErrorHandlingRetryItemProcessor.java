package br.com.dcc.springbatchexamples.processor;

import org.springframework.batch.item.ItemProcessor;

import br.com.dcc.springbatchexamples.exception.ErrorHandlingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ErrorHandlingRetryItemProcessor implements ItemProcessor<String, String> {

	private boolean retry = false;
	private int attemptCount = 0;

	@Override
	public String process(String item) throws Exception {
		log.info("Processing item: {}", item);
		if (retry && item.equalsIgnoreCase("42")) {
			attemptCount++;

			if (attemptCount >= 5) {
				log.info("Success");
				retry = false;
				return String.valueOf(Integer.valueOf(item)*-1);
			} else {
				log.info("Processing of item {} failed", item);
				throw new ErrorHandlingException("Process failed. Attempt: " + attemptCount);
			}
		} else {
			return String.valueOf(Integer.valueOf(item)*-1);
		}
	}

}

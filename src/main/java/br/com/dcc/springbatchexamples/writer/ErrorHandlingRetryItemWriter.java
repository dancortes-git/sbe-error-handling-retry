package br.com.dcc.springbatchexamples.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import br.com.dcc.springbatchexamples.exception.ErrorHandlingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ErrorHandlingRetryItemWriter implements ItemWriter<String> {

	private boolean retry = false;
	private int attemptCount = 0;

	@Override
	public void write(List<? extends String> items) throws Exception {
		for (String item : items) {
			log.info("Writing item: {}", item);
			if (retry && item.equalsIgnoreCase("-84")) {
				attemptCount++;

				if (attemptCount >= 5) {
					log.info("Success");
					retry = false;
					log.info("Item: {}", item);
				} else {
					log.info("Writing of item {} failed", item);
					throw new ErrorHandlingException("Write failed. Attempt: " + attemptCount);
				}
			} else {
				log.info("Item: {}", item);
			}
		}

	}

}

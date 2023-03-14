package com.kafka.producer;

import com.kafka.producer.common.Foo2;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@SpringBootApplication
public class ProducerApplication {
	private final TaskExecutor exec = new SimpleAsyncTaskExecutor();

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Bean
	public CommonErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
		return new DefaultErrorHandler(
				new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2));
	}

	@Bean
	public RecordMessageConverter converter() {
		return new JsonMessageConverter();
	}

	@KafkaListener(id = "fooGroup", topics = "first")
	public void listen(Foo2 foo) {
		log.info("Received: " + foo);
		if(foo.getFoo().startsWith("fail")) {
			throw new RuntimeException("failed");
		}
		this.exec.execute(() -> System.out.println("Hit Enter to terminate..."));
	}

	@KafkaListener(id = "dltGroup", topics = "first.DLT")
	public void dltListen(byte[] in) {
		log.info("Received from DLT: " + new String(in));
		this.exec.execute(() ->System.out.println("Hit Enter to terminate..."));
	}

	@Bean
	public NewTopic topic() {
		return new NewTopic("first",1, (short) 1);
	}

	@Bean
	public NewTopic dlt() {
		return new NewTopic("first.DLT", 1, (short) 1);
	}

	@Bean
	@Profile("default")
	public ApplicationRunner runner() {
		return args -> {
			System.out.println("Hit Enter to terminate...");
			System.in.read();
		};
	}

}

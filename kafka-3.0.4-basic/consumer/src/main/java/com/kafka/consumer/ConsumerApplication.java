package com.kafka.consumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@Bean
	public NewTopic topic() {
		return TopicBuilder.name("first")
				.partitions(1)
				.replicas(1)
				.build();
	}

	@KafkaListener(id = "myId", topics = "first")
	public void listen(String in) {
		System.out.println(in);
	}

}

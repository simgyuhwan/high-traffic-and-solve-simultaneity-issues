package com.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
@SpringBootApplication
public class ConsumerApplication {

	private final static String TOPIC_NAME = "first";
	private final static String BOOTSTRAP_SERVERS = "kafka:9092";
	private final static String CONSUMER_GROUP = "test-consumer-group";

	public static void main(String[] args) {
		Properties configs = new Properties();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP);

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);

		consumer.subscribe(Arrays.asList(TOPIC_NAME));

		while(true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
			for (ConsumerRecord<String, String> record : records) {
				System.out.println("-----------------------------------------------------------------");
				System.out.println("-----------------------------------------------------------------");
				System.out.println("-----------------------------------------------------------------");
				log.info("record : {}", record);
				System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
				System.out.println("-----------------------------------------------------------------");
				System.out.println("-----------------------------------------------------------------");
				System.out.println("-----------------------------------------------------------------");
			}
		}
	}
}

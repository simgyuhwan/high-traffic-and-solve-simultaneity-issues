package com.pipeline;

import com.pipeline.consumer.ConsumerWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootApplication
public class HdfsSinkApplcaitonApplication {
	private final static String BOOTSTRAP_SERVERS = "localhost:9092";
	private final static String TOPIC_NAME = "select-color";
	private final static String GROUP_ID = "color-hdfs-save-consumer-group";
	private final static int CONSUMER_COUNT = 1;
	private final static List<ConsumerWorker> workers = new ArrayList<>();

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new ShutdownThread());

		Properties configs = new Properties();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		ExecutorService executorService = Executors.newCachedThreadPool();
		for(int i = 0; i < CONSUMER_COUNT; i++) {
			workers.add(new ConsumerWorker(configs, TOPIC_NAME, i));
		}
		workers.forEach(executorService::execute);

		SpringApplication.run(HdfsSinkApplcaitonApplication.class, args);
	}

	static class ShutdownThread extends Thread {
		public void run() {
			log.info("Shudown hook");
			workers.forEach(ConsumerWorker::stopAndWakeup);
		}
	}
}

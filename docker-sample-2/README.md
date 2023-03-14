# Kafka 기본 연습

## docker-compose를 이용한 kafka producer 연습

### Docker-compose.yml

```yaml
version: "2"

services:
  zookeeper:
    image: zookeeper:3.6.3
    ports:
      - "2181:2181"
    networks:
      - kafka-net

  kafka:
    image: confluentinc/cp-kafka:6.2.1
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    depends_on:
      - zookeeper
    volumes:
      - ./kafka-data:/var/lib/kafka/data
    networks:
      - kafka-net
networks:
  kafka-net:
    driver: bridge
```

- docker 실행
  ```bash
  docker-compose up -d
  ```

### Producer

```java
@Slf4j
@SpringBootApplication
public class SampleApplication {
	private final static String TOPIC_NAME = "first";
	private final static String BOOTSTRAP_SERVERS = "kafka:9092";
	private static final int NUM_RECORDS = 10;

	public static void main(String[] args) {
		Properties configs = new Properties();
		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

		try{
			for(int i = 0; i <= NUM_RECORDS; i++) {
				ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "record-" + i);
				producer.send(record);

				log.info("send record = {}", record);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			producer.flush();
			producer.close();
		}
	}
}
```

### Consumer 확인

```bash
root@DESKTOP-8PVPUI8:~/kafka-3.3.2/kafka_2.13-3.3.2# bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first --from-beginning
record-0
record-1
record-2
record-3
record-4
record-5
...
```

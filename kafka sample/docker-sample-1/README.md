# Kafka 기본 연습

## 1. docker를 통한 Kakfa producer 연습

- kafka 이미지 다운로드

  ```bash
  docker run -d -p 9092:9092 --name kafka --network=kafka-net -e KAFKA_ADVERTISED_HOST_NAME=localhost -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -t wurstmeister/kafka

  ```

- zookeeper 이미지 다운로드
  ```bash
  docker run -d --name zookeeper --network=kafka-net -p 2181:2181 -t wurstmeister/zookeeper
  ```

* java producer 구현

  카프카에 대한 의존성은 추가했다고 가정

  ```java
  /**
  * 토픽 이름
  */
  private final static String TOPIC_NAME = "test";

  /**
  * 카프카 클러스터 서버의 host, ip 정보
  */
  private final static String BOOTSTRAP_SERVERS = "127.0.0.1:9092";

  public static void main(String[] args) {

  	try {
  		// set kafka properties
  		Properties configs = new Properties();
  		configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);  // kafka cluster
  		configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());// KEY_SERIALIZER
  		configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // VALUE_SERIALIZER

  		// init KafkaProducer
  		KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

  		int idx = 0;
  		while(true){

  			// set ProducerRecord
  			Integer partition = 0;           // partition number (default: Round Robin)
  			String key = "key-" + idx;       // key  (default: null)
  			String data = "record-"+ idx;    // data

  			ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, data);
  			ProducerRecord<String, String> record2 = new ProducerRecord<>(TOPIC_NAME, key, data);
  			ProducerRecord<String, String> record3 = new ProducerRecord<>(TOPIC_NAME, partition, key, data);

  			// send record
  			producer.send(record);

  			System.out.println("===============================================================");
  			System.out.println("producer.send() >> [topic:" + TOPIC_NAME + "][data:" + data + "]");
  			System.out.println("----------------------------------------------------------------");
  			Thread.sleep(1000);
  			idx++;
  		}
  	} catch (Exception e) {
  		System.out.println(e);
  	}
  }
  ```

* 명령어를 통한 메시지 확인
  ```bash
  root@DESKTOP-8PVPUI8:~/kafka-3.3.2/kafka_2.13-3.3.2# bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
  record-0
  record-1
  record-2
  record-3
  ...
  ```

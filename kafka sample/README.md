# 카프카를 사용한 간단한 프로젝트

**아파치카프카 애플리케이션 프로그래밍 with 자바**를 참고. kafka + spring + docker 등을 사용하여 간단하게 연습해보는 프로젝트

---

## 주요 포인트

---

## 기획

---

## 기술 스택

---

## Docker 설정

카프카를 docker 를 사용하여 실행한다. 초기에는 직접 명령어로 도커를 구성하나 나중에는 docker-compose로 수정할 예정

### 1) 네트워크 구성

```bash
   $ docker network create kafka-net
```

### 2) kafka 컨테이너 실행(local)

해당 이미지를 사용하는 이유는 많이 사용되는 이미지로 추가적인 기능이나 설정이 자동으로 구성되었기 때문에 사용한다.

```bash
   $ docker run -d --name kafka \
    --network kafka-net \
    -p 9092:9092 \
    -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
    -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
    confluentinc/cp-kafka:latest
```

### 3) zookeeper 컨테이너 실행

```bash
    $ docker run -d --name zookeeper \
    --network kafka-net \
    -p 2181:2181 \
    -e ZOOKEEPER_CLIENT_PORT=2181 \
    -e ZOOKEEPER_TICK_TIME=2000 \
    confluentinc/cp-zookeeper:latest

```

### 4) 토픽 설정

일단 브로커를 하나만 생성했기 때문에 replication 을 하나로 설정한다. 토픽 이름은 **'select-color'** 이다

```bash
# kafka container 접속
$ docker exec -it kafka /bin/bash

# 토픽 생성
$ kafka-topics --create --topic select-color \
                        --bootstrap-server localhost:9092 \
                        --replication-factor 1 \
                        --partitions 3
```

---

## 스프링 구성

### 의존성

```groovy
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.kafka:spring-kafka'
	// gson
	implementation 'com.google.code.gson:gson'
```

---

## API

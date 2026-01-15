# Inference Manager

> Apache Kafka와 딥러닝 모델 서빙 API를 연결하는 실시간 추론 파이프라인

[![Java](https://img.shields.io/badge/Java-8-007396?logo=java)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?logo=apache-maven)](https://maven.apache.org/)
[![Kafka](https://img.shields.io/badge/Kafka-2.0.0-231F20?logo=apache-kafka)](https://kafka.apache.org/)

## 📋 목차

- [소개](#소개)
- [주요 기능](#주요-기능)
- [아키텍처](#아키텍처)
- [기술 스택](#기술-스택)
- [설치 및 실행](#설치-및-실행)
- [사용법](#사용법)
- [데이터 형식](#데이터-형식)
- [프로젝트 구조](#프로젝트-구조)
- [라이센스](#라이센스)

## 🎯 소개

**Inference Manager**는 Apache Kafka와 HTTP 기반 서빙 API 사이의 중간 브릿지 역할을 하는 Java 애플리케이션입니다. Kafka 토픽에서 JSON 데이터를 소비하고, HTTP POST 요청으로 서빙 API에 전송한 후, 응답 결과를 다시 Kafka 토픽으로 게시합니다.

## ✨ 주요 기능

- Kafka Consumer를 통한 입력 데이터 수신
- HTTP POST 방식으로 JSON 데이터 전송
- Kafka Producer를 통한 결과 데이터 전송
- Fat JAR 방식 빌드 (의존성 포함)

## 🏗 아키텍처

### 동작 구조

![동작 구조 다이어그램](image/model-api-executor.png)

**동작 흐름**:

1. Kafka Consumer가 입력 토픽에서 데이터 폴링 (1000초 타임아웃)
2. 수신한 JSON 문자열을 HTTP POST 요청으로 서빙 API에 전송
3. HTTP Response 본문을 UTF-8로 파싱
4. Kafka Producer를 통해 출력 토픽으로 응답 문자열 전송

## 🛠 기술 스택

### 핵심 기술

| 구분 | 기술 | 버전 |
|------|------|------|
| 언어 | Java | 8 (컴파일러 설정) |
| 빌드 도구 | Apache Maven | - |
| 메시지 큐 | Apache Kafka (kafka-clients) | 2.0.0 |
| HTTP 클라이언트 | Apache HttpComponents (httpclient) | 4.5.6 |
| | Apache HttpComponents (httpmime) | 4.5.6 |
| JSON 처리 | Jackson (jackson-databind) | 2.10.0.pr1 (중복 선언) |
| | org.json | 20180813 |
| 테스트 | JUnit | 4.13.1 |

### Kafka 설정

**Consumer 설정** (APIExecutor.java:55-66):
- `group.id`: 랜덤 UUID 생성
- `enable.auto.commit`: true
- `auto.commit.interval.ms`: 1000
- `session.timeout.ms`: 30000
- Deserializer: StringDeserializer

**Producer 설정** (APIExecutor.java:70-75):
- Serializer: StringSerializer

## 📦 설치 및 실행

### 사전 요구사항

- Java Development Kit (JDK) 8 이상
- Apache Maven 3.x
- 실행 중인 Apache Kafka 클러스터
- 배포된 ML 모델 서빙 API

### 빌드 방법

```bash
# 저장소 클론
git clone https://github.com/your-repo/single-inference.git
cd single-inference

# Maven 빌드 (Fat JAR 생성)
mvn clean install

# 생성된 JAR 파일 확인
ls target/Model-API-Executor-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### 설정

실행 전에 **APIIOManager.java:7** 라인의 `bootstrap` 변수를 수정해야 합니다:

```java
String bootstrap = "";  // ← Kafka 서버 주소 입력 (예: "localhost:9092")
```

현재 빈 문자열로 설정되어 있으므로 반드시 수정 후 빌드해야 합니다.

## 🚀 사용법

### 실행 순서

1. Kafka 클러스터 실행
2. ML 모델 서빙 API 실행 (TensorFlow Serving 또는 Flask)
3. Inference Manager 실행

### 명령어

```bash
java -jar target/Model-API-Executor-1.0-SNAPSHOT-jar-with-dependencies.jar \
  <KAFKA_INPUT_TOPIC> \
  <KAFKA_OUTPUT_TOPIC> \
  <MODEL_API_ADDRESS>
```

### 매개변수 설명

| 매개변수 | 설명 | 예시 |
|---------|------|------|
| `KAFKA_INPUT_TOPIC` | 입력 데이터를 수신할 Kafka 토픽 이름 | `ml-input` |
| `KAFKA_OUTPUT_TOPIC` | 추론 결과를 게시할 Kafka 토픽 이름 | `ml-output` |
| `MODEL_API_ADDRESS` | 서빙 모델의 API 엔드포인트 URL | `http://localhost:5000/predict` |

### 실행 예시

```bash
java -jar Model-API-Executor-1.0-SNAPSHOT-jar-with-dependencies.jar \
  input-topic \
  output-topic \
  http://localhost:8000/predict
```

## 📄 데이터 형식

### 입력 데이터

Kafka 입력 토픽에 다음 형식의 JSON 메시지를 전송:

```json
{
    "instances" : []
}
```

- Kafka 메시지의 `value` 필드를 문자열로 수신
- 수신한 문자열을 그대로 HTTP POST Body에 전송
- HTTP Header: `Content-Type: application/json`

### 출력 데이터

HTTP API 응답은 다음 형식으로 Kafka 출력 토픽에 게시:

```json
{
    "predictions" : []
}
```

- HTTP Response Body를 UTF-8로 파싱
- 파싱한 문자열을 그대로 Kafka 출력 토픽에 전송

### 주의사항

코드는 데이터 형식을 검증하지 않으며, Kafka에서 받은 데이터를 그대로 HTTP API로 전달하고, 응답을 그대로 Kafka로 전송합니다.

## 📁 프로젝트 구조

```text
single-inference/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── dke/
│   │           └── executor/
│   │               ├── APIIOManager.java      # 메인 진입점
│   │               └── model/
│   │                   ├── APIExecutor.java   # Kafka 소비/생산 로직
│   │                   └── ModelRequest.java  # HTTP 요청 처리
│   └── test/
│       └── java/
│           └── dke/
│               └── executor/
│                   └── AppTest.java           # 단위 테스트
├── image/                                     # 문서 이미지
├── pom.xml                                    # Maven 빌드 설정
├── README.md                                  # 프로젝트 문서
└── .gitignore                                 # Git 무시 파일

총 라인 수: ~200 라인 (주석 제외)
```

### 주요 클래스

#### APIIOManager

- **위치**: [src/main/java/dke/executor/APIIOManager.java](src/main/java/dke/executor/APIIOManager.java)
- **기능**:
  - main 메서드 포함 (진입점)
  - args[0], args[1], args[2]를 명령행 인자로 받음
  - bootstrap 변수를 빈 문자열로 초기화 (수정 필요)

#### APIExecutor

- **위치**: [src/main/java/dke/executor/model/APIExecutor.java](src/main/java/dke/executor/model/APIExecutor.java)
- **기능**:
  - KafkaConsumer, KafkaProducer 생성 및 설정
  - `consume()`: 무한 루프로 메시지 폴링 및 처리
  - `load()`: ModelRequest 객체 생성
  - Consumer group ID를 랜덤 UUID로 생성

#### ModelRequest

- **위치**: [src/main/java/dke/executor/model/ModelRequest.java](src/main/java/dke/executor/model/ModelRequest.java)
- **기능**:
  - CloseableHttpClient 생성
  - `postData()`: HTTP POST 요청 실행
  - IOException 발생 시 printStackTrace() 호출 후 null 반환

## 📝 라이센스

라이센스 정보가 명시되어 있지 않습니다.

package com.example.service2;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testcontainers.lifecycle.Startables;

import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.SECONDS;

@Testcontainers
@SpringBootTest
public class TicketKafkaListenerTest {

    public static final String TOPIC_NAME_SEND_ORDER = "all_tickets2";
    @Container
    static final KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.4"))
                    .withEmbeddedZookeeper()
                    .withEnv("KAFKA_LISTENERS", "PLAINTEXT://0.0.0.0:9093 ,BROKER://0.0.0.0:9092")
                    .withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "BROKER:PLAINTEXT,PLAINTEXT:PLAINTEXT")
                    .withEnv("KAFKA_INTER_BROKER_LISTENER_NAME", "BROKER")
                    .withEnv("KAFKA_BROKER_ID", "1")
                    .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1")
                    .withEnv("KAFKA_OFFSETS_TOPIC_NUM_PARTITIONS", "1")
                    .withEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", "1")
                    .withEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", "1")
                    .withEnv("KAFKA_LOG_FLUSH_INTERVAL_MESSAGES", Long.MAX_VALUE + "")
                    .withEnv("KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS", "0");

    static {
        Startables.deepStart(Stream.of(kafkaContainer)).join();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
//        registry.add("spring.kafka.bootstrap-servers", () ->"PLAINTEXT://broker:9092");

        System.out.println("bootstrap server real port = " + kafkaContainer.getBootstrapServers());
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    }


    @Autowired
    TicketService ticketService;

    @Autowired
    ModelMapper modelMapper;


    @Test
    public void testConsumer() throws InterruptedException {
        String bootstrapServers = kafkaContainer.getBootstrapServers();

        Ticket fakeTicket = new Ticket(007, "James Bond");

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.TYPE_MAPPINGS, "ticketMessage:com.example.service2.Ticket, ticketMessage:com.example.service2.TicketDTO");
//        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, "false");

        ProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory);

        System.out.println("event will be sent after timeout");
        SECONDS.sleep(5);
        System.out.println("event will be sent now");
        kafkaTemplate.send(TOPIC_NAME_SEND_ORDER, modelMapper.map(fakeTicket, TicketDTO.class));
        System.out.println("event sent");
        SECONDS.sleep(5);

        List<Ticket> tickets = ticketService.getAllTickets();
        System.out.println("tickets : ");

        tickets.forEach(System.out::println);

        System.out.println("test is over");

    }
}

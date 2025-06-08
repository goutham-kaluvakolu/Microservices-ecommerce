// package order_service.microservice.config;
// import org.apache.kafka.clients.admin.NewTopic;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.kafka.config.TopicBuilder;

// @Configuration
// public class KafkaTopicConfig {

//     // Inject topic properties for order.events from application.properties
//     @Value("${kafka.topic.order.events.name}")
//     private String orderEventsTopicName;

//     @Value("${kafka.topic.order.events.partitions}")
//     private int orderEventsPartitions;

//     @Value("${kafka.topic.order.events.replicas}")
//     private short orderEventsReplicas; // Use 'short' for replicas

//     // @Bean
//     // public NewTopic orderEventsTopic() {
//     //     return TopicBuilder.name(orderEventsTopicName)
//     //             .partitions(orderEventsPartitions)
//     //             .replicas(orderEventsReplicas)
//     //             .build();
//     // }
// }
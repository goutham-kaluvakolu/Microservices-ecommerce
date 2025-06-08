// package com.ecommers.inventory_service.config;

// import com.ecommers.inventory_service.events.OrderCreatedEvent;
// import org.apache.kafka.clients.consumer.ConsumerConfig;
// import org.apache.kafka.common.serialization.StringDeserializer;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
// import org.springframework.kafka.core.ConsumerFactory;
// import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
// import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
// import org.springframework.kafka.support.serializer.JsonDeserializer;

// import java.util.HashMap;
// import java.util.Map;

// @Configuration
// public class KafkaConsumerConfig {

//     @Value("${spring.kafka.bootstrap-servers}")
//     private String bootstrapServers;

//     @Value("${spring.kafka.consumer.group-id}")
//     private String groupId;

//     @Bean
//     public ConsumerFactory<String, OrderCreatedEvent> consumerFactory() {
//         Map<String, Object> props = new HashMap<>();
//         props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//         props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//         props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//         props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//         props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
//         props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
//         props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.ecommers.inventory_service.events");
//         props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.ecommers.inventory_service.events.OrderCreatedEvent");
//         props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        
//         return new DefaultKafkaConsumerFactory<>(props);
//     }

//     @Bean
//     public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> kafkaListenerContainerFactory() {
//         ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory = 
//             new ConcurrentKafkaListenerContainerFactory<>();
//         factory.setConsumerFactory(consumerFactory());
//         return factory;
//     }
// } 
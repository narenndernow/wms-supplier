package com.wms.supplier.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic supplierEventsTopic(){
        // below method will have 2 arguments
        // String name, int numPartitions, short replicationFactor
       // return new NewTopic("supplier-events",3,(short) 1);

        // for safer, cleaner and properly formatted method
        // for a single node local broker
        return TopicBuilder.name("supplier-events")
                .partitions(1)
                .replicas(1)
                .build();
    }
}

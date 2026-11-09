package com.wms.supplier.kafka;

import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.UUID;

@Component
public class SupplierEventProducer {

    private static final String TOPIC = "supplier-events";

    private final KafkaTemplate<String, SupplierEvent> kafkaTemplate;

    public SupplierEventProducer(
            KafkaTemplate<String,SupplierEvent> kafkaTemplate){

        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(SupplierEvent event){

        kafkaTemplate.send(
                TOPIC,
                event.supplierId().toString(),
                event
        );
    }
}

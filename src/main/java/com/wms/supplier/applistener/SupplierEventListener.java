package com.wms.supplier.applistener;

import com.wms.supplier.kafka.SupplierEvent;
import com.wms.supplier.kafka.SupplierEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SupplierEventListener {

    private final SupplierEventProducer supplierEventProducer;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(SupplierEvent event){

        supplierEventProducer.publish(event);
    }
}

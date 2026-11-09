package com.wms.supplier.kafka;

import java.time.Instant;

public record SupplierEvent(
        String eventId,
        String eventType,
        Long supplierId,
        String supplierName,
        String email,
        Instant occurredAt
) {
}

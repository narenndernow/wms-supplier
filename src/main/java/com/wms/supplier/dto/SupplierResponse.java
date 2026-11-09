package com.wms.supplier.dto;

import java.time.LocalDateTime;

public record SupplierResponse(

        Long id,
        String supplierCode,
        String name,
        String contactPerson,
        String email,
        String phone,
        String address,
        String city,
        String country,
        Boolean active,
        LocalDateTime createdAt
) {
}

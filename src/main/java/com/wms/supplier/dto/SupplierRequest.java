package com.wms.supplier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SupplierRequest(

        @NotBlank(message= "Supplier code is required")
        String supplierCode,

        @NotBlank(message="Supplier name is required")
        String name,

        String contactPerson,

        @Email(message="Invalid email address")
        String email,

        String phone,
        String address,
        String city,
        String country,
        Boolean active
) {
}

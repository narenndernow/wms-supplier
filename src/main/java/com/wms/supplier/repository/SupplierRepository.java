package com.wms.supplier.repository;

import com.wms.supplier.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

        // find supplier based on supplier code
        Optional<Supplier> findBySupplierCode(String supplierCode);

        // check duplicate supplier code while creating
        boolean existsBySupplierCode(String supplierCode);

        // Check duplicate supplier code while updating
        boolean existsBySupplierCodeAndIdNot(String supplierCode, Long id);

        //search by supplier name or supplier code
        Page<Supplier>
        findByNameContainingIgnoreCaseOrSupplierCodeContainingIgnoreCase(
                String name,
                String supplierCode,
                Pageable pageable
        );

        //retrieve active suppliers
        Page<Supplier> findByActiveTrue(Pageable pageable);
}



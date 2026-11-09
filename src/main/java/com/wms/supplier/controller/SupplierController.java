package com.wms.supplier.controller;

import com.wms.supplier.dto.SupplierRequest;
import com.wms.supplier.dto.SupplierResponse;
import com.wms.supplier.service.SupplierService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService){

        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(
            @Valid @RequestBody SupplierRequest request){

       // return ResponseEntity.ok(supplierService.createSupplier(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(supplierService.createSupplier(request));

    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> listSuppliers(){

        return ResponseEntity.ok(supplierService.listSuppliers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequest request
    ){

        return ResponseEntity.ok(supplierService.updateSupplier(id,request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSupplier(@PathVariable Long id){

        supplierService.deleteSupplier(id);
    }

}

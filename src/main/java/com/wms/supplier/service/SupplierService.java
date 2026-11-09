package com.wms.supplier.service;

import com.wms.supplier.dto.SupplierRequest;
import com.wms.supplier.dto.SupplierResponse;
import com.wms.supplier.entity.Supplier;
import com.wms.supplier.exception.SupplierNotFoundException;
import com.wms.supplier.kafka.SupplierEvent;
import com.wms.supplier.kafka.SupplierEventProducer;
import com.wms.supplier.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final ApplicationEventPublisher eventPublisher;

    public  SupplierService(SupplierRepository supplierRepository,
                            ApplicationEventPublisher eventPublisher){

        this.supplierRepository = supplierRepository;
        this.eventPublisher = eventPublisher;

    }

    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request){

        if(supplierRepository.existsBySupplierCode(request.supplierCode())){
            throw new IllegalArgumentException(
                    "Supplier code already exist:"+request.supplierCode()
            );
        }

        Supplier supplier = new Supplier();

        supplier.setSupplierCode(request.supplierCode());
        supplier.setName(request.name());
        supplier.setContactPerson(request.contactPerson());
        supplier.setEmail(request.email());
        supplier.setPhone(request.phone());
        supplier.setAddress(request.address());
        supplier.setCity(request.city());
        supplier.setCountry(request.country());
        supplier.setActive(request.active() == null ? true: request.active());

        Supplier saved = supplierRepository.save(supplier);

        publishEvent("SUPPLIER_CREATED", saved);

        return new SupplierResponse(
                saved.getId(),
                saved.getSupplierCode(),
                saved.getName(),
                saved.getContactPerson(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getAddress(),
                saved.getCity(),
                saved.getCountry(),
                saved.getActive(),
                saved.getCreatedAt()
        );



    }

    @Transactional(readOnly = true)
    public List<SupplierResponse> listSuppliers() {

        return supplierRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request){

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(()-> new SupplierNotFoundException(id));

        supplier.setName(request.name());
        supplier.setContactPerson(request.contactPerson());
        supplier.setEmail(request.email());
        supplier.setAddress(request.address());
        supplier.setPhone(request.phone());

        Supplier updated = supplierRepository.save(supplier);

        publishEvent("SUPPLIER_EVENT", updated);

        return toResponse(updated);
    }

    @Transactional
    public void deleteSupplier(Long id){

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(()-> new SupplierNotFoundException(id));

        supplierRepository.delete(supplier);

        publishEvent("SUPPLIER_DELETED", supplier);
    }

    private void publishEvent(String eventType, Supplier supplier){

        SupplierEvent event = new SupplierEvent(
                UUID.randomUUID().toString(),
                eventType,
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                Instant.now()
        );

        eventPublisher.publishEvent(event);
    }

    private SupplierResponse toResponse(Supplier supplier){

        return new SupplierResponse(
                supplier.getId(),
                supplier.getSupplierCode(),
                supplier.getName(),
                supplier.getContactPerson(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.getCity(),
                supplier.getCountry(),
                supplier.getActive(),
                supplier.getCreatedAt()
        );
    }
}

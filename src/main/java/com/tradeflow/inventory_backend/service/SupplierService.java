package com.tradeflow.inventory_backend.service;

import com.tradeflow.inventory_backend.dto.ShipmentDto;
import com.tradeflow.inventory_backend.dto.SupplierDto;
import com.tradeflow.inventory_backend.entity.Shipment;
import com.tradeflow.inventory_backend.model.Supplier;
import com.tradeflow.inventory_backend.repository.ShipmentRepository;
import com.tradeflow.inventory_backend.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final ShipmentRepository shipmentRepository;

    public SupplierService(SupplierRepository supplierRepository, ShipmentRepository shipmentRepository) {
        this.supplierRepository = supplierRepository;
        this.shipmentRepository = shipmentRepository;
    }

    public SupplierDto createSupplier(SupplierDto supplierDto) {
        // Check if phone number already exists
        if (supplierDto.getContactNumber() != null &&
                supplierRepository.findByContactNumber(supplierDto.getContactNumber()).isPresent()) {
            throw new RuntimeException("Supplier with this phone number already exists");
        }

        Supplier supplier = new Supplier();
        supplier.setName(supplierDto.getName());
        supplier.setContactNumber(supplierDto.getContactNumber());
        supplier.setAddress(supplierDto.getAddress());
        supplier.setDueAmount(supplierDto.getDueAmount() != null ? supplierDto.getDueAmount() : BigDecimal.ZERO);

        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToDto(savedSupplier);
    }

    @Transactional
    public ShipmentDto createShipment(ShipmentDto shipmentDto) {
        // Find supplier by name
        Supplier supplier = supplierRepository.findByName(shipmentDto.getSupplierName())
                .orElseThrow(() -> new RuntimeException("Supplier not found with name: " + shipmentDto.getSupplierName()));

        // Create shipment
        Shipment shipment = new Shipment();
        shipment.setSupplier(supplier);
        shipment.setDate(shipmentDto.getDate());
        shipment.setPurchaseAmount(shipmentDto.getPurchaseAmount());
        shipment.setLaborCost(shipmentDto.getLaborCost() != null ? shipmentDto.getLaborCost() : BigDecimal.ZERO);
        shipment.setTransportCost(shipmentDto.getTransportCost() != null ? shipmentDto.getTransportCost() : BigDecimal.ZERO);
        shipment.setPaidAmount(shipmentDto.getPaidAmount() != null ? shipmentDto.getPaidAmount() : BigDecimal.ZERO);
        shipment.setTotalAmount(shipmentDto.getTotalAmount());
        shipment.setDueAmount(shipmentDto.getDueAmount());

        // Update supplier due amount
        BigDecimal currentDue = supplier.getDueAmount() != null ? supplier.getDueAmount() : BigDecimal.ZERO;
        supplier.setDueAmount(currentDue.add(shipmentDto.getDueAmount()));

        // Save both shipment and updated supplier
        Shipment savedShipment = shipmentRepository.save(shipment);
        supplierRepository.save(supplier);

        return convertToShipmentDto(savedShipment);
    }

    public Page<SupplierDto> getAllSuppliers(int page, int size, String sortBy, String sortDir, String searchQuery) {
        // Normalize empty strings to null
        searchQuery = (searchQuery != null && searchQuery.trim().isEmpty()) ? null : searchQuery;

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Page<Supplier> suppliers = supplierRepository.findAllWithSearch(searchQuery, pageable);
        return suppliers.map(this::convertToDto);
    }

    public Page<ShipmentDto> getAllShipments(int page, int size, String sortBy, String sortDir, String searchQuery) {
        // Normalize empty strings to null
        searchQuery = (searchQuery != null && searchQuery.trim().isEmpty()) ? null : searchQuery;

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Page<Shipment> shipments = shipmentRepository.findAllWithSearch(searchQuery, pageable);
        return shipments.map(this::convertToShipmentDto);
    }

    public Optional<SupplierDto> getSupplierById(Long id) {
        return supplierRepository.findById(id).map(this::convertToDto);
    }

    public SupplierDto updateSupplier(Long id, SupplierDto supplierDto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        // Check if phone number already exists for another supplier
        if (supplierDto.getContactNumber() != null) {
            Optional<Supplier> existingSupplier = supplierRepository.findByContactNumber(supplierDto.getContactNumber());
            if (existingSupplier.isPresent() && !existingSupplier.get().getSupplierId().equals(id)) {
                throw new RuntimeException("Another supplier with this phone number already exists");
            }
        }

        supplier.setName(supplierDto.getName());
        supplier.setContactNumber(supplierDto.getContactNumber());
        supplier.setAddress(supplierDto.getAddress());
        if (supplierDto.getDueAmount() != null) {
            supplier.setDueAmount(supplierDto.getDueAmount());
        }

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return convertToDto(updatedSupplier);
    }

    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    private SupplierDto convertToDto(Supplier supplier) {
        return new SupplierDto(
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getContactNumber(),
                supplier.getAddress(),
                supplier.getDueAmount()
        );
    }

    private ShipmentDto convertToShipmentDto(Shipment shipment) {
        return new ShipmentDto(
                shipment.getShipmentId(),
                shipment.getSupplier().getName(),
                shipment.getDate(),
                shipment.getPurchaseAmount(),
                shipment.getLaborCost(),
                shipment.getTransportCost(),
                shipment.getPaidAmount(),
                shipment.getTotalAmount(),
                shipment.getDueAmount()
        );
    }
}
package com.tradeflow.inventory_backend.controller;

import com.tradeflow.inventory_backend.dto.ShipmentDto;
import com.tradeflow.inventory_backend.dto.SupplierDto;
import com.tradeflow.inventory_backend.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
@PreAuthorize("hasRole('ADMIN')")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<SupplierDto> createSupplier(@RequestBody SupplierDto supplierDto) {
        try {
            SupplierDto createdSupplier = supplierService.createSupplier(supplierDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSupplier);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<SupplierDto>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String searchQuery) {

        Page<SupplierDto> suppliers = supplierService.getAllSuppliers(page, size, sortBy, sortDir, searchQuery);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable Long id) {
        Optional<SupplierDto> supplier = supplierService.getSupplierById(id);
        return supplier.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> updateSupplier(@PathVariable Long id, @RequestBody SupplierDto supplierDto) {
        try {
            SupplierDto updatedSupplier = supplierService.updateSupplier(id, supplierDto);
            return ResponseEntity.ok(updatedSupplier);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/shipments")
    public ResponseEntity<ShipmentDto> createShipment(@RequestBody ShipmentDto shipmentDto) {
        try {
            ShipmentDto createdShipment = supplierService.createShipment(shipmentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdShipment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
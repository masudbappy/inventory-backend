package com.tradeflow.inventory_backend.service;

import com.tradeflow.inventory_backend.dto.CategoryDto;
import com.tradeflow.inventory_backend.dto.ProductDto;
import com.tradeflow.inventory_backend.dto.SupplierDto;
import com.tradeflow.inventory_backend.dto.TypeDto;
import com.tradeflow.inventory_backend.dto.WarehouseDto;
import com.tradeflow.inventory_backend.exception.ResourceNotFoundException;
import com.tradeflow.inventory_backend.model.Category;
import com.tradeflow.inventory_backend.model.Product;
import com.tradeflow.inventory_backend.model.Supplier;
import com.tradeflow.inventory_backend.model.Type;
import com.tradeflow.inventory_backend.model.Warehouse;
import com.tradeflow.inventory_backend.repository.CategoryRepository;
import com.tradeflow.inventory_backend.repository.ProductRepository;
import com.tradeflow.inventory_backend.repository.SupplierRepository;
import com.tradeflow.inventory_backend.repository.TypeRepository;
import com.tradeflow.inventory_backend.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final SupplierRepository supplierRepository;
	private final WarehouseRepository warehouseRepository;
	private final TypeRepository typeRepository;

	public ProductService(ProductRepository productRepository,
	                      CategoryRepository categoryRepository,
	                      SupplierRepository supplierRepository,
	                      WarehouseRepository warehouseRepository,
	                      TypeRepository typeRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.supplierRepository = supplierRepository;
		this.warehouseRepository = warehouseRepository;
		this.typeRepository = typeRepository;
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProductById(Long id) throws ResourceNotFoundException {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

	public Product createProduct(ProductDto productDto) throws ResourceNotFoundException {
		Product product = new Product();

		// Set basic fields
		product.setName(productDto.getName());
		product.setProductCode(productDto.getProductCode());
		product.setStock(productDto.getStock());
		product.setUnit(productDto.getUnit());
		product.setBuyingPrice(productDto.getBuyingPrice());
		product.setSellingPrice(productDto.getSellingPrice());
		product.setDate(productDto.getDate() != null ? productDto.getDate() : LocalDate.now());

		// Handle Category - find or create
		Category category = findOrCreateCategory(productDto.getCategory());
		product.setCategory(category);

		// Handle TypeEntity - find or create
		Type typeEntity = findOrCreateTypeEntity(productDto.getTypeEntity());
		product.setTypeEntity(typeEntity);

		// Handle Warehouse - find or create
		Warehouse warehouse = findOrCreateWarehouse(productDto.getWarehouse());
		product.setWarehouse(warehouse);

		// Handle Supplier - find or create
		Supplier supplier = findOrCreateSupplier(productDto.getSupplier());
		product.setSupplier(supplier);

		return productRepository.save(product);

	}

	public Product updateProduct(Long id, ProductDto productDto) throws ResourceNotFoundException {
		Product existingProduct = getProductById(id);

		// Update basic fields
		existingProduct.setName(productDto.getName());
		existingProduct.setProductCode(productDto.getProductCode());
		existingProduct.setStock(productDto.getStock());
		existingProduct.setUnit(productDto.getUnit());
		existingProduct.setBuyingPrice(productDto.getBuyingPrice());
		existingProduct.setSellingPrice(productDto.getSellingPrice());
		existingProduct.setDate(productDto.getDate() != null ? productDto.getDate() : existingProduct.getDate());

		// Update Category - find or create
		if (productDto.getCategory() != null) {
			Category category = findOrCreateCategory(productDto.getCategory());
			existingProduct.setCategory(category);
		}

		// Update TypeEntity - find or create
		if (productDto.getTypeEntity() != null) {
			Type typeEntity = findOrCreateTypeEntity(productDto.getTypeEntity());
			existingProduct.setTypeEntity(typeEntity);
		}

		// Update Warehouse - find or create
		if (productDto.getWarehouse() != null) {
			Warehouse warehouse = findOrCreateWarehouse(productDto.getWarehouse());
			existingProduct.setWarehouse(warehouse);
		}

		// Update Supplier - find or create
		if (productDto.getSupplier() != null) {
			Supplier supplier = findOrCreateSupplier(productDto.getSupplier());
			existingProduct.setSupplier(supplier);
		}

		return productRepository.save(existingProduct);
	}

	public void deleteProduct(Long id) throws ResourceNotFoundException {
		Product product = getProductById(id);
		productRepository.delete(product);
	}

	private Category findOrCreateCategory(CategoryDto categoryDto) {
		return categoryRepository.findByName(categoryDto.getName())
				.orElseGet(() -> {
					Category newCategory = new Category();
					newCategory.setName(categoryDto.getName());
					return categoryRepository.save(newCategory);
				});
	}

	private Type findOrCreateTypeEntity(TypeDto typeDto) {
		return typeRepository.findByName(typeDto.getName())
				.orElseGet(() -> {
					Type newType = new Type();
					newType.setName(typeDto.getName());
					return typeRepository.save(newType);
				});
	}

	private Warehouse findOrCreateWarehouse(WarehouseDto warehouseDto) {
		return warehouseRepository.findByWarehouseNameAndLocation(
						warehouseDto.getWarehouseName(), warehouseDto.getLocation())
				.orElseGet(() -> {
					Warehouse newWarehouse = new Warehouse();
					newWarehouse.setWarehouseName(warehouseDto.getWarehouseName());
					newWarehouse.setLocation(warehouseDto.getLocation());
					return warehouseRepository.save(newWarehouse);
				});
	}

	private Supplier findOrCreateSupplier(SupplierDto supplierDto) {
		return supplierRepository.findByNameAndContactNumber(
						supplierDto.getName(), supplierDto.getContactNumber())
				.orElseGet(() -> {
					Supplier newSupplier = new Supplier();
					newSupplier.setName(supplierDto.getName());
					newSupplier.setContactNumber(supplierDto.getContactNumber());
					newSupplier.setAddress(supplierDto.getAddress());
					return supplierRepository.save(newSupplier);
				});
	}
}

package com.tradeflow.inventory_backend.service;

import com.tradeflow.inventory_backend.dto.CategoryDto;
import com.tradeflow.inventory_backend.dto.ProductDto;
import com.tradeflow.inventory_backend.dto.SupplierDto;
import com.tradeflow.inventory_backend.dto.TypeDto;
import com.tradeflow.inventory_backend.dto.WarehouseDto;
import com.tradeflow.inventory_backend.dto.output.CategoryResponseDto;
import com.tradeflow.inventory_backend.dto.output.ProductResponseDto;
import com.tradeflow.inventory_backend.dto.output.SupplierResponseDto;
import com.tradeflow.inventory_backend.dto.output.TypeResponseDto;
import com.tradeflow.inventory_backend.dto.output.WarehouseResponseDto;
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
import java.util.stream.Collectors;

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

	public ProductResponseDto createProduct(ProductDto productDto) throws ResourceNotFoundException {
		// Check if product code already exists
		if (productRepository.existsByProductCode(productDto.getProductCode())) {
			throw new IllegalArgumentException("Product with code '" + productDto.getProductCode() + "' already exists");
		}
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

		Product savedProduct = productRepository.save(product);
		return convertToResponseDto(savedProduct);
	}

	public ProductResponseDto updateProduct(Long id, ProductDto productDto) throws ResourceNotFoundException {
		Product existingProduct = getProductEntityById(id);

		if (!existingProduct.getProductCode().equals(productDto.getProductCode()) &&
				productRepository.existsByProductCode(productDto.getProductCode())) {
			throw new IllegalArgumentException("Product with code '" + productDto.getProductCode() + "' already exists");
		}

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

		Product updatedProduct = productRepository.save(existingProduct);
		return convertToResponseDto(updatedProduct);
	}

	public void deleteProduct(Long id) throws ResourceNotFoundException {
		Product product = getProductEntityById(id);
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

	public List<ProductResponseDto> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream()
				.map(this::convertToResponseDto)
				.collect(Collectors.toList());
	}

	public ProductResponseDto getProductById(Long id) throws ResourceNotFoundException {
		Product product = getProductEntityById(id);
		return convertToResponseDto(product);
	}

	private Product getProductEntityById(Long id) throws ResourceNotFoundException {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

	private ProductResponseDto convertToResponseDto(Product product) {
		ProductResponseDto dto = new ProductResponseDto();
		dto.setProductId(product.getProductId());
		dto.setName(product.getName());
		dto.setProductCode(product.getProductCode());
		dto.setStock(product.getStock());
		dto.setUnit(product.getUnit());
		dto.setBuyingPrice(product.getBuyingPrice());
		dto.setSellingPrice(product.getSellingPrice());
		dto.setDate(product.getDate());

		if (product.getCategory() != null) {
			dto.setCategory(convertToCategoryResponseDto(product.getCategory()));
		}

		if (product.getTypeEntity() != null) {
			dto.setTypeEntity(convertToTypeResponseDto(product.getTypeEntity()));
		}

		if (product.getWarehouse() != null) {
			dto.setWarehouse(convertToWarehouseResponseDto(product.getWarehouse()));
		}

		if (product.getSupplier() != null) {
			dto.setSupplier(convertToSupplierResponseDto(product.getSupplier()));
		}

		return dto;
	}

	private CategoryResponseDto convertToCategoryResponseDto(Category category) {
		CategoryResponseDto dto = new CategoryResponseDto();
		dto.setCategoryId(category.getCategoryId());
		dto.setName(category.getName());
		return dto;
	}

	private TypeResponseDto convertToTypeResponseDto(Type type) {
		TypeResponseDto dto = new TypeResponseDto();
		dto.setTypeId(type.getTypeId());
		dto.setName(type.getName());
		return dto;
	}

	private WarehouseResponseDto convertToWarehouseResponseDto(Warehouse warehouse) {
		WarehouseResponseDto dto = new WarehouseResponseDto();
		dto.setWarehouseId(warehouse.getWarehouseId());
		dto.setWarehouseName(warehouse.getWarehouseName());
		dto.setLocation(warehouse.getLocation());
		return dto;
	}

	private SupplierResponseDto convertToSupplierResponseDto(Supplier supplier) {
		SupplierResponseDto dto = new SupplierResponseDto();
		dto.setSupplierId(supplier.getSupplierId());
		dto.setName(supplier.getName());
		dto.setContactNumber(supplier.getContactNumber());
		dto.setAddress(supplier.getAddress());
		return dto;
	}
}
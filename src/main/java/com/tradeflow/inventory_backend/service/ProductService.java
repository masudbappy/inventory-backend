package com.tradeflow.inventory_backend.service;

import com.tradeflow.inventory_backend.dto.ProductDto;
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
		setProductFields(product, productDto);
		setProductRelationships(product, productDto);
		return productRepository.save(product);
	}

	public Product updateProduct(Long id, ProductDto productDto) throws ResourceNotFoundException {
		Product existingProduct = getProductById(id);
		setProductFields(existingProduct, productDto);
		setProductRelationships(existingProduct, productDto);
		return productRepository.save(existingProduct);
	}

	public void deleteProduct(Long id) throws ResourceNotFoundException {
		Product product = getProductById(id);
		productRepository.delete(product);
	}

	private void setProductFields(Product product, ProductDto productDto) {
		product.setName(productDto.getName());
		product.setProductCode(productDto.getProductCode());
		product.setType(productDto.getType());
		product.setStock(productDto.getStock());
		product.setBuyingPrice(productDto.getBuyingPrice());
		product.setSellingPrice(productDto.getSellingPrice());
		product.setUnit(productDto.getUnit());
		product.setLowStockThreshold(productDto.getLowStockThreshold());
	}

	private void setProductRelationships(Product product, ProductDto productDto) throws ResourceNotFoundException {
		// Handle Category relationship
		if (productDto.getCategoryId() != null) {
			Category category = categoryRepository.findById(productDto.getCategoryId())
					.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDto.getCategoryId()));
			product.setCategory(category);
		}

		// Handle Supplier relationship
		if (productDto.getSupplierId() != null) {
			Supplier supplier = supplierRepository.findById(productDto.getSupplierId())
					.orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + productDto.getSupplierId()));
			product.setSupplier(supplier);
		}

		// Handle Warehouse relationship
		if (productDto.getWarehouseId() != null) {
			Warehouse warehouse = warehouseRepository.findById(productDto.getWarehouseId())
					.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + productDto.getWarehouseId()));
			product.setWarehouse(warehouse);
		}

		// Handle Type relationship
		if (productDto.getTypeId() != null) {
			Type typeEntity = typeRepository.findById(productDto.getTypeId())
					.orElseThrow(() -> new ResourceNotFoundException("Type not found with id: " + productDto.getTypeId()));
			product.setTypeEntity(typeEntity);
		}
	}
}

package com.tradeflow.inventory_backend.controller;

import com.tradeflow.inventory_backend.dto.MessageResponse;
import com.tradeflow.inventory_backend.dto.ProductDto;
import com.tradeflow.inventory_backend.dto.output.ProductResponseDto;
import com.tradeflow.inventory_backend.exception.ResourceNotFoundException;
import com.tradeflow.inventory_backend.model.Product;
import com.tradeflow.inventory_backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
@PreAuthorize("hasRole('ADMIN')")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
		List<ProductResponseDto> products = productService.getAllProducts();
		return ResponseEntity.ok(products);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponseDto> getProductById(@PathVariable("id") Long id) {
		try {
			ProductResponseDto product = productService.getProductById(id);
			return ResponseEntity.ok(product);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductDto productDto) {
		try {
			ProductResponseDto createdProduct = productService.createProduct(productDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto productDto) {
		try {
			ProductResponseDto updatedProduct = productService.updateProduct(id, productDto);
			return ResponseEntity.ok(updatedProduct);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<MessageResponse> deleteProduct(@PathVariable("id") Long id) {
		try {
			productService.deleteProduct(id);
			return ResponseEntity.ok(new MessageResponse("Product deleted successfully"));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Page<ProductResponseDto>> searchProducts(
			@RequestParam(name = "q") String query,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sortBy", defaultValue = "productId") String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {

		Page<ProductResponseDto> products = productService.searchProducts(query, page, size, sortBy, sortDir);
		return ResponseEntity.ok(products);
	}
}

package com.packt.webstore.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.packt.webstore.domain.Product;
import com.packt.webstore.domain.repository.ProductRepository;
import com.packt.webstore.domain.repository.impl.InMemoryProductRepository;
import com.packt.webstore.service.ProductService;
@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository; 
	
	public List<Product> getAllProducts() {
		
		return productRepository.getAllProducts();
	}

	public Product getProductById(String productId) {
		return productRepository.getProductById(productId);
	}

	public List<Product> getProductsByCategory(String category) {
		
		return productRepository.getProductsByCategory(category);
	}

	public Set<Product> getProductsByFilter(Map<String, List<String>> filterParams) {
		
		return productRepository.getProductsByFilter(filterParams);
	}

	public List<Product> getProductsByManufacturer(String manufacturer) {
		
		return productRepository.getProductsByManufacturer(manufacturer);
	}

	public List<Product> getProductsByPriceFilter(BigDecimal low, BigDecimal high) {
		
		return productRepository.getProductsByPriceFilter(low, high);
	}

	@Override
	public void addProduct(Product product) {
		productRepository.addProduct(product);
		
	}
	

}

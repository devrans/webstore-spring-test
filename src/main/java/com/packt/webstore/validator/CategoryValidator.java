package com.packt.webstore.validator;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.packt.webstore.domain.Product;
import com.packt.webstore.exception.ProductNotFoundException;
import com.packt.webstore.service.ProductService;

public class CategoryValidator implements ConstraintValidator<Category, String>{
	
	List <String> allowedCategories;
	
	@Autowired
	private ProductService productService;
	
	public CategoryValidator() {
		allowedCategories = new ArrayList<>();
		allowedCategories.add("Laptop");
		allowedCategories.add("Games");
		allowedCategories.add("Books");
	}

	public void initialize(Category constraintAnnotation) {
		//  celowo pozostawione puste; w tym miejscu nale�y zainicjowa� adnotacj� ograniczaj�c� do sensownych domy�lnych w
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (allowedCategories.contains(value)) {
			return true;
		}
		
		//		try {
//			product = productService.getProductById(value);
//			
//		} catch (ProductNotFoundException e) {
//			return true;
//		}
//		
//		if(product!= null) {
//			return false;
//		}
//		
		return false;
	}
	 

}

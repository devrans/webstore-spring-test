package com.packt.webstore.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.packt.webstore.domain.Product;
import com.packt.webstore.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService; 
	

	@RequestMapping
	public String list(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return "products";
	}
	@RequestMapping("/all")
	public String allProducts(Model model){
		model.addAttribute("products", productService.getAllProducts());
		return "products";
	}
	@RequestMapping("/{category}")
	public String getProductsByCategory(@PathVariable("category") String productCategory, Model model) {
		model.addAttribute("products", productService.getProductsByCategory(productCategory));
		return "products";
	}
	
	@RequestMapping("/filter/{ByCriteria}/{BySpecification}")
	public String getProductsByFilter(@MatrixVariable(pathVar="ByCriteria") Map<String,List<String>> filterParams, @MatrixVariable(pathVar="BySpecification") Map<String, List<String>> specFilter, Model model) {
		model.addAttribute("products", productService.getProductsByFilter(filterParams));
		return "products";
	}
	
	@RequestMapping("/filter/{ByCriteria}")
	public String getProductsByFilter(@MatrixVariable(pathVar="ByCriteria") Map<String,List<String>> filterParams, Model model) {
		model.addAttribute("products", productService.getProductsByFilter(filterParams));
		return "products";
	}
	@RequestMapping("/product")
	public String getProductById(@RequestParam("id") String productId, Model model) {
		model.addAttribute("product", productService.getProductById(productId));
		return "product";
	}
	@RequestMapping("/{category}/{ByPrice}")
	public String filterProducts(@RequestParam("manufacturer") String manufacturer,@PathVariable("category") String productCategory, @MatrixVariable(pathVar="ByPrice") Map<String,List<String>> priceParams, Model model) {
		List<Product> filteredProducts = new ArrayList<Product>();
		List<Product> filteredProducts2 = productService.getProductsByCategory(productCategory);
		
		List<Product> filteredProducts3 = productService.getProductsByPriceFilter(BigDecimal.valueOf(Integer.parseInt(priceParams.get("low").get(0))),BigDecimal.valueOf(Integer.parseInt(priceParams.get("high").get(0)) ) ); 
		List<Product> filteredProducts4 = productService.getProductsByManufacturer(manufacturer);
		
		if ( productService.getProductsByCategory(productCategory) != null &&
				productService.getProductsByPriceFilter(BigDecimal.valueOf(Integer.parseInt(priceParams.get("low").get(0))),BigDecimal.valueOf(Integer.parseInt(priceParams.get("high").get(0)) ) ) != null &&
				productService.getProductsByManufacturer(manufacturer) != null) {
//			
			for (Product product : filteredProducts2) {
				if (filteredProducts3.contains(product) && filteredProducts4.contains(product)) {
					filteredProducts.add(product);
				}
			}
		}
		
		model.addAttribute("products", filteredProducts );
		return "products";
	}
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getAddNewProductFrom(Model model) {
		Product newProduct = new Product();
		model.addAttribute("newProduct", newProduct);
		return "addProduct";
	}
	
	
	
	@RequestMapping(value="/add", method = RequestMethod.POST)
	public String processAddNewProductFrom(@ModelAttribute("newProduct")Product newProduct, BindingResult result){
		String[] suppressedFields = result.getSuppressedFields();
		
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Próba wi¹zania niedozwolonych pól: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		productService.addProduct(newProduct);
		return "redirect:/products";
	}
	@InitBinder
	public void initialseBinder(WebDataBinder binder) {
		binder.setDisallowedFields("unitsInOrder", "discontinued");
	}

}
package com.packt.webstore.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.packt.webstore.domain.Product;
import com.packt.webstore.exception.NoProductsFoundUnderCategoryException;
import com.packt.webstore.exception.ProductNotFoundException;
import com.packt.webstore.service.ProductService;
import com.packt.webstore.validator.ProductImageValidator;
import com.packt.webstore.validator.ProductValidator;
import com.packt.webstore.validator.UnitsInStockValidator;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService; 
	
	@Autowired
	private UnitsInStockValidator unitsInStockValidator;
	
	@Autowired
	private ProductValidator productValidator;
	
	@Autowired
	private ProductImageValidator productImageValidator;
	

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
		List<Product> products = productService.getProductsByCategory(productCategory);
		if (products == null || products.isEmpty()) {
			throw new NoProductsFoundUnderCategoryException();
		}
		model.addAttribute("products", products);
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
	public String processAddNewProductFrom(@ModelAttribute("newProduct")@Valid Product newProduct, BindingResult result, HttpServletRequest request){
		if(result.hasErrors()) {
			return "addProduct";
		}
		
		String[] suppressedFields = result.getSuppressedFields();
		
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Próba wi¹zania niedozwolonych pól: " + StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
			
		MultipartFile productImage = newProduct.getProductImage();
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
				
			if (productImage!=null && !productImage.isEmpty()) {
		       try {
		      	productImage.transferTo(new File(rootDirectory+"resources/images/"+newProduct.getProductId() + ".png"));
		       } catch (Exception e) {
				throw new RuntimeException("Próba zapisu obrazka zakoñczona niepowodzeniem", e);
		   }
		   }
		
		MultipartFile productPdf = newProduct.getProductPdf();		
		if (productPdf!=null && !productPdf.isEmpty()) {
	       try {
	    	   productPdf.transferTo(new File(rootDirectory+"resources/pdf/"+newProduct.getProductId() + ".pdf"));	    	   
	       } catch (Exception e) {
			throw new RuntimeException("Próba zapisu pdf zakoñczona niepowodzeniem", e);
	   }
	   }		
		
		productService.addProduct(newProduct);
		return "redirect:/products";
	}
	@InitBinder
	public void initialseBinder(WebDataBinder binder) {
		binder.setAllowedFields("productId","name","unitPrice","description","manufacturer","category","unitsInStock", "condition","productImage", "productPdf", "language");
		binder.setDisallowedFields("unitsInOrder", "discontinued");
		binder.setValidator(productValidator);
		//binder.setValidator(productImageValidator);
	}
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ModelAndView handleError(HttpServletRequest req, ProductNotFoundException exception) {
		 ModelAndView mav = new ModelAndView();
		 mav.addObject("invalidProductId", exception.getProductId());
		 mav.addObject("exception", exception);
		 mav.addObject("url", req.getRequestURL()+"?"+req.getQueryString());
		 mav.setViewName("productNotFound");
		 return mav;
	}
	
	@RequestMapping("/invalidPromoCode")
	public String invalidPromoCode() {		
		return "invalidPromoCode";
	}

}

package com.packt.webstore.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;

@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String welcome(Model model) {
		model.addAttribute("greeting", "Witaj w sklepie internetowym!");
		model.addAttribute("tagline", "Wyjątkowym i jedynym sklepie internetowym");
		
		return "welcome";
	}
	
//	@RequestMapping("/home")
//	public ModelAndView greeting(Map<String, Object> model ) {
//		model.put("greeting", "Witaj w sklepie internetowym!");
//		model.put("tagline", "wspanialy sklepik");
//		View view = new InternalResourceView("/WEB-INF/views/welcome.jsp");
//		return new ModelAndView(view, model);
//	}
	
	@RequestMapping("/welcome/greeting")
	public String greeting() {
//		return "redirect:/welcome/greeting";
		return "welcome";
	}
}

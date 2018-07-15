package com.packt.webstore.domain.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.packt.webstore.domain.Customer;
import com.packt.webstore.domain.repository.CustomerRepository;
 @Repository
public class InMemoryCustomerRepository implements CustomerRepository {
	private List <Customer> listOfCustomers = new ArrayList<Customer>();
	
	
	public InMemoryCustomerRepository() {
		Customer ks = new Customer("1", "Karolina Slonka", "Zielona Gora", 10);
		Customer kim = new Customer("2", "Kim Dzong Un", "Pjongjang", 2);
		Customer ad = new Customer("3", "Andrzej Duda", "Warszawa", 13);
		
		listOfCustomers.add(ks);
		listOfCustomers.add(kim);
		listOfCustomers.add(ad);
	}
	
	public List<Customer> getAllCustomers() {
		return listOfCustomers;
	}

}

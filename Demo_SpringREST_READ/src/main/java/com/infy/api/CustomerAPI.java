package com.infy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.CustomerDTO;
import com.infy.exception.InfyBankException;
import com.infy.service.CustomerService;

@RestController
@RequestMapping(value = "/infybank") //I made this change to test
public class CustomerAPI {

	@Autowired
	private CustomerService customerService; //auto wiring customer service
	
	@Autowired
	Environment environment;  //auto wiring Environment to display messages


	@GetMapping(value = "/customers") //@GetRequest handles GET request (Get all customer details)
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() throws InfyBankException {
		List<CustomerDTO> customerList = customerService.getAllCustomers();
		return new ResponseEntity<>(customerList, HttpStatus.OK);
	}

	@GetMapping(value = "/customers/{customerId}") //fetches details of customer based on customerId
	public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Integer customerId) throws InfyBankException { //@PathVariable annotation is used to extract the value from the URI
		                                                                                                        //template variable (here custid)
		CustomerDTO customer = customerService.getCustomer(customerId);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}
	@PostMapping(value = "/customers") //@PostMapping handles Post request (Add customers)
	public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customer) throws InfyBankException { //used to bind the HTTP request/response body
		                                                                                                    //with a domain object in method parameter or return type.
		Integer customerId = customerService.addCustomer(customer);
		String successMessage = environment.getProperty("API.INSERT_SUCCESS") + customerId;
		return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
	}
	@PutMapping(value = "/customers/{customerId}") //@PutMapping handles http put request (Update customer)
	public ResponseEntity<String> updateCustomer(@PathVariable Integer customerId, @RequestBody CustomerDTO customer)
			throws InfyBankException {
		customerService.updateCustomer(customerId, customer.getEmailId());
		String successMessage = environment.getProperty("API.UPDATE_SUCCESS");
		return new ResponseEntity<>(successMessage, HttpStatus.OK);
	}
	@DeleteMapping(value = "/customers/{customerId}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Integer customerId) throws InfyBankException {
		customerService.deleteCustomer(customerId);
		String successMessage = environment.getProperty("API.DELETE_SUCCESS");
		return new ResponseEntity<>(successMessage, HttpStatus.OK);
	}
}

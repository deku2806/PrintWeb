package Admin.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Admin.Models.*;
import Admin.Repository.*;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
	@Autowired
	private CustomerRepository customerRepo;
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(){
		List<Customer> customer = customerRepo.findAll();
		return ResponseEntity.ok(customer);
	}
}

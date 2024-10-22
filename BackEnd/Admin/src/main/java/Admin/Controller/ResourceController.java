package Admin.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Admin.Models.*;
import Admin.Repository.*;

@RestController
@RequestMapping("api/resource")
public class ResourceController {
	@Autowired
	private ResourcePropertyDetailRepository resourcePropertyDetailRepo;
	@Autowired
	private ResourcePropertyRepository resourcePropertyRepo;
	
	@GetMapping("/allResource")
	public ResponseEntity<?> getAll(){
		List<ResourcePropertyDetail> resources = resourcePropertyDetailRepo.findAll();
		return ResponseEntity.ok(resources);
	}
	
	@GetMapping("/allResourceProperty")
	public ResponseEntity<?> getAllProp(){
		List<ResourceProperty> resourceProps = resourcePropertyRepo.findAll();
		return ResponseEntity.ok(resourceProps);
	}
	
	@PostMapping("/update-quantity")
	public ResponseEntity<?> updateQuantity(@RequestParam int addQuantity, int resourcePropertyDetailId){
		Optional<ResourcePropertyDetail> resourcePropertyDetailOpt = resourcePropertyDetailRepo.findById(resourcePropertyDetailId);
		ResourcePropertyDetail resourcePropertyDetail = resourcePropertyDetailOpt.get();
		resourcePropertyDetail.setQuantity(resourcePropertyDetail.getQuantity()+addQuantity);
		resourcePropertyDetailRepo.save(resourcePropertyDetail);
		return ResponseEntity.ok(resourcePropertyDetail);
	}
	
}

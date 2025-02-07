package Admin.Controller;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import Admin.Models.*;
import Admin.Repository.*;

@RestController
@RequestMapping("api/delivery")
public class DeliveryController {
	@Autowired
	private DeliveryRepository deliveryRepo;
	@Autowired
	private ProjectRepository projectRepo;
	@Autowired
	private ShippingMethodRepository shippingMethodRepo;
	@Autowired
    private JavaMailSender mailSender;
	
	@GetMapping("/allDelivery")
	public ResponseEntity<?> getAll(){
		List<Delivery> delivery = deliveryRepo.findAll();
		return ResponseEntity.ok(delivery);
	}
	
	@GetMapping("/deliveryByProject")
	public ResponseEntity<?> getByProjectId(@RequestParam int projectId){
		Delivery delivery = deliveryRepo.findByProjectId(projectId);
		return ResponseEntity.ok(delivery);
	}
	
	@PostMapping("/shipping")
    public ResponseEntity<?> shipProject(@RequestBody Delivery dataDelivery) {
        Optional<Delivery> deliveryOpt = deliveryRepo.findById(dataDelivery.getId());
        if(deliveryOpt.isEmpty()) {
        	return ResponseEntity.badRequest().body("FAIL");
        }
        Delivery delivery = deliveryOpt.get();
        delivery.setStatus(Delivery.DeliveryStatus.DA_VAN_CHUYEN);
        delivery.setEstimateDeliveryTime(dataDelivery.getEstimateDeliveryTime()); 
        delivery.setDeliveryId(dataDelivery.getDeliveryId());
        Delivery savedDelivery = deliveryRepo.save(delivery);
        return ResponseEntity.ok(savedDelivery);
    }
	
	@PostMapping("/create")
    public ResponseEntity<?> createDelivery(@RequestBody Delivery delivery) {
        delivery.setStatus(Delivery.DeliveryStatus.DANG_GIAO);
        Delivery savedDelivery = deliveryRepo.save(delivery);
        return ResponseEntity.ok(savedDelivery);
    }
	
	@GetMapping("/method")
	public ResponseEntity<?> getAllMethod(){
		List<ShippingMethod> method = shippingMethodRepo.findAll();
		return ResponseEntity.ok(method);
	}
	
	@PostMapping("/confirm")
	public ResponseEntity<?> confirm(@RequestParam int deliveryId, @RequestParam String status){
		Optional<Delivery> deliveryOpt = deliveryRepo.findById(deliveryId);
		if(deliveryOpt.isEmpty()) {
			return ResponseEntity.badRequest().body("FAIL");
		}
		Delivery delivery = deliveryOpt.get();
		if (status.equals("DA_GIAO_HANG")) {
	        delivery.setActualDeliveryTime(LocalDateTime.now());
	    }
		delivery.setStatus(Delivery.DeliveryStatus.valueOf(status));
		deliveryRepo.save(delivery);
		
		return ResponseEntity.ok("Thành công");
	}
	
	@PostMapping("/send-mail")
	public ResponseEntity<?> sendMail(@RequestParam int projectId) {
	    Optional<Project> optionalProject = projectRepo.findById(projectId);
	    
	    if (!optionalProject.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
	    }

	    Project project = optionalProject.get();
	    String[] recipients = {project.getCustomer().getEmail(), project.getUser().getEmail()};
	    String subject = "Giao hàng thành công";

	    try {
	        String body = "Your project has been delivered successfully.";  // Set your email body
	        sendEmail(recipients, subject, body);
	        return ResponseEntity.ok("Email sent successfully to: " + Arrays.toString(recipients));
	    } catch (Exception e) {
	        System.err.println("Error sending email: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
	    }
	}

	public void sendEmail(String[] to, String subject, String body) {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(to);
	    message.setSubject(subject);
	    message.setText(body);  
	    mailSender.send(message);
	}


	
}

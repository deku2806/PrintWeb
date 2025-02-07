package Admin.Controller;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import Admin.Models.*;
import Admin.Repository.*;

@RestController
@RequestMapping("api/print")
public class PrintController {
	@Autowired
	private PrintJobRepository printJobRepo;
	@Autowired
	private ResourceForPrintJobRepository resourceForPrintJobRepo;
	@Autowired
	private DesignRepository designRepo;
	@Autowired
	private BillRepository billRepo;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private ResourcePropertyDetailRepository resourcePropertyDetailRepo;
	@Autowired
	private ProjectRepository projectRepo;

	@PostMapping("/add-to-print")
	public ResponseEntity<?> printJob(@RequestParam int designId) {
		Optional<Design> design = designRepo.findById(designId);
		if (design.isEmpty()) {
			return ResponseEntity.badRequest().body("Thiết kế không tồn tại");
		}
		Optional<PrintJobs> printJobsOpt = printJobRepo.findByDesignId(designId);
		PrintJobs printJobs = printJobsOpt.get();
		printJobs.setStatus(PrintJobs.status.IN_PROGRESS);
		printJobRepo.save(printJobs);
		return ResponseEntity.ok(printJobs);
	}

	@GetMapping("/get-print-job")
	public ResponseEntity<?> getToPrint(@RequestParam int designId) {
		Optional<PrintJobs> printJob = printJobRepo.findByDesignId(designId);
		if (printJob.isEmpty()) {
			return ResponseEntity.badRequest().body("Thiết kế không tồn tại");
		}
		return ResponseEntity.ok(printJob);
	}

	@GetMapping("/get-print-job-detail")
	public ResponseEntity<?> getToPrintDetail(@RequestParam int printJobId) {
		List<ResourceForPrintJob> printJobResources = resourceForPrintJobRepo.findByPrintJobsId(printJobId);

		if (!printJobResources.isEmpty()) {
			return ResponseEntity.ok(printJobResources);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No resources found for the given print job");
		}
	}

	@PostMapping("/update-stock")
	public ResponseEntity<?> updateStock(@RequestBody List<ResourcePropertyDetail> resourceRequests) {
		for (ResourcePropertyDetail resourceRequest : resourceRequests) {
			Optional<ResourcePropertyDetail> resourceOpt = resourcePropertyDetailRepo.findById(resourceRequest.getId());
			if (resourceOpt.isEmpty()) {
				return ResponseEntity.badRequest().body("Resource not found: " + resourceRequest.getId());
			}

			ResourcePropertyDetail resource = resourceOpt.get();
			if ("Máy in".equals(resource.getResourceProperty().getName())) {
				continue;
			}
			if (resource.getQuantity() < resourceRequest.getQuantity()) {
				return ResponseEntity.badRequest().body("Not enough stock for resource: " + resource.getName());
			}

			resource.setQuantity(resource.getQuantity() - resourceRequest.getQuantity());
			resourcePropertyDetailRepo.save(resource);
		}

		return ResponseEntity.ok("Stock updated successfully.");
	}

	@PostMapping("/send-noti")
	public ResponseEntity<?> sendEmail(@RequestParam int projectId) {
		Optional<Project> projectOpt = projectRepo.findById(projectId);
		
		if (projectOpt.isPresent()) {
			Project project = projectOpt.get();
			project.setStatus(Project.status.DA_HOAN_THANH);
			projectRepo.save(project);
			Bill dataBill = billRepo.findByName(project.getName());
			sendBill(project.getCustomer().getEmail(), dataBill);
			return ResponseEntity.ok(dataBill);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Gửi email không thành công");
	}

	private void sendBill(String toEmail, Bill dataBill) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("Đã hoàn thành project");

		StringBuilder emailBody = new StringBuilder();
		emailBody.append("Dear ").append(dataBill.getCustomer().getFullName());
		emailBody.append("Project của bạn đã được hoàn thành. Sẽ được gửi cho bạn trong thời gian tới \n\n");
		emailBody.append("Tên Project: ").append(dataBill.getName()).append("\n");
		emailBody.append("Tổng tiền: ").append(dataBill.getTotalMoney()).append("\n");
		emailBody.append("Ngày đặt: ").append(dataBill.getCreateTime()).append("\n");
		emailBody.append("\nCảm ơn bạn đã sử dụng dịch vụ!\n");

		message.setText(emailBody.toString());

		mailSender.send(message);
	}
	
	@GetMapping("/billByProject")
	public ResponseEntity<?> getBill(@RequestParam String projectName){
		Bill dataBill = billRepo.findByName(projectName);
		if(dataBill==null) {
			return ResponseEntity.badRequest().body("Không tìm thấy bill");
		}
		return ResponseEntity.ok(dataBill);
	}

	@PostMapping("/create-bill")
	public ResponseEntity<?> createBill(@RequestBody Map<String, Object> data) {
		String name = (String) data.get("name");
		String tradingCode = (String) data.get("tradingCode");
		String totalMoneyStr = (String) data.get("totalMoney");
		Customer customer = new ObjectMapper().convertValue(data.get("customer"), Customer.class);
		User employee = new ObjectMapper().convertValue(data.get("employee"), User.class);
		BigDecimal totalMoney = new BigDecimal(totalMoneyStr);
		Bill bill = new Bill();
		bill.setName(name);
		bill.setTotalMoney(totalMoney);
		bill.setStatus(Bill.BillStatus.CHO_THANH_TOAN);
		bill.setCustomer(customer);
		bill.setEmployee(employee);
		bill.setTradingCode(tradingCode);
		billRepo.save(bill);

		return ResponseEntity.ok(bill);
	}

}

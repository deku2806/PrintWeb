package Admin.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Admin.Models.*;
import Admin.Repository.*;

@RestController
@RequestMapping("api/bill")
public class BillController {
	@Autowired 
	private BillRepository billRepo;
	@Autowired 
	private PrintJobRepository printJobsRepo;
	
	@PostMapping("/paid")
	public ResponseEntity<?> update(@RequestParam String projectName){
		
		Bill dataBill = billRepo.findByName(projectName);
		if(dataBill==null) {
			return ResponseEntity.badRequest().body("Không tìm thấy bill");
		}
		dataBill.setStatus(Bill.BillStatus.DA_THANH_TOAN);
		billRepo.save(dataBill);
		return ResponseEntity.ok(dataBill);
	}
	
	@PostMapping("/printComplete")
	public ResponseEntity<?> completed(@RequestParam int designId){
		Optional<PrintJobs> printJobOpt = printJobsRepo.findByDesignId(designId);
		PrintJobs printJobs = printJobOpt.get();
		printJobs.setStatus(PrintJobs.status.COMPLETED);
		printJobsRepo.save(printJobs);
		return ResponseEntity.ok(printJobs);
	}
}

package Admin.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Admin.Models.*;
import Admin.Models.Design.Status;
import Admin.Repository.*;

@RestController
@RequestMapping("api/design")
public class DesignController {
	@Autowired
	private DesignRepository designRepo;

	@Autowired
	private ProjectRepository projectRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private NotificationRepository notificationRepo;

	@Autowired
	private ResourceForPrintJobRepository resourceForPrintJobRepo;

	@Autowired
	private ResourcePropertyDetailRepository resourcePropertyDetailRepo;

	@Autowired
	private PrintJobRepository printJobRepo;

	@GetMapping("/get-by-project")
	private ResponseEntity<?> getAll(@RequestParam int projectId) {
		List<Design> design = designRepo.findByProjectId(projectId);
		return ResponseEntity.ok(design);
	}

	@PostMapping("/complete-print")
	private ResponseEntity<?> completePrint(@RequestBody List<Map<String, Integer>> printJobData) {
		List<Map<String, Object>> responseList = new ArrayList<>();

		for (Map<String, Integer> data : printJobData) {
			Integer detailId = data.get("detailId");
			Integer printJobId = data.get("printJobId");

			Optional<PrintJobs> printJobOpt = printJobRepo.findById(printJobId);
			if (printJobOpt.isEmpty()) {
				return ResponseEntity.badRequest().body("Không tồn tại printJob với id: " + printJobId);
			}

			Optional<ResourcePropertyDetail> detailOpt = resourcePropertyDetailRepo.findById(detailId);
			if (detailOpt.isEmpty()) {
				return ResponseEntity.badRequest().body("Không tìm thấy tài nguyên với id: " + detailId);
			}

			ResourceForPrintJob print = new ResourceForPrintJob();
			print.setPrintJobs(printJobOpt.get());
			print.setResourcePropertyDetail(detailOpt.get());
			resourceForPrintJobRepo.save(print);

			Map<String, Object> responseItem = new HashMap<>();
			responseItem.put("print_job_id", printJobOpt.get().getId());
			responseItem.put("resource_property_detail_id", detailOpt.get().getId());
			responseList.add(responseItem);
		}

		return ResponseEntity.ok(responseList);
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadDesign(@RequestParam String file, @RequestParam int projectId,
			@RequestParam int userId) {

		Optional<Project> projectOpt = projectRepo.findById(projectId);
		Optional<User> userOpt = userRepo.findById(userId);

		if (projectOpt.isPresent() && userOpt.isPresent()) {
			Project project = projectOpt.get();
			User user = userOpt.get();

			Design design = new Design();
			design.setFilePath(file);
			design.setProject(project);
			design.setUser(user);
			design.setStatus(Design.Status.CHO_DUYET);
			design.setApprover_id(projectOpt.get().getUser().getId());

			try {
				designRepo.save(design);
				return ResponseEntity.ok("Design uploaded successfully");
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error saving design: " + e.getMessage());
			}
		} else {
			return ResponseEntity.badRequest().body("Project or User does not exist.");
		}
	}

	@PostMapping("/approve")
	public ResponseEntity<?> approveDesign(@RequestParam int designId, int userId) {

		Design design = designRepo.findById(designId)
				.orElseThrow(() -> new IllegalArgumentException("Design not found"));

		User approver = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Approver not found"));

		if (design.getProject().getUser().getId() != approver.getId()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to approve this design");
		}
		design.setStatus(Status.DA_PHE_DUYET);
		designRepo.save(design);
		PrintJobs printJob = new PrintJobs();
		printJob.setDesign(design);
		printJob.setStatus(PrintJobs.status.PENDING);
		printJobRepo.save(printJob);
		Project project = design.getProject();
		projectRepo.save(project);

		Notification notification = new Notification();
		notification.setUser(design.getUser());
		notification.setContent("Thiết kế của bạn đã được chấp nhận");
		notificationRepo.save(notification);
		return ResponseEntity.ok("Design approved successfully");
	}

	@PostMapping("/reject")
	public ResponseEntity<?> rejectDesign(@RequestParam int designId, int userId) {

		Design design = designRepo.findById(designId)
				.orElseThrow(() -> new IllegalArgumentException("Design not found"));

		User approver = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Approver not found"));

		if (design.getProject().getUser().getId() != approver.getId()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to approve this design");
		}

		design.setStatus(Status.TU_CHOI);
		design.setApprover_id(userId);
		designRepo.save(design);

		Project project = design.getProject();
		projectRepo.save(project);

		Notification notification = new Notification();
		notification.setUser(design.getUser());
		notification.setContent("Thiết kế của bạn bị từ chối");
		notificationRepo.save(notification);
		return ResponseEntity.ok("Design rejected");
	}

}

package Admin.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Admin.Models.*;
import Admin.Repository.*;

@RestController
@RequestMapping("api/project")
public class ProjectController {

	@Autowired
	private ProjectRepository projectRepo;
	
	@GetMapping("/all-project")
	public ResponseEntity<?> allProject() {
		List<Project> allProject = projectRepo.findAll();
		return ResponseEntity.ok(allProject);
	}

	@GetMapping("/project-by-id")
	public ResponseEntity<?> getById(@RequestParam int projectId) {
		Optional<Project> projectOpt = projectRepo.findById(projectId);
		if (projectOpt.isEmpty()) {
			return ResponseEntity.badRequest().body("Project không tồn tại");
		}
		return ResponseEntity.ok(projectOpt.get());
	}

	@GetMapping("/project-by-designId")
	public ResponseEntity<?> getByDesignId(@RequestParam int designId) {
		List<Project> projectOpt = projectRepo.findByDesignId(designId);
		if (projectOpt.isEmpty()) {
			return ResponseEntity.badRequest().body("Project không tồn tại");
		}
		return ResponseEntity.ok(projectOpt);
	}

	@PostMapping("/new-project")
	public ResponseEntity<?> createProject(@RequestBody Project project) {
		try {
			project.setStatus(Project.status.DANG_THIET_KE);
			Project savedProject = projectRepo.save(project);
			return ResponseEntity.ok(savedProject);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/complete")
	public ResponseEntity<?> complete(@RequestParam int projectId) {
		Optional<Project> projectOpt = projectRepo.findById(projectId);
		if(projectOpt.isEmpty()) {
			return ResponseEntity.badRequest().body("Không tìm thấy dự án");
		}
		Project project = projectOpt.get();
		project.setStatus(Project.status.DA_HOAN_THANH);
		projectRepo.save(project);
		return ResponseEntity.ok(project);
	}
}

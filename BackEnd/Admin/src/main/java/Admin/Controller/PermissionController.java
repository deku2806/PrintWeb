package Admin.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Admin.Models.*;
import Admin.Repository.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class PermissionController {
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private PermissionRepository permissionRepo;
	@Autowired
	private UserRepository userRepo;

	@PostMapping("/permission")
	public ResponseEntity<?> changePermissions(@RequestParam int userId, @RequestParam int roleId) {
		Optional<Role> roleOpt = roleRepo.findById(roleId);
	    Optional<User> userOpt = userRepo.findById(userId);
	    
	    if (!roleOpt.isPresent() || !userOpt.isPresent()) {
	        return ResponseEntity.badRequest().body("Invalid user or role ID");
	    }

	    Permission permission = new Permission();
	    permission.setUser(userOpt.get());
	    permission.setRole(roleOpt.get());
	    
	    boolean exists = permissionRepo.existsByUserIdAndRoleId(userId, roleId);
	    if (exists) {
	        return ResponseEntity.ok("Đã tồn tại quyền");
	    }
	    permissionRepo.save(permission);
	    return ResponseEntity.ok("Phân quyền thành công");
	}

	@GetMapping("/roles")
	public ResponseEntity<?> getRoles() {
		List<Role> roles = roleRepo.findAll();
		return ResponseEntity.ok(roles);
	}

	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		List<User> users = userRepo.findAll();
		return ResponseEntity.ok(users);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		userRepo.deleteById(id);
		return ResponseEntity.ok("Xóa thành công");
	}
}

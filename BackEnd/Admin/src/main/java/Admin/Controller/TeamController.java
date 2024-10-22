package Admin.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Admin.Models.*;
import Admin.Repository.*;

@RestController
@RequestMapping("/api/team")
public class TeamController {
	@Autowired
	private TeamRepository teamRepo;
	@Autowired 
	private UserRepository UserRepo;
	
	@GetMapping("/all-team")
	public ResponseEntity<?> allTeam(){
		List<Team> team = teamRepo.findAll();
		return ResponseEntity.ok(team);
	}
	@PostMapping("/add")
	public ResponseEntity<?> addTeam(@RequestBody Team newTeam){
		teamRepo.save(newTeam);
		return ResponseEntity.ok("Thêm thành công");
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateTeam(@RequestParam int updateId,@RequestBody Team newTeam){
		if(teamRepo.findById(updateId).isEmpty()) {
			return ResponseEntity.badRequest().body("Không tồn tại id này");
		}
		Team existingTeam = teamRepo.findById(updateId).get();
		existingTeam.setName(newTeam.getName());
		existingTeam.setDescription(newTeam.getDescription());
		teamRepo.save(existingTeam);
		return ResponseEntity.ok("Sửa thành công");
	}
	
	@DeleteMapping("/remove")
	public ResponseEntity<?> removeTeam(@RequestParam int teamId) {
	    if (!teamRepo.existsById(teamId)) {
	        return ResponseEntity.badRequest().body("Không tồn tại id này");
	    }
	    List<User> users = UserRepo.findByTeam(teamRepo.findById(teamId).get());
	    
	    for (User user : users) {
	        user.setTeam(null); 
	        UserRepo.save(user); 
	    }
	    teamRepo.deleteById(teamId);
	    return ResponseEntity.ok("Xóa thành công");
	}
	
	@PutMapping("/change-manager")
	public ResponseEntity<?> changeManager(@RequestParam int teamId, @RequestParam int userId) {
	    Optional<Team> teamOpt = teamRepo.findById(teamId);
	    Optional<User> userOpt = UserRepo.findById(userId); 
	    
	    if (userOpt.isEmpty() && teamOpt.isEmpty()) {
	        return ResponseEntity.badRequest().body("Không tồn tại phòng ban hoặc tài khoản");
	    }
	    Team team = teamOpt.get();
	    team.setManagerId(userId);
	    return ResponseEntity.ok("Đã thay đổi trưởng phòng");
	}
	
	@PutMapping("/change-team")
	public ResponseEntity<?> changeTeam(@RequestParam int teamId, @RequestParam int userId) {
	    Optional<Team> teamOpt = teamRepo.findById(teamId);
	    Optional<User> userOpt = UserRepo.findById(userId); 

	    if (userOpt.isEmpty() && teamOpt.isEmpty()) {
	        return ResponseEntity.badRequest().body("Không tồn tại phòng ban hoặc tài khoản");
	    }
	    User user = userOpt.get();
	    Team oldTeam = user.getTeam();
	    Team newTeam = teamOpt.get();
	    if (oldTeam != null) {
	        oldTeam.setNumberOfMember(oldTeam.getNumberOfMember() - 1);
	        teamRepo.save(oldTeam); 
	    }
	    user.setTeam(newTeam);
	    UserRepo.save(user);
	    newTeam.setNumberOfMember(newTeam.getNumberOfMember() + 1);
	    return ResponseEntity.ok("Đã thay đổi phòng ban");
	}
	
	@GetMapping("/get-by-id")
	public ResponseEntity<?> getById(@RequestParam int id){
		Team team = teamRepo.findById(id).get();
		return ResponseEntity.ok(team);
	}
	
	
	
}

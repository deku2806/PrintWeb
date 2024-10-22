package Admin.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Admin.Models.Notification;
import Admin.Models.User;
import Admin.Repository.*;

@RestController
@RequestMapping("api/notification")
public class NotificationController {
	@Autowired
	private NotificationRepository notificationRepo;
	@Autowired
	private UserRepository userRepo;

	@GetMapping("/user/{userId}")
	public List<Notification> getUserNotifications(@PathVariable int userId) {
		return notificationRepo.findByUserId(userId);
	}
	
	@PostMapping("/add-noti/{userId}")
	public ResponseEntity<?> addNotification(@PathVariable int userId, @RequestBody Notification dataNoti){
		Optional<User> userOpt = userRepo.findById(userId);
		if(userOpt.isEmpty()) {
			return ResponseEntity.badRequest().body("Không có user này");
		}
		dataNoti.setUser(userOpt.get());
		notificationRepo.save(dataNoti);
		
		return ResponseEntity.ok("Thêm thành công ");
	}
	@PostMapping("/{notificationId}/mark-as-read")
    public void markAsRead(@PathVariable int notificationId) {
        Notification notification = notificationRepo.findById(notificationId).orElseThrow();
        notification.setIsSeen(true); 
        notificationRepo.save(notification);  
    }
	
	@GetMapping("/check-new/{userId}")
    public ResponseEntity<?> checkNewNotifications(@PathVariable int userId) {
        List<Notification> latestNotification = notificationRepo.findLatestNotificationByUserId(userId);
        return ResponseEntity.ok(latestNotification);
    }
}

package Admin.Controller;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import Admin.Models.*;
import Admin.Repository.*;
//import Admin.Security.JwtUtil;
import Admin.Security.JwtUtil;

@RestController
@RequestMapping("api/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ConfirmEmailRepository confirmEmailRepo;
	@Autowired
	private TeamRepository teamRepo;
	@Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(false); 
        User savedUser = userRepo.save(user);
        
        String confirmationCode = UUID.randomUUID().toString();
        ConfirmEmail confirmEmail = new ConfirmEmail();
        confirmEmail.setConfirmCode(confirmationCode);
        confirmEmail.setUser(savedUser);
        confirmEmailRepo.save(confirmEmail);
        sendConfirmationEmail(user.getEmail(), confirmationCode);
        return ResponseEntity.ok("Đăng ký thành công, vui lòng xác nhận email!");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
       
        Optional<User> existingUserOpt = userRepo.findById(id);
        
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setFullName(user.getFullName());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            User updatedUser = userRepo.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            // User not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam String code) {
        Optional<ConfirmEmail> confirmEmailOpt = confirmEmailRepo.findByConfirmCode(code);
        if (confirmEmailOpt.isPresent()) {
            ConfirmEmail confirmEmail = confirmEmailOpt.get();

            
            if (!confirmEmail.getIsConfirm() && confirmEmail.getExpiryTime().isAfter(LocalDateTime.now())) {
                confirmEmail.setIsConfirm(true);
                confirmEmail.getUser().setActive(true); 
                confirmEmailRepo.save(confirmEmail);
                return ResponseEntity.ok("Email confirmed successfully. You can now log in.");
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired confirmation code.");
    }

    private void sendConfirmationEmail(String toEmail, String confirmationCode) {
    	String confirmationUrl = "http://localhost:3000/confirm?code=" + confirmationCode;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Confirmation");
        message.setText("Please confirm your email using the following code: " + confirmationUrl);
        mailSender.send(message);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tài khoản hoặc mật khẩu");
        }

        User user = userOpt.get();	
        if (!user.isActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hãy xác nhận email");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        Map<String, Object> response = new HashMap<>();
        Set<Permission> permissions = user.getPermission();
        List<String> roles = permissions.stream()
                                        .map(permission -> permission.getRole().getRoleName())
                                        .collect(Collectors.toList());
        response.put("token", token);
        response.put("id", user.getId());
        response.put("fullname", user.getFullName());
        response.put("username", user.getUserName());
        response.put("avatar", user.getAvatar());
        response.put("birthday", user.getDateOfBirth());
        response.put("isactive", user.isActive());
        response.put("role", roles);
        response.put("team", user.getTeam().getName());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/forgot-password")
    
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String resetCode = UUID.randomUUID().toString();
            
            ConfirmEmail confirmEmail = new ConfirmEmail();
            confirmEmail.setConfirmCode(resetCode);
            confirmEmail.setUser(user);
            confirmEmail.setExpiryTime(LocalDateTime.now().plusHours(1)); 
            confirmEmail.setIsConfirm(false); 
            confirmEmailRepo.save(confirmEmail);

            sendResetPasswordEmail(email, resetCode);
            return ResponseEntity.ok("Reset password email sent.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String code, @RequestParam String newPassword) {
        Optional<ConfirmEmail> confirmEmailOpt = confirmEmailRepo.findByConfirmCode(code);
        if (confirmEmailOpt.isPresent()) {
            ConfirmEmail confirmEmail = confirmEmailOpt.get();

            if (!confirmEmail.getIsConfirm() && confirmEmail.getExpiryTime().isAfter(LocalDateTime.now())) {
                User user = confirmEmail.getUser();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepo.save(user);
                
                confirmEmail.setIsConfirm(true); 
                confirmEmailRepo.save(confirmEmail);
                
                return ResponseEntity.ok("Password has been reset successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset code.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reset code not found.");
        }
    }

    private void sendResetPasswordEmail(String toEmail, String resetCode) {
        String resetUrl = "http://localhost:3000/resetPassword?code=" + resetCode;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Please reset your password using the following link: " + resetUrl);
        mailSender.send(message);
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam int userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        return ResponseEntity.ok("Password changed successfully.");
    }
    
    @PutMapping("/add-team")
    public ResponseEntity<?> addTeam(@RequestParam int teamId, @RequestParam int userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        Optional<Team> teamOpt = teamRepo.findById(teamId);
        if(userOpt.isEmpty()|| teamOpt.isEmpty()) {
        	return ResponseEntity.badRequest().body("Tài khoản hoặc phòng ban không tồn tại");
        }
        User user = userOpt.get();
        Team team = teamOpt.get();
        user.setTeam(team);
        userRepo.save(user);

        return ResponseEntity.ok("Đã thêm tài khoản vào phòng ban");
    }
    
    @GetMapping("/user-by-id")
    public ResponseEntity<?> getById(@RequestParam int teamId){
    	Optional<Team> teamOpt = teamRepo.findById(teamId);
        if(teamOpt.isEmpty()) {
        	return ResponseEntity.badRequest().body("Phòng ban không tồn tại");
        }
        List<User> user = userRepo.findByTeam(teamOpt.get());
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/getUser")
    public ResponseEntity<?> getByIdUser(@RequestParam(value = "id", required = false, defaultValue = "0") int id){
        User user = userRepo.findById(id).get();
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/manager")
    public ResponseEntity<?> getManager(@RequestParam int teamId){
    	Optional<Team> teamOpt = teamRepo.findById(teamId);

        if (teamOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Phòng ban không tồn tại");
        }

        Team team = teamOpt.get();
        List<User> users = userRepo.findByTeamExcludingManager(team.getId());

        return ResponseEntity.ok(users);
    }
 
    
}

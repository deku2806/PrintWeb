package Admin.Models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name="confirmemail")
public class ConfirmEmail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="code")
	private String confirmCode;
	@Column(name="isconfirm")
	private Boolean isConfirm = false;
	@Column(name="ex_at")
	private LocalDateTime ExpiryTime;
	@Column(name="created_at")
	private LocalDateTime CreateTime;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_confirm"))
	@JsonIgnoreProperties(value = "confirmEmail")
	private User user;
	
	@PrePersist
    protected void onCreate() {
		CreateTime = LocalDateTime.now();
        ExpiryTime = CreateTime.plusMinutes(10);
    }
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConfirmCode() {
		return confirmCode;
	}

	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}

	public Boolean getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(Boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	public LocalDateTime getExpiryTime() {
		return ExpiryTime;
	}

	public void setExpiryTime(LocalDateTime expiryTime) {
		ExpiryTime = expiryTime;
	}

	public LocalDateTime getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		CreateTime = createTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
  
	
}

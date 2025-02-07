package Admin.Models;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;

@Table(name = "user")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "fullname")
	private String fullName;

	@Column(name = "dateofbirth")
	private Date dateOfBirth;

	@Column(name = "avatar")
	private String avatar;

	@Column(name = "email")
	private String email;

	@Column(name = "phonenumber")
	private String phoneNumber;

	@Column(name = "isactive")
	private boolean isActive = false;

	@Column(name = "created_at")
	private LocalDateTime createTime;

	@Column(name = "updated_at")
	private LocalDateTime updateTime;

	@PrePersist
	protected void onCreate() {
		createTime = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updateTime = LocalDateTime.now();
	}

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private Set<ConfirmEmail> confirmEmail;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private Set<Design> design;

	@ManyToOne
	@JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "fk_team_user"))
	@JsonIgnoreProperties("team")
	private Team team;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private Set<Permission> permission;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private Set<Notification> notification;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private Set<Project> project;

	@OneToMany(mappedBy = "employee")
	@JsonIgnore
	private Set<Bill> bill;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private Set<RefreshToken> refreshToken;

	@OneToMany(mappedBy = "employee")
	@JsonIgnore
	private Set<ImportCoupon> importCoupon;
	
	@OneToMany(mappedBy = "employee")
	@JsonIgnore
	private Set<KeyPerformanceIndicators> kpi;
	
	public Set<KeyPerformanceIndicators> getKpi() {
		return kpi;
	}

	public void setKpi(Set<KeyPerformanceIndicators> kpi) {
		this.kpi = kpi;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<RefreshToken> getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(Set<RefreshToken> refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Set<Permission> getPermission() {
		return permission;
	}

	public void setPermission(Set<Permission> permission) {
		this.permission = permission;
	}

	public Set<Design> getDesign() {
		return design;
	}

	public void setDesign(Set<Design> design) {
		this.design = design;
	}

	public Set<ImportCoupon> getImportCoupon() {
		return importCoupon;
	}

	public void setImportCoupon(Set<ImportCoupon> importCoupon) {
		this.importCoupon = importCoupon;
	}

	public Set<Bill> getBill() {
		return bill;
	}

	public void setBill(Set<Bill> bill) {
		this.bill = bill;
	}

	public Set<Notification> getNotification() {
		return notification;
	}

	public void setNotification(Set<Notification> notification) {
		this.notification = notification;
	}

	public Set<Project> getProject() {
		return project;
	}

	public void setProject(Set<Project> project) {
		this.project = project;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Set<ConfirmEmail> getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(Set<ConfirmEmail> confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}

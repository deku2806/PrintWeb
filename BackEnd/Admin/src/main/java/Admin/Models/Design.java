package Admin.Models;

import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;

@Entity
public class Design {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String FilePath;
	private LocalDateTime time;
	@Enumerated(EnumType.STRING)
	private Status status;
	private int approver_id;
	
	@ManyToOne
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project_design"))
    @JsonIgnoreProperties("project")
    private Project project;
	
	@ManyToOne
    @JoinColumn(name = "designer_id", foreignKey = @ForeignKey(name = "fk_user_design"))
    @JsonIgnoreProperties("user")
    private User user;
	
	@OneToMany(mappedBy = "design")
    @JsonIgnore
	private Set<PrintJobs> printJobs;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFilePath() {
		return FilePath;
	}


	public void setFilePath(String filePath) {
		FilePath = filePath;
	}


	public LocalDateTime getTime() {
		return time;
	}


	public void setTime(LocalDateTime time) {
		this.time = time;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public int getApprover_id() {
		return approver_id;
	}


	public void setApprover_id(int approver_id) {
		this.approver_id = approver_id;
	}


	public Project getProject() {
		return project;
	}


	public void setProject(Project project) {
		this.project = project;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Set<PrintJobs> getPrintJobs() {
		return printJobs;
	}


	public void setPrintJobs(Set<PrintJobs> printJobs) {
		this.printJobs = printJobs;
	}

	

	public enum Status{
		CHO_DUYET, DA_PHE_DUYET, TU_CHOI
	}
}

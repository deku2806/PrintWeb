package Admin.Models;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;

@Entity
@Table(name = "print_job")
public class PrintJobs {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Enumerated(EnumType.STRING)
	private status status;

	@ManyToOne
	@JoinColumn(name = "design_id", foreignKey = @ForeignKey(name = "fk_design_printjobs"))
	@JsonIgnoreProperties("design")
	private Design design;

	@OneToMany(mappedBy = "printJobs", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Set<ResourceForPrintJob> resourceForPrintJob;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public status getStatus() {
		return status;
	}

	public void setStatus(status status) {
		this.status = status;
	}

	public Design getDesign() {
		return design;
	}

	public void setDesign(Design design) {
		this.design = design;
	}

	public Set<ResourceForPrintJob> getResourceForPrintJob() {
		return resourceForPrintJob;
	}

	public void setResourceForPrintJob(Set<ResourceForPrintJob> resourceForPrintJob) {
		this.resourceForPrintJob = resourceForPrintJob;
	}

	public enum status {
		PENDING, IN_PROGRESS, COMPLETED, FAILED
	}
}

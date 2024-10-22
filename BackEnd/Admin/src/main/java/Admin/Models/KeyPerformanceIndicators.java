package Admin.Models;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;

@Entity
public class KeyPerformanceIndicators {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String indicatorName; 
	private int target; 
	private int actuallyAchieved; 
	private boolean achieveKPI; 

	@Enumerated(EnumType.STRING)
	private Period period; 

	@ManyToOne
	@JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "fk_employee_kpi"))
	@JsonIgnoreProperties("employee")
	private User employee;

	public enum Period {
		THANG, QUY, NAM
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getActuallyAchieved() {
		return actuallyAchieved;
	}

	public void setActuallyAchieved(int actuallyAchieved) {
		this.actuallyAchieved = actuallyAchieved;
	}

	public boolean isAchieveKPI() {
		return achieveKPI;
	}

	public void setAchieveKPI(boolean achieveKPI) {
		this.achieveKPI = achieveKPI;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}
	
	
}

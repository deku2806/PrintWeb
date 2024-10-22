package Admin.Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
public class ImportCoupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private BigDecimal totalMoney;
    private String tradingCode;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "resource_property_detail_id", foreignKey = @ForeignKey(name = "fk_detail_importcoupon"))
    @JsonIgnoreProperties("resourcePropertyDetail")
    private ResourcePropertyDetail resourcePropertyDetail;
   
    @ManyToOne
    @JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "fk_employee_importcoupon"))
    @JsonIgnoreProperties("employee")
    private User employee;

    @PrePersist
	protected void onCreate() {
		createTime = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updateTime = LocalDateTime.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getTradingCode() {
		return tradingCode;
	}

	public void setTradingCode(String tradingCode) {
		this.tradingCode = tradingCode;
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

	public ResourcePropertyDetail getResourcePropertyDetail() {
		return resourcePropertyDetail;
	}

	public void setResourcePropertyDetail(ResourcePropertyDetail resourcePropertyDetail) {
		this.resourcePropertyDetail = resourcePropertyDetail;
	}

	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}
	
	
}

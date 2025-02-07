package Admin.Models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery")  
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String deliveryAddress;
    private int deliveryId;
    private LocalDateTime estimateDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    @Enumerated(EnumType.STRING)  
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus status;

    @ManyToOne
    @JoinColumn(name = "shipping_method_id", foreignKey = @ForeignKey(name = "fk_shippingmethod_delivery"))
    @JsonIgnoreProperties("shippingMethod")
    private ShippingMethod shippingMethod;

    @ManyToOne
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "fk_customer_delivery"))
    @JsonIgnoreProperties("customer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project_delivery"))
    @JsonIgnoreProperties("project")
    private Project project;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    public void setEstimateDeliveryTime(LocalDateTime estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Project getProject() {
        return project;
    }
	
    public void setProject(Project project) {
        this.project = project;
    }
    
    public enum DeliveryStatus {
        DANG_GIAO, DA_VAN_CHUYEN, DA_GIAO_HANG, TU_CHOI, KHONG_NHAN
    }
    
}

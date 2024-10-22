package Admin.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String requestDescriptionFromCustomer;
    private LocalDateTime startDate;
    private String image;
    private LocalDateTime expectedEndDate;
    private status status;

    @PrePersist
    protected void onCreate() {
        startDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        expectedEndDate = LocalDateTime.now();
    }
    
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<Delivery> deliveries;
    
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<Design> design;

    @ManyToOne
    @JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "fk_project_user"))
    @JsonIgnoreProperties("user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "fk_customer_project"))
    @JsonIgnoreProperties("customer")
    private Customer customer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestDescriptionFromCustomer() {
        return requestDescriptionFromCustomer;
    }

    public void setRequestDescriptionFromCustomer(String requestDescriptionFromCustomer) {
        this.requestDescriptionFromCustomer = requestDescriptionFromCustomer;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public String getFormattedStartDate() {
        return formatDate(startDate);
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getExpectedEndDate() {
        return expectedEndDate;
    }

    public String getFormattedExpectedEndDate() {
        return formatDate(expectedEndDate);
    }

    public void setExpectedEndDate(LocalDateTime expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public status getStatus() { 
        return status;
    }

    public void setStatus(status status) { 
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum status {
        DANG_THIET_KE, DANG_IN, DA_HOAN_THANH
    }

    
    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}

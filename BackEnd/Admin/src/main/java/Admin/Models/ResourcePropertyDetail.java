package Admin.Models;

import java.math.BigDecimal;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "resource_property_detail")
public class ResourcePropertyDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String image;

	@Column(precision = 10, scale = 2)
	private BigDecimal price;

	private int quantity;

	@ManyToOne
	@JoinColumn(name = "property_id", foreignKey = @ForeignKey(name = "fk_detail_property"))
	@JsonIgnoreProperties("resourcePropertyDetail")
	private ResourceProperty resourceProperty;

	@OneToMany(mappedBy = "resourcePropertyDetail")
	@JsonIgnore
	private Set<ResourceForPrintJob> resourceForPrintJob;

	@OneToMany(mappedBy = "resourcePropertyDetail")
	@JsonIgnore
	private Set<ImportCoupon> importCoupon;

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ResourceProperty getResourceProperty() {
		return resourceProperty;
	}

	public void setResourceProperty(ResourceProperty resourceProperty) {
		this.resourceProperty = resourceProperty;
	}

	public Set<ResourceForPrintJob> getResourceForPrintJob() {
		return resourceForPrintJob;
	}

	public void setResourceForPrintJob(Set<ResourceForPrintJob> resourceForPrintJob) {
		this.resourceForPrintJob = resourceForPrintJob;
	}

	public Set<ImportCoupon> getImportCoupon() {
		return importCoupon;
	}

	public void setImportCoupon(Set<ImportCoupon> importCoupon) {
		this.importCoupon = importCoupon;
	}

}

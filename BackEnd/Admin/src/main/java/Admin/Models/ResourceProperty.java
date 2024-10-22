package Admin.Models;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
@Entity
@Table(name="resource_property")
public class ResourceProperty {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private int quantity;
	
	@ManyToOne
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_resource_property"))
    @JsonIgnoreProperties("resourceProperty")
    private Resource resource;
	
	@OneToMany(mappedBy = "resourceProperty", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
	private Set<ResourcePropertyDetail> resourcePropertyDetail;

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Set<ResourcePropertyDetail> getResourcePropertyDetail() {
		return resourcePropertyDetail;
	}

	public void setResourcePropertyDetail(Set<ResourcePropertyDetail> resourcePropertyDetail) {
		this.resourcePropertyDetail = resourcePropertyDetail;
	}
	
	
}

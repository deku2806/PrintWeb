package Admin.Models;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "resources")
public class Resource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String image;
	@Enumerated(EnumType.STRING)
	private Type type;
	private int available_quantity = 0;
	@Enumerated(EnumType.STRING)
	private Status status;
	@OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Set<ResourceProperty> resourceProperty;

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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getAvailable_quantity() {
		return available_quantity;
	}

	public void setAvailable_quantity(int available_quantity) {
		this.available_quantity = available_quantity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Set<ResourceProperty> getResourceProperty() {
		return resourceProperty;
	}

	public void setResourceProperty(Set<ResourceProperty> resourceProperty) {
		this.resourceProperty = resourceProperty;
	}

	public enum Status {
		SAN_SANG_SU_DUNG, CAN_BAO_TRI;
	}

	public enum Type {
		EQUIPMENT, MATERIAL
	}
}

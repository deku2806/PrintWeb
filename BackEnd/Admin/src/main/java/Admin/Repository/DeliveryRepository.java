package Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Admin.Models.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer>{
	Delivery findByProjectId(int projectId);
}

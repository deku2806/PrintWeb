package Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import Admin.Models.*;

@Repository
public interface ResourcePropertyRepository extends JpaRepository<ResourceProperty, Integer>{

}

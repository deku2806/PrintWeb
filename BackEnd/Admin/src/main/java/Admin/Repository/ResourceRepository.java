package Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import Admin.Models.*;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer>{

}

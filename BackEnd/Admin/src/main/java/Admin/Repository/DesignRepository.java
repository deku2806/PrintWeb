package Admin.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import Admin.Models.Design;

@Repository
public interface DesignRepository extends JpaRepository<Design, Integer>{
	List<Design> findByProjectId(int projectId);
}

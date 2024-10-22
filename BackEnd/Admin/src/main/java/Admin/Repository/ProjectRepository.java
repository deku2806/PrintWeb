package Admin.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Admin.Models.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer>{
	List<Project> findByDesignId(int designId);
}

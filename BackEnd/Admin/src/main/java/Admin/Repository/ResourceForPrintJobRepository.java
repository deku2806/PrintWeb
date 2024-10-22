package Admin.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import Admin.Models.*;

@Repository
public interface ResourceForPrintJobRepository extends JpaRepository<ResourceForPrintJob, Integer>{
	List<ResourceForPrintJob> findByPrintJobsId(int printJobId);
}

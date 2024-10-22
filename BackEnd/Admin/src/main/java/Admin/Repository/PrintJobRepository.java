package Admin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Admin.Models.PrintJobs;

@Repository
public interface PrintJobRepository extends JpaRepository<PrintJobs, Integer>{
	Optional<PrintJobs> findByDesignId(int designId);
}

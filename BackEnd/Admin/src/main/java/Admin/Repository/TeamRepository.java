package Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import Admin.Models.*;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer>{
	@Query("SELECT t FROM Team t JOIN t.users u WHERE u.id = :userId")
    Team findTeamByUserId(int userId);
}

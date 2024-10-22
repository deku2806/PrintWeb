package Admin.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import Admin.Models.Team;
import Admin.Models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findByTeam(Team team);
    @Query("SELECT u FROM User u JOIN u.permission p JOIN p.role r WHERE u.team.id = :teamId AND r.roleName = 'Manager'")
    List<User> findByTeamExcludingManager(@Param("teamId") int teamId);
    @Query("SELECT u FROM User u JOIN Team t WHERE u.id = :userId AND t.managerId = :userId")
    List<User> findByExcludingManager(@Param("userId") int userId);
}
	
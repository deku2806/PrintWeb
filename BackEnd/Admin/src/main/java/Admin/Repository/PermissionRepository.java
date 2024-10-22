package Admin.Repository;

import Admin.Models.Permission;
import Admin.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    List<Permission> findByUser(User user);
    List<Permission> findByUserId(Integer userId);
    boolean existsByUserIdAndRoleId(int userId, int roleId);
}
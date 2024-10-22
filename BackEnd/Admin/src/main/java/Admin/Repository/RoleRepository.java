package Admin.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Admin.Models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	Role findByRoleCode(String roleCode);
}

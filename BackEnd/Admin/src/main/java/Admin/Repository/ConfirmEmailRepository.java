package Admin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Admin.Models.ConfirmEmail;

@Repository
public interface ConfirmEmailRepository extends JpaRepository<ConfirmEmail, Integer>{
	Optional<ConfirmEmail> findByConfirmCode(String confirmCode);
	
}

package Admin.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import Admin.Models.*;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	List<Notification> findByUserId(int userId);

	@Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createTime DESC")
	List<Notification> findLatestNotificationByUserId(int userId);
}

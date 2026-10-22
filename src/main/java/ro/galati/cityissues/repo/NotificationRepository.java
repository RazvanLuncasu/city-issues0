package ro.galati.cityissues.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.galati.cityissues.model.Notification;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientEmailOrderByCreatedAtDesc(String email);
}
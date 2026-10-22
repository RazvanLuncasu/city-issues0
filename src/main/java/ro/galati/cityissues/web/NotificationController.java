package ro.galati.cityissues.web;

import org.springframework.web.bind.annotation.*;
import ro.galati.cityissues.model.Notification;
import ro.galati.cityissues.repo.NotificationRepository;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Notification> list(Principal principal) {
        // Returnează doar notificările adresate utilizatorului logat
        return repo.findByRecipientEmailOrderByCreatedAtDesc(principal.getName());
    }

    @PatchMapping("/{id}/seen")
    public void markSeen(@PathVariable Long id) {
        Notification n = repo.findById(id).orElseThrow();
        n.setSeen(true);
        repo.save(n);
    }
}
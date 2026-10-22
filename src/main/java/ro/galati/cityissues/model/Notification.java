package ro.galati.cityissues.model;
import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private boolean seen = false;
    private Instant createdAt = Instant.now();
    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User recipient;

    public Notification() {}
    public Notification(String message, User recipient) { this.message = message; this.recipient = recipient; }
    // Getters/Setters
    public Long getId() { return id; }
    public String getMessage() { return message; }
    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }
    public User getRecipient() { return recipient; }
    public Instant getCreatedAt() { return createdAt; }
}
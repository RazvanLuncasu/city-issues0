package ro.galati.cityissues.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ro.galati.cityissues.dto.SignupRequest;
import ro.galati.cityissues.model.Role;
import ro.galati.cityissues.model.User;
import ro.galati.cityissues.repo.UserRepository;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignupRequest req) {
        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email deja utilizat!"));
        }
        User user = new User();
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        user.setPassword(encoder.encode(req.getPassword())); // Criptăm parola!
        user.setRole(Role.ROLE_CITIZEN); // Forțăm rolul de cetățean
        userRepo.save(user);
        return ResponseEntity.ok(Map.of("message", "Cont creat!"));
    }
}
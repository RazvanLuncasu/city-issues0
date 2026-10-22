package ro.galati.cityissues;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.galati.cityissues.model.Role;
import ro.galati.cityissues.model.User;
import ro.galati.cityissues.repo.UserRepository;

@SpringBootApplication
public class CityIssuesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityIssuesApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepo, PasswordEncoder encoder) {
		return args -> {
			// 1. Creăm Operatorul (dacă nu există)
			if (userRepo.findByEmail("admin@galati.ro").isEmpty()) {
				User admin = new User();
				admin.setEmail("admin@galati.ro");
				admin.setFullName("Operator Primărie");
				admin.setPassword(encoder.encode("admin123"));
				admin.setRole(Role.valueOf("ROLE_OPERATOR"));
				userRepo.save(admin);
			}

			// 2. Creăm Cetățeanul 1 (Ion)
			if (userRepo.findByEmail("ion@gmail.com").isEmpty()) {
				User u1 = new User();
				u1.setEmail("ion@gmail.com");
				u1.setFullName("Ion Popescu");
				u1.setPassword(encoder.encode("parola1"));
				u1.setRole(Role.valueOf("ROLE_CITIZEN"));
				userRepo.save(u1);
			}

			// 3. Creăm Cetățeanul 2 (Maria)
			if (userRepo.findByEmail("maria@gmail.com").isEmpty()) {
				User u2 = new User();
				u2.setEmail("maria@gmail.com");
				u2.setFullName("Maria Ionescu");
				u2.setPassword(encoder.encode("parola2"));
				u2.setRole(Role.valueOf("ROLE_CITIZEN"));
				userRepo.save(u2);
			}

			// 4. Creăm Cetățeanul 3 (Ștefan) - NOU
			if (userRepo.findByEmail("stefan@gmail.com").isEmpty()) {
				User u3 = new User();
				u3.setEmail("stefan@gmail.com");
				u3.setFullName("Ștefan Vasilescu");
				u3.setPassword(encoder.encode("parola3"));
				u3.setRole(Role.valueOf("ROLE_CITIZEN"));
				userRepo.save(u3);
			}

			System.out.println(">>> Baza de date populată cu: 1 Operator și 3 Cetățeni!");
		};
	}
}
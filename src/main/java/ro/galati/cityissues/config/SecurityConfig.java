package ro.galati.cityissues.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Dezactivat pentru dezvoltare
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // Permite consola H2
                .authorizeHttpRequests(auth -> auth
                        // 1. Resurse Publice (Pagini statice, CSS, JS și H2 Console)
                        .requestMatchers("/", "/index.html", "/login.html", "/signup.html", "/operator.html", "/h2-console/**", "/*.css", "/*.js").permitAll()

                        // 2. Endpoint-uri de Autentificare (permitem înregistrarea publică)
                        .requestMatchers("/api/auth/**").permitAll()

                        // 3. Reguli pentru Sesizări (Reports)
                        .requestMatchers(HttpMethod.GET, "/api/reports/**").permitAll() // Oricine poate vedea lista
                        .requestMatchers(HttpMethod.POST, "/api/reports").hasRole("CITIZEN") // Doar cetățenii logați pot raporta

                        // 4. Reguli pentru Notificări
                        .requestMatchers("/api/notifications/**").authenticated() // Trebuie să fii logat pentru notificări

                        // 5. Acțiuni specifice Operatorului (Primărie)
                        .requestMatchers(HttpMethod.PATCH, "/api/reports/*/status").hasRole("OPERATOR") //
                        .requestMatchers(HttpMethod.PATCH, "/api/reports/*/assign").hasRole("OPERATOR") //
                        .requestMatchers(HttpMethod.PATCH, "/api/reports/*/response").hasRole("OPERATOR")

                        // Orice altă cerere necesită autentificare
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // Folosim Basic Auth

        return http.build();
    }
}
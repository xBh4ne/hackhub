package unicam.dreamteam.infrastructure.config;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloStaff;
import unicam.dreamteam.domain.repository.StaffRepository;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    // TODO: Rimuovere. Usare solo a scopo di sviluppo.
    @Override
    public void run(@NonNull ApplicationArguments args) {
        staffAccount("admin", "admin@hack-hub.it", "admin", RuoloStaff.ADMIN);
    }

    private void staffAccount(String username, String email, String password, RuoloStaff ruolo) {
        if (staffRepository.existsByUsername(username)) return;

        Staff admin = new Staff(
                username,
                email,
                passwordEncoder.encode(password),
                ruolo
        );

        staffRepository.save(admin);
        System.out.printf(
                "%s creato: %s, %s%n",
                ruolo.getName(),
                username,
                password
        );
    }
}

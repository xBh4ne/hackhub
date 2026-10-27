package unicam.dreamteam.domain.service.accounts;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicam.dreamteam.domain.model.entity.users.Utente;
import unicam.dreamteam.domain.repository.UtenteRepository;

import java.util.Optional;


@Service
@AllArgsConstructor
public class UtenteService {
    private final UtenteRepository utenteRepository;

    public Utente getByUsername(String username) {
        Optional<Utente> utente = utenteRepository.findByUsername(username);

        if(utente.isEmpty()) throw new EntityNotFoundException(
                String.format(
                        "Utente.username=%s",
                        username
                )
        );

        return utente.get();
    }

    public Utente getById(Long id) {
        Optional<Utente> utente = utenteRepository.findById(id);

        if(utente.isEmpty()) throw new EntityNotFoundException(
                String.format(
                        "Utente.id=%s",
                        id
                )
        );

        return utente.get();
    }

    public Utente save(Utente utente) {
        return this.utenteRepository.save(utente);
    }
}

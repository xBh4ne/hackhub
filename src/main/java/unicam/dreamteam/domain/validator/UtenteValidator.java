package unicam.dreamteam.domain.validator;

import unicam.dreamteam.domain.model.entity.users.Utente;
import unicam.dreamteam.domain.exception.team.TeamException;

import org.springframework.stereotype.Component;

@Component
public class UtenteValidator {
    public void validaInTeam(Utente utente) {
        if (!utente.hasTeam()) throw new TeamException(
                String.format(
                        "Utente(username=%s) non fa parte di un team.", utente.getUsername()
                )
        );
    }

    public void validaNotInTeam(Utente utente) {
        if (utente.hasTeam()) throw new TeamException(
                String.format(
                        "Utente(username=%s) fa già parte di un team.", utente.getUsername()
                )
        );
    }
}

package unicam.dreamteam.domain.exception.ruolo;

import unicam.dreamteam.domain.model.entity.users.ruolo.Ruolo;

public class RuoloException extends RuntimeException {
    public RuoloException(Ruolo ruolo, Ruolo ruoloAtteso) {
        super(String.format(
                "Ruolo ricevuto errato. ricevuto=%s atteso=%s",
                ruolo.getName(),
                ruoloAtteso.getName()
        ));
    }
}
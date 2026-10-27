package unicam.dreamteam.domain.model.entity.state.hackathon;


import unicam.dreamteam.domain.exception.hackathon.HackathonException;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.entity.sottomissione.Sottomissione;
import unicam.dreamteam.domain.model.entity.sottomissione.Valutazione;

public interface StatoHackathon {
    default void iscrivi(Hackathon hackathon, Team team) {
        throw new HackathonException("Iscrizioni non aperte");
    }
    default void avvia(Hackathon hackathon) {
        throw new HackathonException("Non è possibile avviare l'hackathon in questo stato.");
    }
    default void inviaSottomissione(Hackathon hackathon, Sottomissione sottomissione) {
        throw new HackathonException("Hackathon non in corso. ");
    }
    default void proclamaVincitore(Hackathon hackathon, Team team) {
        throw new HackathonException("Hackathon non in fase di proclamazione.");
    }

    default void avviaValutazione(Hackathon hackathon) {
        throw new HackathonException("Non è possibile avviare la valutazione in questo stato.");
    }

    default void valuta(Hackathon hackathon, Sottomissione sottomissione, Valutazione valutazione) {
        throw new HackathonException("Hackathon non in fase di valutazione");
    }

    default void concludiValutazione(Hackathon hackathon) {
        throw new HackathonException("Non è possibile avviare la valutazione in questo stato.");
    }

    default void concludi(Hackathon hackathon) {
        throw new HackathonException("Operazione non permessa in questo stato");
    }
    default void apriIscrizioni(Hackathon hackathon) {
        throw new HackathonException("Iscrizioni già aperte oppure operazione non permessa in questo stato.");
    }


    StatoHackathon prossimoStato();
    String getNome();

    default boolean equals(StatoHackathon stato) {
        return getNome().equals(stato.getNome());
    }
}

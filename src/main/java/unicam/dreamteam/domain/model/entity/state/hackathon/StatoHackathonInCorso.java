package unicam.dreamteam.domain.model.entity.state.hackathon;

import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.sottomissione.Sottomissione;

public class StatoHackathonInCorso implements StatoHackathon {
        @Override
    public void inviaSottomissione(Hackathon hackathon, Sottomissione sottomissione) {
        hackathon.getSottomissioni().add(sottomissione);
    }

    @Override
    public void avviaValutazione(Hackathon hackathon) {
        hackathon.setStato(prossimoStato());
    }

    @Override
    public StatoHackathon prossimoStato() { return new StatoHackathonValutazione(); }

    @Override
    public String getNome() { return "IN_CORSO"; }
}

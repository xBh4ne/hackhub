package unicam.dreamteam.domain.model.entity.state.hackathon;

import unicam.dreamteam.domain.exception.hackathon.HackathonException;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.sottomissione.Sottomissione;
import unicam.dreamteam.domain.model.entity.sottomissione.Valutazione;

public class StatoHackathonValutazione implements StatoHackathon {

    @Override
    public void valuta(Hackathon hackathon, Sottomissione sottomissione, Valutazione valutazione) {
        sottomissione.setValutazione(valutazione);
    }

    @Override
    public void concludiValutazione(Hackathon hackathon) {
        boolean tutteValutate = hackathon.getSottomissioni().stream()
                .allMatch(s -> s.getValutazione() != null);
        if (!tutteValutate) throw new HackathonException("Non tutte le sottomissioni sono state valutate");
        hackathon.setStato(prossimoStato());
    }

    @Override
    public StatoHackathon prossimoStato() { return new StatoHackathonProclamazioneVincitore(); }

    @Override
    public String getNome() { return "VALUTAZIONE"; }
}

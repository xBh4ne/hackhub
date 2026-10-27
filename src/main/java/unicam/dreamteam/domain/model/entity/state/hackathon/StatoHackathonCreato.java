package unicam.dreamteam.domain.model.entity.state.hackathon;

import unicam.dreamteam.domain.model.entity.Hackathon;

public class StatoHackathonCreato implements StatoHackathon {
    @Override
    public StatoHackathon prossimoStato() {
        return new StatoHackathonIscrizione();
    }

    @Override
    public void apriIscrizioni(Hackathon hackathon) {
        hackathon.setStato(prossimoStato());
    }

    @Override
    public String getNome() {
        return "CREATO";
    }
}

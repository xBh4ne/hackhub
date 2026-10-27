package unicam.dreamteam.domain.model.entity.state.hackathon;

import unicam.dreamteam.domain.exception.hackathon.HackathonException;

public class StatoHackathonConcluso implements StatoHackathon {
    @Override
    public StatoHackathon prossimoStato() {
        throw new HackathonException("L'hackathon è già concluso");
    }

    @Override
    public String getNome() {
        return "CONCLUSO";
    }
}

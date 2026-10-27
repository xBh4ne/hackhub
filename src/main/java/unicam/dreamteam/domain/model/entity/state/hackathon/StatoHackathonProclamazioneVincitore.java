package unicam.dreamteam.domain.model.entity.state.hackathon;

import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Team;

public class StatoHackathonProclamazioneVincitore implements StatoHackathon {

    @Override
    public void proclamaVincitore(Hackathon hackathon, Team team) {
        hackathon.setTeamVincitore(team);
        concludi(hackathon);
    }

    @Override
    public void concludi(Hackathon hackathon) {
        hackathon.setStato(prossimoStato());
    }

    @Override
    public StatoHackathon prossimoStato() {
        return new StatoHackathonConcluso();
    }

    @Override
    public String getNome() { return "PROCLAMAZIONE_VINCITORE"; }
}

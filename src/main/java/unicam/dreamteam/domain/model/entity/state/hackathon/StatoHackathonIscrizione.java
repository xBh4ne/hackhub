package unicam.dreamteam.domain.model.entity.state.hackathon;

import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Team;

public class StatoHackathonIscrizione implements StatoHackathon {
    @Override
    public void iscrivi(Hackathon hackathon, Team team) {
        hackathon.getTeamIscritti().add(team);
        team.getHackathons().add(hackathon);
    }

    @Override
    public void avvia(Hackathon hackathon) {
        hackathon.setStato(prossimoStato());
    }

    @Override
    public StatoHackathon prossimoStato() { return new StatoHackathonInCorso(); }

    @Override
    public String getNome() { return "ISCRIZIONE"; }
}

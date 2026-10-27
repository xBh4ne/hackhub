package unicam.dreamteam.domain.validator;

import org.springframework.stereotype.Component;
import unicam.dreamteam.domain.exception.hackathon.HackathonException;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.entity.sottomissione.Sottomissione;
import unicam.dreamteam.domain.model.entity.state.hackathon.StatoHackathonValutazione;
import unicam.dreamteam.domain.model.entity.users.Staff;

@Component
public class HackathonValidator {
    public void validaTeamIscrittoHackathon(Hackathon hackathon, Team team) {
        if (hackathon.getTeamIscritti().contains(team)) return;
        throw new HackathonException("Il team non è iscritto a questo hackathon");
    }

    public void validaTeamHaInviatoSottomissione(Hackathon hackathon, Team team) {
        if (hackathon.hasSottomissione(team)) return;

        throw new HackathonException(
                String.format(
                        "Il team(id=%s, nome=%s) non ha ancora inviato una sottomissione. (Hackathon.id=%s, Hackathon.nome=%s)",
                        team.getId(),team.getNome(),
                        hackathon.getId(), hackathon.getNome()
                )
        );
    }

    public void validaTeamNonHaInviatoSottomissione(Hackathon hackathon, Team team) {
        if (!hackathon.hasSottomissione(team)) return;

        throw new HackathonException(
                String.format(
                        "Il team(id=%s, nome=%s) ha già inviato una sottomissione. (Hackathon.id=%s, Hackathon.nome=%s)",
                        team.getId(),team.getNome(),
                        hackathon.getId(), hackathon.getNome()
                )
        );
    }

    public void validaHackathonNonHaMentore(Hackathon hackathon, Staff mentore) {
        if (!hackathon.getMentori().contains(mentore)) return;

        throw new HackathonException(
                String.format(
                        "Mentore è già assegnato all'Hackathon(id=%s, nome=%s)",
                        hackathon.getId(),
                        hackathon.getNome()
                )
        );
    }

    public void validaHackathonHaMentore(Hackathon hackathon, Staff mentore) {
        if (hackathon.getMentori().contains(mentore)) return;

        throw new HackathonException(
                String.format(
                        "Mentore è già assegnato all'Hackathon(id=%s, nome=%s)",
                        hackathon.getId(),
                        hackathon.getNome()
                )
        );
    }

    public void validaTutteSottomissioniValutate(Hackathon hackathon) {
        if (hackathon.getSottomissioni().stream().allMatch(Sottomissione::isValutata)) return;
        throw new HackathonException("Non tutte le sottomissioni sono valutate.");
    }

    public void validaHackathonGiudicatoDa(Hackathon hackathon, Staff giudice) {
        if(hackathon.getGiudice().equals(giudice)) return;
        throw new HackathonException(
                String.format(
                    "Hackathon(nome=%s) non è assegnato al giudice(username=%s).",
                    hackathon.getNome(),
                    giudice.getUsername()
                )
        );
    }

    public void validaHackathonOrganizzatoDa(Hackathon hackathon, Staff organizzatore) {
        if(hackathon.getOrganizzatore().equals(organizzatore)) return;
        throw new HackathonException(
                String.format(
                        "L'organizzatore(username=%s) non gestisce questo Hackathon(nome=%s).",
                        organizzatore.getUsername(),
                        hackathon.getNome()
                )
        );
    }

    public void validaHackathonAssegnatoA(Hackathon hackathon, Staff mentore) {
        if(hackathon.getMentori().contains(mentore)) return;
        throw new HackathonException(
                String.format(
                        "L'mentore(username=%s) non è assegnato a questo Hackathon(nome=%s).",
                        mentore.getUsername(),
                        hackathon.getNome()
                )
        );
    }

    public void validaHackathonInValutazione(Hackathon hackathon) {
        if (hackathon.getStato().equals(new StatoHackathonValutazione())) return;

        throw new HackathonException(
                String.format(
                        "Hackathon(nome=%s) non è in valutazione.",
                        hackathon.getNome()
                )
        );
    }
}

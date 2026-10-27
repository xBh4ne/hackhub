package unicam.dreamteam.domain.validator;

import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloStaff;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloUtente;
import unicam.dreamteam.domain.exception.ruolo.RuoloException;

import org.springframework.stereotype.Component;

@Component
public class RuoloValidator {

    public void validaAdmin(RuoloStaff ruolo) {
        RuoloStaff ruoloAtteso = RuoloStaff.ADMIN;
        if (ruolo == ruoloAtteso) return;

        throw new RuoloException(ruolo, ruoloAtteso);
    }

    public void validaOrganizzatore(RuoloStaff ruolo) {
        RuoloStaff ruoloAtteso = RuoloStaff.ORGANIZZATORE;
        if (ruolo == ruoloAtteso) return;

        throw new RuoloException(ruolo, ruoloAtteso);
    }

    public void validaGiudice(RuoloStaff ruolo) {
        RuoloStaff ruoloAtteso = RuoloStaff.GIUDICE;
        if (ruolo == ruoloAtteso) return;

        throw new RuoloException(ruolo, ruoloAtteso);
    }

    public void validaMentore(RuoloStaff ruolo) {
        RuoloStaff ruoloAtteso = RuoloStaff.MENTORE;
        if (ruolo == ruoloAtteso) return;

        throw new RuoloException(ruolo, ruoloAtteso);
    }

    public void validaUtente(RuoloUtente ruolo) {
        RuoloUtente ruoloAtteso = RuoloUtente.UTENTE;
        if (ruolo == ruoloAtteso) return;

        throw new RuoloException(ruolo, ruoloAtteso);
    }

    public void validaTeamLeader(RuoloUtente ruolo) {
        RuoloUtente ruoloAtteso = RuoloUtente.TEAM_LEADER;
        if (ruolo == ruoloAtteso) return;

        throw new RuoloException(ruolo, ruoloAtteso);
    }

    public void validaTeamMember(RuoloUtente ruolo) {
        RuoloUtente ruoloAtteso = RuoloUtente.TEAM_MEMBER;
        if (ruolo == ruoloAtteso) return;

        throw new RuoloException(ruolo, ruoloAtteso);
    }
}

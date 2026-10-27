package unicam.dreamteam.domain.service.facade;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import unicam.dreamteam.domain.adapter.CalendarAdapter;
import unicam.dreamteam.domain.exception.hackathon.HackathonException;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.RichiestaSupporto;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.model.entity.users.Utente;
import unicam.dreamteam.domain.service.accounts.StaffService;
import unicam.dreamteam.domain.service.accounts.UtenteService;
import unicam.dreamteam.domain.service.hackathon.HackathonService;
import unicam.dreamteam.domain.service.team.RichiestaSupportoService;
import unicam.dreamteam.domain.validator.HackathonValidator;
import unicam.dreamteam.domain.validator.StaffValidator;
import unicam.dreamteam.domain.validator.UtenteValidator;
import unicam.dreamteam.presentation.dto.team.richiesteSupporto.RichiestaSupportoDTO;
import unicam.dreamteam.presentation.mapper.RichiestaSupportoMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RichiestaSupportoFacade {
    private final RichiestaSupportoService richiestaSupportoService;
    private final HackathonService hackathonService;
    private final StaffService staffService;
    private final UtenteService utenteService;
    private final CalendarAdapter calendarAdapter;

    private final UtenteValidator utenteValidator;
    private final StaffValidator staffValidator;
    private final HackathonValidator hackathonValidator;

    private final RichiestaSupportoMapper richiestaSupportoMapper;

    public RichiestaSupportoDTO richiediSupporto(Long hackathonId, Long mentoreId, String username, RichiestaSupportoDTO request) {
        Utente utente = utenteService.getByUsername(username);
        utenteValidator.validaInTeam(utente);

        Hackathon hackathon = hackathonService.getById(hackathonId);
        Staff mentore = staffService.getById(mentoreId);
        staffValidator.validaMentore(mentore);

        this.hackathonValidator.validaHackathonAssegnatoA(hackathon, mentore);

        RichiestaSupporto richiesta = richiestaSupportoService.crea(
                request.getOggetto(),
                request.getDescrizione(),
                utente.getTeam(),
                mentore
        );
        return richiestaSupportoMapper.toSimpleDTO(richiesta);
    }

    public List<RichiestaSupportoDTO> getByMentoreUsername(String username) {
        Staff mentore = staffService.getByUsername(username);
        staffValidator.validaMentore(mentore);

        return richiestaSupportoService.getAllByMentore(mentore).stream()
                .map(richiestaSupportoMapper::toSimpleDTO)
                .toList();
    }

    public List<RichiestaSupportoDTO> getByTeamUsername(String username) {
        Utente utente = utenteService.getByUsername(username);
        utenteValidator.validaInTeam(utente);

        return richiestaSupportoService.getAllByTeam(utente.getTeam()).stream()
                .map(richiestaSupportoMapper::toSimpleDTO)
                .toList();
    }

    public RichiestaSupportoDTO prenotaCall(Long richiestaId, LocalDateTime dataOra, String username) {
        Staff mentore = staffService.getByUsername(username);
        staffValidator.validaMentore(mentore);

        RichiestaSupporto richiesta = richiestaSupportoService.getById(richiestaId);

        if (!richiesta.getMentore().equals(mentore)) throw new AccessDeniedException("Non sei il mentore di questa richiesta.");

        calendarAdapter.prenotaSlot(richiesta, dataOra);
        return richiestaSupportoMapper.toDTO(richiesta);
    }
}

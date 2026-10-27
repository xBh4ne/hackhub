package unicam.dreamteam.domain.service.facade;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicam.dreamteam.domain.exception.hackathon.HackathonException;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloStaff;
import unicam.dreamteam.domain.service.accounts.StaffService;
import unicam.dreamteam.domain.service.hackathon.HackathonService;
import unicam.dreamteam.domain.service.team.SegnalazioneService;
import unicam.dreamteam.domain.service.team.TeamService;
import unicam.dreamteam.domain.validator.HackathonValidator;
import unicam.dreamteam.domain.validator.StaffValidator;
import unicam.dreamteam.presentation.dto.team.richiesteSupporto.SegnalazioneDTO;
import unicam.dreamteam.presentation.mapper.SegnalazioneMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class SegnalazioneFacade {
    private final SegnalazioneService segnalazioneService;
    private final HackathonService hackathonService;
    private final TeamService teamService;
    private final StaffService staffService;

    private final StaffValidator staffValidator;
    private final HackathonValidator hackathonValidator;
    private final SegnalazioneMapper segnalazioneMapper;

    public SegnalazioneDTO segnalaTeam(Long hackathonId, Long teamId, String descrizione, String username) {
        Staff mentore = staffService.getByUsername(username);
        staffValidator.validaMentore(mentore);

        Hackathon hackathon = hackathonService.getById(hackathonId);
        Team team = teamService.getById(teamId);

        this.hackathonValidator.validaHackathonAssegnatoA(hackathon, mentore);
        this.hackathonValidator.validaTeamIscrittoHackathon(hackathon, team);

        return segnalazioneMapper.toDTO(segnalazioneService.segnala(descrizione, mentore, team, hackathon));
    }

    public List<SegnalazioneDTO> getAll() {
        return segnalazioneService.getAll().stream()
                .map(segnalazioneMapper::toSimpleDTO)
                .toList();
    }

    public List<SegnalazioneDTO> getByTeam(Long teamId) {
        return segnalazioneService.getByTeam(teamId).stream()
                .map(segnalazioneMapper::toSimpleDTO)
                .toList();
    }

    public List<SegnalazioneDTO> getAllByStaffUsername(String username) {
        Staff staff = this.staffService.getByUsername(username);

        if (staff.getRuolo() == RuoloStaff.ADMIN) return getAll();
        if (staff.getRuolo() == RuoloStaff.ORGANIZZATORE) return getAllByOrganizzatore(staff);

        return List.of();
    }

    public List<SegnalazioneDTO> getAllByOrganizzatore(Staff organizzatore) {
        this.staffValidator.validaOrganizzatore(organizzatore);
        return this.segnalazioneService.getByOrganizzatore(organizzatore).stream()
                .map(this.segnalazioneMapper::toSimpleDTO)
                .toList();
    }
}

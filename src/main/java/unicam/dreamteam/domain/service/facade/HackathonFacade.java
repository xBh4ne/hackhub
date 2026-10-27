package unicam.dreamteam.domain.service.facade;

import unicam.dreamteam.domain.adapter.PagamentoAdapter;
import unicam.dreamteam.domain.exception.hackathon.HackathonException;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.entity.state.hackathon.StatoHackathonConcluso;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.model.entity.users.Utente;
import unicam.dreamteam.domain.service.accounts.StaffService;
import unicam.dreamteam.domain.service.accounts.UtenteService;
import unicam.dreamteam.domain.service.hackathon.HackathonService;
import unicam.dreamteam.domain.model.entity.users.Autenticabile;
import unicam.dreamteam.domain.service.security.SecurityService;
import unicam.dreamteam.domain.service.team.TeamService;
import unicam.dreamteam.domain.validator.HackathonValidator;
import unicam.dreamteam.domain.validator.StaffValidator;
import unicam.dreamteam.domain.validator.UtenteValidator;
import unicam.dreamteam.presentation.dto.hackathon.HackathonDTO;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicam.dreamteam.presentation.dto.team.TeamDTO;
import unicam.dreamteam.presentation.mapper.HackathonMapper;
import unicam.dreamteam.presentation.mapper.TeamMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class HackathonFacade {
    // Service
    private final SecurityService securityService;
    private final HackathonService hackathonService;
    private final TeamService teamService;
    private final UtenteService utenteService;
    private final StaffService staffService;

    // Mapper
    private final HackathonMapper hackathonMapper;
    private final TeamMapper teamMapper;

    // Validator
    private final HackathonValidator hackathonValidator;
    private final StaffValidator staffValidator;
    private final UtenteValidator utenteValidator;

    private final PagamentoAdapter pagamentoAdapter;

    public List<HackathonDTO> listaHackathon() {
        return this.hackathonService.getAll().stream()
                .map(this.hackathonMapper::toSimpleDTO)
                .toList();
    }

    public List<HackathonDTO> listaHackathonByUsername(String username) {
        Autenticabile account = this.securityService.getAccountByUsername(username);

        if (account instanceof Utente utente) return listaHackathonByUtente(utente);
        if (account instanceof Staff staff) return listaHackathonByStaff(staff);

        return List.of();
    }

    private List<HackathonDTO> listaHackathonByUtente(Utente utente) {
        this.utenteValidator.validaInTeam(utente);
        return this.hackathonService.getAllByTeam(utente.getTeam()).stream()
                .map(this.hackathonMapper::toDTO)
                .toList();
    }

    public List<HackathonDTO> listaHackathonByStaffUsername(String username) {
        Staff staff = this.staffService.getByUsername(username);
        return listaHackathonByStaff(staff);
    }

    public List<HackathonDTO> listaHackathonByStaff(Staff staff) {
        return this.hackathonService.getAllByStaff(staff).stream()
                .map(this.hackathonMapper::toDTO)
                .toList();
    }

    public HackathonDTO creaHackathon(HackathonDTO hackathonDTO, String username) {
        Staff currentUser = this.staffService.getByUsername(username);
        Staff giudice = this.staffService.getById(hackathonDTO.getGiudiceId());

        this.staffValidator.validaOrganizzatore(currentUser);
        this.staffValidator.validaGiudice(giudice);
        return this.hackathonMapper.toDTO(
                this.hackathonService.crea(hackathonDTO, currentUser, giudice)
        );
    }

    public HackathonDTO apriIscrizioni(Long hackathonId, String username) {
        Staff staff = this.staffService.getByUsername(username);
        Hackathon hackathon = this.hackathonService.getById(hackathonId);

        this.hackathonValidator.validaHackathonOrganizzatoDa(hackathon, staff);

        hackathon.apriIscrizioni();
        return this.hackathonMapper.toDTO(
                this.hackathonService.save(hackathon)
        );
    }


    public HackathonDTO avviaHackathon(Long hackathonId, String username) {
        Staff staff = this.staffService.getByUsername(username);
        Hackathon hackathon = this.hackathonService.getById(hackathonId);

        this.hackathonValidator.validaHackathonOrganizzatoDa(hackathon, staff);

        hackathon.avvia();
        return this.hackathonMapper.toDTO(
                this.hackathonService.save(hackathon)
        );
    }

    public HackathonDTO aggiungiMentore(Long hackathonId, Long mentoreId, String username) {
        Staff staff = this.staffService.getByUsername(username);
        Staff mentore = this.staffService.getById(mentoreId);
        Hackathon hackathon = this.hackathonService.getById(hackathonId);

        this.hackathonValidator.validaHackathonOrganizzatoDa(hackathon, staff);

        this.hackathonValidator.validaHackathonNonHaMentore(hackathon, mentore);
        return this.hackathonMapper.toDTO(
                this.hackathonService.aggiungiMentore(hackathon, mentore)
        );
    }

    public HackathonDTO rimuoviMentore(Long hackathonId, Long mentoreId, String username) {
        Staff staff = this.staffService.getByUsername(username);
        Staff mentore = this.staffService.getById(mentoreId);
        Hackathon hackathon = this.hackathonService.getById(hackathonId);

        this.hackathonValidator.validaHackathonOrganizzatoDa(hackathon, staff);

        this.hackathonValidator.validaHackathonHaMentore(hackathon, mentore);
        return this.hackathonMapper.toDTO(
                this.hackathonService.rimuoviMentore(hackathon, mentore)
        );
    }

    @Transactional
    public HackathonDTO iscriviTeam(Long hackathonId, String username) {
        Utente utente = this.utenteService.getByUsername(username);
        this.utenteValidator.validaInTeam(utente);

        Hackathon hackathon = this.hackathonService.getById(hackathonId);
        Team team = utente.getTeam();
        hackathon.iscrivi(team);

        this.teamService.save(team);
        return this.hackathonMapper.toDTO(
                this.hackathonService.save(hackathon)
        );
    }

    public HackathonDTO avviaValutazione(Long hackathonId, String username) {
        Staff staff = this.staffService.getByUsername(username);
        Hackathon hackathon = hackathonService.getById(hackathonId);

        this.hackathonValidator.validaHackathonOrganizzatoDa(hackathon, staff);

        hackathon.avviaValutazione();
        return this.hackathonMapper.toSimpleDTO(
                this.hackathonService.save(hackathon)
        );
    }

    public HackathonDTO concludiValutazione(Long hackathonId, String username) {
        Staff staff = this.staffService.getByUsername(username);
        Hackathon hackathon = hackathonService.getById(hackathonId);

        this.hackathonValidator.validaHackathonGiudicatoDa(hackathon, staff);

        hackathon.concludiValutazione();
        return this.hackathonMapper.toSimpleDTO(
                this.hackathonService.save(hackathon)
        );
    }

    public HackathonDTO proclamaVincitore(Long hackathonId, Long teamId, String username) {
        Hackathon hackathon = this.hackathonService.getById(hackathonId);
        Staff staff = this.staffService.getByUsername(username);

        this.hackathonValidator.validaTutteSottomissioniValutate(hackathon);
        this.hackathonValidator.validaHackathonOrganizzatoDa(hackathon, staff);

        Team team = this.teamService.getById(teamId);
        hackathon.proclamaVincitore(team);
        return this.hackathonMapper.toDTO(
                this.hackathonService.save(hackathon)
        );
    }

    public HackathonDTO getHackathonById(Long hackathonId) {
        return this.hackathonMapper.toDTO(
                this.hackathonService.getById(hackathonId)
        );
    }

    public TeamDTO getTeamVincitore(Long hackathonId) {
        Hackathon hackathon = this.hackathonService.getById(hackathonId);
        if (hackathon.getTeamVincitore() == null) throw new HackathonException("Team vincitore ancora non proclamato");
        return this.teamMapper.toDTO(hackathon.getTeamVincitore());
    }

    public void pagaVincitore(Long hackathonId, String username) {
        Staff organizzatore = staffService.getByUsername(username);
        staffValidator.validaOrganizzatore(organizzatore);

        Hackathon hackathon = hackathonService.getById(hackathonId);

        if (hackathon.getTeamVincitore() == null)
            throw new HackathonException("Nessun vincitore proclamato per questo hackathon.");

        this.pagamentoAdapter.pagaPremio(
                hackathon.getId(),
                hackathon.getTeamVincitore().getId(),
                hackathon.getPremioDenaro()
        );
    }
}

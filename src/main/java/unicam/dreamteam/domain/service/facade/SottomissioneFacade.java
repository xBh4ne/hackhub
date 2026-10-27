package unicam.dreamteam.domain.service.facade;

import org.springframework.security.access.AccessDeniedException;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.entity.sottomissione.Sottomissione;
import unicam.dreamteam.domain.model.entity.sottomissione.Valutazione;
import unicam.dreamteam.domain.model.entity.users.Autenticabile;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.model.entity.users.Utente;
import unicam.dreamteam.domain.service.accounts.StaffService;
import unicam.dreamteam.domain.service.accounts.UtenteService;
import unicam.dreamteam.domain.service.hackathon.HackathonService;
import unicam.dreamteam.domain.service.hackathon.sottomissione.SottomissioneService;
import unicam.dreamteam.domain.service.security.SecurityService;
import unicam.dreamteam.domain.validator.HackathonValidator;
import unicam.dreamteam.domain.validator.StaffValidator;
import unicam.dreamteam.domain.validator.UtenteValidator;
import unicam.dreamteam.presentation.dto.hackathon.sottomissione.SottomissioneDTO;
import unicam.dreamteam.presentation.dto.hackathon.sottomissione.ValutazioneDTO;
import unicam.dreamteam.presentation.mapper.sottomissione.SottomissioneMapper;


import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SottomissioneFacade {
    // Service
    private final SecurityService securityService;
    private final SottomissioneService sottomissioneService;
    private final HackathonService hackathonService;
    private final StaffService staffService;
    private final UtenteService utenteService;

    // Validator
    private final UtenteValidator utenteValidator;
    private final StaffValidator staffValidator;
    private final HackathonValidator hackathonValidator;

    // Mapper
    private final SottomissioneMapper sottomissioneMapper;

    public List<SottomissioneDTO> listaSottomissioniByGiudice(String username) {
        Staff giudice = staffService.getByUsername(username);
        staffValidator.validaGiudice(giudice);

        return sottomissioneService.getAllByGiudice(giudice).stream()
                .map(sottomissioneMapper::toSimpleDTO)
                .toList();
    }

    public SottomissioneDTO inviaSottomissione(Long hackathonId, String username, String contenuto) {
        Utente utente = this.utenteService.getByUsername(username);
        this.utenteValidator.validaInTeam(utente);

        Hackathon hackathon = hackathonService.getById(hackathonId);
        Team team = utente.getTeam();

        this.hackathonValidator.validaTeamIscrittoHackathon(hackathon, team);
        this.hackathonValidator.validaTeamNonHaInviatoSottomissione(hackathon, team);

        Sottomissione sottomissione = new Sottomissione(contenuto, team, hackathon);
        hackathon.inviaSottomissione(sottomissione);
        return  this.sottomissioneMapper.toSimpleDTO(
                sottomissioneService.save(sottomissione)
        );
    }

    public SottomissioneDTO aggiornaSottomissione(Long hackathonId, String username, String nuovoContenuto) {
        Utente utente = this.utenteService.getByUsername(username);
        utenteValidator.validaInTeam(utente);

        Hackathon hackathon = hackathonService.getById(hackathonId);
        Team team = utente.getTeam();

        this.hackathonValidator.validaTeamIscrittoHackathon(hackathon, team);
        this.hackathonValidator.validaTeamHaInviatoSottomissione(hackathon, team);

        Sottomissione sottomissione = this.sottomissioneService.getByHackathonAndTeam(hackathon, team);

        sottomissione.aggiorna(nuovoContenuto);
        return this.sottomissioneMapper.toSimpleDTO(
                sottomissioneService.save(sottomissione)
        );
    }

    public SottomissioneDTO getSottomissioneByIdAndAccountUsername(Long sottomissioneId, String username) {
        Autenticabile account = this.securityService.getAccountByUsername(username);

        if (account instanceof Utente utente) return getSottomissioneByIdAndUtente(sottomissioneId, utente);
        if (account instanceof Staff staff) return getSottomissioneByIdAndStaff(sottomissioneId, staff);

        throw new AccessDeniedException("Il tuo ruolo non ti permette di visualizzare questa sottomissione.");
    }

    public SottomissioneDTO getSottomissioneByIdAndStaff(Long sottomissioneId, Staff staff) {
        this.staffValidator.validaGiudice(staff);
        Sottomissione sottomissione = this.sottomissioneService.getById(sottomissioneId);
        Hackathon hackathon = sottomissione.getHackathon();

        if (!hackathon.getGiudice().equals(staff) && !hackathon.getOrganizzatore().equals(staff))
            throw new AccessDeniedException("Non puoi operare in questo hackathon.");

        return this.sottomissioneMapper.toSimpleDTO(sottomissione);
    }

    public SottomissioneDTO getSottomissioneByIdAndUtente(Long sottomissioneId, Utente utente) {
        utenteValidator.validaInTeam(utente);

        Sottomissione sottomissione = this.sottomissioneService.getById(sottomissioneId);
        Team team = utente.getTeam();

        if (!sottomissione.getTeam().equals(team)) throw new EntityNotFoundException(
                String.format("Nessuna sottomissione trovata con l'id = %s.", sottomissioneId)
        );

        return this.sottomissioneMapper.toSimpleDTO(sottomissione);
    }

    public List<SottomissioneDTO> listaSottomissioniByUtenteUsername(String username) {
        Utente utente = this.utenteService.getByUsername(username);
        this.utenteValidator.validaInTeam(utente);

        Team team = utente.getTeam();
        return this.sottomissioneService.getByTeam(team).stream()
                .map(this.sottomissioneMapper::toSimpleDTO)
                .toList();
    }

    public SottomissioneDTO valutaSottomission(Long sottomissioneId, ValutazioneDTO valutazioneDTO, String username) {
        Staff giudice = staffService.getByUsername(username);
        staffValidator.validaGiudice(giudice);

        Sottomissione sottomissione = sottomissioneService.getById(sottomissioneId);

        this.hackathonValidator.validaHackathonInValutazione(sottomissione.getHackathon());
        this.hackathonValidator.validaHackathonGiudicatoDa(sottomissione.getHackathon(), giudice);

        if (sottomissione.isValutata()) {
            // aggiorna la valutazione esistente
            sottomissione.getValutazione().aggiorna(valutazioneDTO.getPunteggio(), valutazioneDTO.getGiudizio());
        } else {
            Valutazione valutazione = new Valutazione(sottomissione, valutazioneDTO.getPunteggio(), valutazioneDTO.getGiudizio(), giudice);
            sottomissione.setValutazione(valutazione);
        }

        return sottomissioneMapper.toSimpleDTO(sottomissioneService.save(sottomissione));
    }
}

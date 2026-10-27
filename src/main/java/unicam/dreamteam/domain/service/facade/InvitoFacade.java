package unicam.dreamteam.domain.service.facade;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicam.dreamteam.domain.exception.invito.InvitoException;
import unicam.dreamteam.domain.model.entity.Invito;
import unicam.dreamteam.domain.model.entity.state.invito.StatoInvitoPendente;
import unicam.dreamteam.domain.model.entity.users.Utente;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloStaff;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloUtente;
import unicam.dreamteam.domain.service.accounts.UtenteService;
import unicam.dreamteam.domain.model.entity.users.Autenticabile;
import unicam.dreamteam.domain.service.security.SecurityService;
import unicam.dreamteam.domain.service.team.InvitoService;
import unicam.dreamteam.domain.validator.UtenteValidator;
import unicam.dreamteam.presentation.dto.team.InvitoDTO;
import unicam.dreamteam.presentation.mapper.InvitoMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class InvitoFacade {
    // Service
    private final SecurityService securityService;
    private final InvitoService invitoService;
    private final UtenteService utenteService;

    // Validator
    private UtenteValidator utenteValidator;

    // Mapper
    private final InvitoMapper invitoMapper;

    public List<InvitoDTO> getAllByPendentiByAccountUsername(String username) {
        Autenticabile account = this.securityService.getAccountByUsername(username);
        if (account.getRuolo() == RuoloStaff.ADMIN) return getAllPendenti();
        if (account.getRuolo() == RuoloUtente.UTENTE) return getAllPendentiByUtente((Utente) account);
        return List.of(); // :)
    }

    public List<InvitoDTO> getAllPendenti() {
        return this.invitoService.getAllByStato(new StatoInvitoPendente()).stream()
                .map(this.invitoMapper::toDTO)
                .toList();
    }

    public List<InvitoDTO> getAllPendentiByUtenteUsername(String username) {
        Utente utente = this.utenteService.getByUsername(username);
        return getAllPendentiByUtente(utente);
    }

    public List<InvitoDTO> getAllPendentiByUtente(Utente utente) {
        return this.invitoService.getAllByUtenteAndStato(utente, new StatoInvitoPendente()).stream()
                .map(this.invitoMapper::toDTO)
                .toList();
    }

    public List<InvitoDTO> getAllPendentiTeamByUtenteUsername(String username) {
        Utente utente = this.utenteService.getByUsername(username);
        return getAllPendentiTeamByUtente(utente);
    }

    public List<InvitoDTO> getAllPendentiTeamByUtente(Utente utente) {
        this.utenteValidator.validaInTeam(utente);
        return this.invitoService.getAllByUtentesTeam(utente.getTeam()).stream()
                .map(this.invitoMapper::toDTO)
                .toList();
    }

    public InvitoDTO invita(String usernameUtenteMembroTeam, String usernameUtenteInvitato) {
        Utente currentUser = this.utenteService.getByUsername(usernameUtenteMembroTeam);
        this.utenteValidator.validaInTeam(currentUser);

        Utente utenteInvitato = this.utenteService.getByUsername(usernameUtenteInvitato);

        return this.invitoMapper.toDTO(
                this.invitoService.invita(currentUser.getTeam(), utenteInvitato)
        );
    }

    public InvitoDTO accettaInvito(Long invitoId, String username) {
        Utente utente = this.utenteService.getByUsername(username);
        this.utenteValidator.validaNotInTeam(utente);
        Invito invito = this.invitoService.getById(invitoId);
        if (!invito.getUtente().equals(utente)) throw new InvitoException("Invito non di tua proprietà.");

        return this.invitoMapper.toDTO(
                this.invitoService.accetta(
                    invito
        ));
    }

    public InvitoDTO rifiutaInvito(Long invitoId, String username) {
        Utente utente = this.utenteService.getByUsername(username);
        Invito invito = this.invitoService.getById(invitoId);
        if (!invito.getUtente().equals(utente)) throw new InvitoException("Invito non di tua proprietà.");

        return this.invitoMapper.toDTO(
                this.invitoService.rifiuta(
                        invito
                ));
    }
}

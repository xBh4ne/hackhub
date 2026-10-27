package unicam.dreamteam.presentation.api.controllers;

import unicam.dreamteam.domain.service.facade.InvitoFacade;
import unicam.dreamteam.presentation.dto.team.requests.InvitaMembroRequest;
import unicam.dreamteam.presentation.dto.team.InvitoDTO;
import unicam.dreamteam.presentation.mapper.InvitoMapper;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inviti")
@PreAuthorize("isAuthenticated()")
@AllArgsConstructor
public class InvitoController {
    private InvitoFacade invitoFacade;

    /**
     * Restituisce gli inviti in stato pendente, per staff con ruolo {@code ADMIN}. Se l'utente ha il ruolo {@code UTENTE} vengono
     * restituiti solo i propri inviti, se ha il ruolo {@code ADMIN} vengono restituiti tutti.
     *
     * @param authentication le informazioni sull'autenticazione dell'utente corrente
     * @return la lista degli inviti in stato pendente, vedi {@link InvitoDTO}
     */
    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'UTENTE')")
    public ResponseEntity<List<InvitoDTO>> index(Authentication authentication) {
        return ResponseEntity.ok(
                this.invitoFacade.getAllByPendentiByAccountUsername(authentication.getName())
        );
    }

    /**
     * Invita un utente a unirsi al proprio team.
     *
     * @param request        le informazioni necessarie per creare l'invito, vedi {@link InvitaMembroRequest}
     * @param authentication le informazioni sull'autenticazione dell'utente corrente
     * @return le informazioni dell'invito creato, vedi {@link InvitoDTO}
     */
    // TODO: Gestione Dimensione Massima Team per hackathon
    // TODO: Utente tecnicamente non puo' invitare se stesso nel suo team perchè è già in un team. (Gestirlo?)
    @PostMapping()
    @PreAuthorize("hasRole('UTENTE')")
    public ResponseEntity<InvitoDTO> invita(@Valid @RequestBody InvitaMembroRequest request, Authentication authentication) {
        return ResponseEntity.ok(
                this.invitoFacade.invita(
                        authentication.getName(),
                        request.usernameUtente()
                )
        );
    }

    @PostMapping("/{invitoId}/accetta")
    @PreAuthorize("hasRole('UTENTE')")
    public ResponseEntity<InvitoDTO> accetta(@PathVariable Long invitoId, Authentication authentication) {
        return ResponseEntity.ok(
                this.invitoFacade.accettaInvito(
                        invitoId,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{invitoId}/rifiuta")
    @PreAuthorize("hasRole('UTENTE')")
    public ResponseEntity<InvitoDTO> rifiuta(@PathVariable Long invitoId, Authentication authentication) {
        return ResponseEntity.ok(
                this.invitoFacade.rifiutaInvito(
                        invitoId,
                        authentication.getName()
                )
        );
    }
}

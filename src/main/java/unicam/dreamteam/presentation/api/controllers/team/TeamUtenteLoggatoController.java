package unicam.dreamteam.presentation.api.controllers.team;

import org.springframework.web.bind.annotation.PostMapping;
import unicam.dreamteam.domain.service.facade.*;
import unicam.dreamteam.presentation.dto.hackathon.HackathonDTO;
import unicam.dreamteam.presentation.dto.hackathon.sottomissione.SottomissioneDTO;
import unicam.dreamteam.presentation.dto.team.InvitoDTO;
import unicam.dreamteam.presentation.dto.team.TeamDTO;
import unicam.dreamteam.presentation.dto.team.richiesteSupporto.RichiestaSupportoDTO;
import unicam.dreamteam.presentation.mapper.InvitoMapper;
import unicam.dreamteam.presentation.mapper.TeamMapper;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Controller per endpoints del team di cui fa parte l'utente.
 */
@RestController
@RequestMapping("/api/team")
@PreAuthorize("hasRole('UTENTE')")
@AllArgsConstructor
public class TeamUtenteLoggatoController {
    private final TeamFacade teamFacade;
    private final InvitoFacade invitoFacade;
    private final HackathonFacade hackathonFacade;
    private final SottomissioneFacade sottomissioneFacade;
    private final RichiestaSupportoFacade richiestaSupportoFacade;

    /**
     * Mostra le informazioni del proprio team.
     */
    @GetMapping
    public ResponseEntity<TeamDTO> myTeamInfo(Authentication authentication) {
        return ResponseEntity.ok(
                this.teamFacade.getTeamByUsername(authentication.getName())
        );
    }

    /**
     * Esce dal team. (Se è l'ultimo membro del team si elimina.)
     */
    @PostMapping("/esci")
    public ResponseEntity<String> esci(Authentication authentication) {
        return ResponseEntity.ok(
                this.teamFacade.esciDalTeam(authentication.getName())
        );
    }

    /**
     * Mostra gli inviti del proprio team. (Quelli inviati dal proprio team)
     */
    @GetMapping("/inviti")
    public ResponseEntity<List<InvitoDTO>> inviti(Authentication authentication) {
        return ResponseEntity.ok(
                this.invitoFacade.getAllPendentiTeamByUtenteUsername(authentication.getName())
        );
    }

    /**
     * Mostra tutti gli Hackathon a cui il team è iscritto.
     */
    @GetMapping("/hackathons")
    public ResponseEntity<List<HackathonDTO>> hackathons(Authentication authentication) {
        return ResponseEntity.ok(
                this.hackathonFacade.listaHackathonByUsername(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/sottomissioni")
    public ResponseEntity<List<SottomissioneDTO>> sottomissioni(Authentication authentication) {
        return ResponseEntity.ok(
                this.sottomissioneFacade.listaSottomissioniByUtenteUsername(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/richieste-supporto")
    public ResponseEntity<List<RichiestaSupportoDTO>> richiesteSupporto(Authentication authentication) {
        return ResponseEntity.ok(
                this.richiestaSupportoFacade.getByTeamUsername(
                        authentication.getName()
                )
        );
    }
}

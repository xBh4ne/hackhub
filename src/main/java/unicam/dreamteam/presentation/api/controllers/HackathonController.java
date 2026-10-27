package unicam.dreamteam.presentation.api.controllers;

import unicam.dreamteam.domain.service.facade.HackathonFacade;
import unicam.dreamteam.presentation.dto.hackathon.HackathonDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import lombok.AllArgsConstructor;
import unicam.dreamteam.presentation.dto.team.TeamDTO;

@RestController
@RequestMapping("/api/hackathons")
@AllArgsConstructor
public class HackathonController {
    private HackathonFacade hackathonFacade;

    @GetMapping
    public ResponseEntity<List<HackathonDTO>> index() {
        return ResponseEntity.ok(
                this.hackathonFacade.listaHackathon()
        );
    }

    @GetMapping("/{hackathonId}")
    public ResponseEntity<HackathonDTO> getById(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(
                this.hackathonFacade.getHackathonById(hackathonId)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ORGANIZZATORE')")
    public ResponseEntity<HackathonDTO> creaHackathon(@RequestBody HackathonDTO request, Authentication authentication) {
        return ResponseEntity.ok(
                this.hackathonFacade.creaHackathon(
                        request,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{hackathonId}/mentore/add/{mentoreId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ORGANIZZATORE')")
    public ResponseEntity<HackathonDTO> aggiungiMentore(@PathVariable Long hackathonId, @PathVariable Long mentoreId, Authentication authentication) {
        return ResponseEntity.ok(
                this.hackathonFacade.aggiungiMentore(
                        hackathonId,
                        mentoreId,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{hackathonId}/mentori/remove/{mentoreId}")
    @PreAuthorize("hasRole('ORGANIZZATORE')")
    public ResponseEntity<HackathonDTO> rimuoviMentore(@PathVariable Long hackathonId, @PathVariable Long mentoreId, Authentication authentication) {
        return ResponseEntity.ok(
                hackathonFacade.rimuoviMentore(
                        hackathonId,
                        mentoreId,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{hackathonId}/apri-iscrizioni")
    @PreAuthorize("hasRole('ORGANIZZATORE')")
    public ResponseEntity<HackathonDTO> apriIscrizioni(@PathVariable Long hackathonId, Authentication authentication) {
        return ResponseEntity.ok(
                hackathonFacade.apriIscrizioni(
                        hackathonId,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{hackathonId}/iscrivi")
    @PreAuthorize("hasAnyRole('UTENTE', 'TEAM_LEADER')")
    public ResponseEntity<HackathonDTO> iscriviTeam(@PathVariable Long hackathonId, Authentication authentication) {
        return ResponseEntity.ok(
                this.hackathonFacade.iscriviTeam(
                        hackathonId,
                        authentication.getName() // Username dell'utente loggato.
                )
        );
    }

    @PostMapping("/{hackathonId}/avvia")
    @PreAuthorize("hasRole('ORGANIZZATORE')")
    public ResponseEntity<HackathonDTO> avviaHackathon(@PathVariable Long hackathonId, Authentication authentication) {
        return ResponseEntity.ok(
              this.hackathonFacade.avviaHackathon(
                      hackathonId,
                      authentication.getName()
              )
        );
    }

    @PostMapping("/{hackathonId}/avvia-valutazione")
    @PreAuthorize("hasRole('ORGANIZZATORE')")
    public ResponseEntity<HackathonDTO> avviaValutazione(@PathVariable Long hackathonId, Authentication authentication) {
        return ResponseEntity.ok(
                hackathonFacade.avviaValutazione(
                        hackathonId,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{hackathonId}/concludi-valutazione")
    @PreAuthorize("hasRole('GIUDICE')")
    public ResponseEntity<HackathonDTO> concludiValutazione(@PathVariable Long hackathonId, Authentication authentication) {
        return ResponseEntity.ok(
                hackathonFacade.concludiValutazione(
                        hackathonId,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{hackathonId}/proclama-vincitore")
    @PreAuthorize("hasRole('ORGANIZZATORE')")
    public ResponseEntity<HackathonDTO> proclamaVincitore(@PathVariable Long hackathonId, @RequestBody TeamDTO teamVincitore, Authentication authentication) {
        return ResponseEntity.ok(
                this.hackathonFacade.proclamaVincitore(
                        hackathonId,
                        teamVincitore.getId(),
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{hackathonId}/team-vincitore")
    public ResponseEntity<TeamDTO> proclamaVincitore(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(
                this.hackathonFacade.getTeamVincitore(
                        hackathonId
                )
        );
    }
}

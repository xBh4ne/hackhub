package unicam.dreamteam.presentation.api.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import unicam.dreamteam.domain.service.facade.RichiestaSupportoFacade;
import unicam.dreamteam.presentation.dto.team.richiesteSupporto.PrenotaCallRequest;
import unicam.dreamteam.presentation.dto.team.richiesteSupporto.RichiestaSupportoDTO;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/richieste-supporto")
@AllArgsConstructor
public class RichiestaSupportoController {
    private final RichiestaSupportoFacade richiestaSupportoFacade;

    @GetMapping
    @PreAuthorize("hasRole('MENTORE')")
    public ResponseEntity<List<RichiestaSupportoDTO>> index(Authentication authentication) {
        return ResponseEntity.ok(
                this.richiestaSupportoFacade.getByMentoreUsername(authentication.getName())
        );
    }

    @PostMapping("/hackathon/{hackathonId}/mentore/{mentoreId}")
    @PreAuthorize("hasRole('UTENTE')")
    public ResponseEntity<RichiestaSupportoDTO> richiediSupporto(
            @PathVariable Long hackathonId,
            @PathVariable Long mentoreId,
            @RequestBody RichiestaSupportoDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(
                this.richiestaSupportoFacade.richiediSupporto(
                        hackathonId,
                        mentoreId,
                        authentication.getName(),
                        request
                )
        );
    }

    @PostMapping("/{richiestaId}/call")
    @PreAuthorize("hasRole('MENTORE')")
    public ResponseEntity<RichiestaSupportoDTO> prenotaCall(
            @PathVariable Long richiestaId,
            @RequestBody PrenotaCallRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                richiestaSupportoFacade.prenotaCall(richiestaId, request.dataOra(), authentication.getName())
        );
    }
}

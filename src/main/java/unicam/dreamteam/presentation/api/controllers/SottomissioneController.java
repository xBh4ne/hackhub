package unicam.dreamteam.presentation.api.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import unicam.dreamteam.domain.service.facade.SottomissioneFacade;
import unicam.dreamteam.presentation.dto.hackathon.sottomissione.SottomissioneDTO;
import unicam.dreamteam.presentation.dto.hackathon.sottomissione.ValutazioneDTO;

@RestController
@RequestMapping("/api/sottomissioni")
@AllArgsConstructor
public class SottomissioneController {
    private final SottomissioneFacade sottomissioneFacade;

    @GetMapping("/{sottomissioneId}")
    @PreAuthorize("hasAnyRole('GIUDICE', 'ORGANIZZATORE', 'UTENTE')")
    public ResponseEntity<SottomissioneDTO> getById(@PathVariable Long sottomissioneId, Authentication authentication) {
        return ResponseEntity.ok(
                this.sottomissioneFacade.getSottomissioneByIdAndAccountUsername(
                        sottomissioneId,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/send")
    @PreAuthorize("hasRole('UTENTE')")
    public ResponseEntity<SottomissioneDTO> inviaSottomissione(@RequestBody SottomissioneDTO dto, Authentication authentication) {
        return ResponseEntity.ok(
                this.sottomissioneFacade.inviaSottomissione(
                        dto.getHackathonId(),
                        authentication.getName(), // Username utente loggato
                        dto.getContenuto()
                )
        );
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('UTENTE')")
    public ResponseEntity<SottomissioneDTO> aggiornaSottomissione(@RequestBody SottomissioneDTO dto, Authentication authentication) {
        return ResponseEntity.ok(
                this.sottomissioneFacade.aggiornaSottomissione(
                        dto.getHackathonId(),
                        authentication.getName(), // Username utente loggato
                        dto.getContenuto()
                )
        );
    }


    @PostMapping("/{sottomissioneId}/valuta")
    @PreAuthorize("hasAnyRole('GIUDICE')")
    public ResponseEntity<SottomissioneDTO> valutaSottomissione(@PathVariable Long sottomissioneId, @RequestBody ValutazioneDTO valutazioneDTO, Authentication authentication) {
        return ResponseEntity.ok(
                this.sottomissioneFacade.valutaSottomission(
                        sottomissioneId,
                        valutazioneDTO,
                        authentication.getName()
                )
        );
    }
}

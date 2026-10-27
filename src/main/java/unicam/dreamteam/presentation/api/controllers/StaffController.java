package unicam.dreamteam.presentation.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloStaff;
import unicam.dreamteam.domain.service.facade.HackathonFacade;
import unicam.dreamteam.domain.service.accounts.StaffService;
import unicam.dreamteam.domain.service.facade.SottomissioneFacade;
import unicam.dreamteam.presentation.dto.hackathon.HackathonDTO;
import unicam.dreamteam.presentation.dto.hackathon.sottomissione.SottomissioneDTO;
import unicam.dreamteam.presentation.dto.security.AccountDTO;
import unicam.dreamteam.presentation.mapper.AccountMapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import lombok.AllArgsConstructor;
import unicam.dreamteam.presentation.mapper.HackathonMapper;


@RestController
@RequestMapping("/api/staffs")
@PreAuthorize("isAuthenticated()")
@AllArgsConstructor
public class StaffController {
    // Facade
    private HackathonFacade hackathonFacade;
    private SottomissioneFacade sottomissioneFacade;

    // Service
    private StaffService staffService;

    // Mapper
    private AccountMapper accountMapper;
    private HackathonMapper hackathonMapper;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<AccountDTO>> getAll() {
        return ResponseEntity.ok(
                this.staffService.getAll()
                    .stream()
                    .map(accountMapper::toDTO)
                    .toList()
        );
    }

    @GetMapping("/admins")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<AccountDTO>> getAllAdmins() {
        return ResponseEntity.ok(
                this.staffService.getAllByRuolo(RuoloStaff.ADMIN)
                    .stream()
                    .map(accountMapper::toDTO)
                    .toList()
        );

    }

    @GetMapping("/giudici")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZZATORE')")
    public ResponseEntity<List<AccountDTO>> getAllGiudici() {
        return ResponseEntity.ok(
                this.staffService.getAllByRuolo(RuoloStaff.GIUDICE)
                    .stream()
                    .map(accountMapper::toDTO)
                    .toList()
        );
    }

    @GetMapping("/organizzatori")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<AccountDTO>> getAllOrganizzatori() {
        return ResponseEntity.ok(
                this.staffService.getAllByRuolo(RuoloStaff.ORGANIZZATORE)
                    .stream()
                    .map(accountMapper::toDTO)
                    .toList()
        );
    }


    @GetMapping("/mentori")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZZATORE')")
    public ResponseEntity<List<AccountDTO>> getAllMentori() {
        return ResponseEntity.ok(
                this.staffService.getAllByRuolo(RuoloStaff.MENTORE)
                    .stream()
                    .map(accountMapper::toDTO)
                    .toList()
        );
    }

    @GetMapping("/hackathons")
    @PreAuthorize("hasAnyRole('GIUDICE', 'ORGANIZZATORE', 'MENTORE')")
    public ResponseEntity<List<HackathonDTO>> getAllStaffHackathons(Authentication authentication) {
        return ResponseEntity.ok(
                this.hackathonFacade.listaHackathonByStaffUsername(
                    authentication.getName()
        ));
    }

    @GetMapping("/sottomissioni")
    @PreAuthorize("hasRole('GIUDICE')")
    public ResponseEntity<List<SottomissioneDTO>> listaSottomissioniGiudice(Authentication authentication) {
        return ResponseEntity.ok(
                sottomissioneFacade.listaSottomissioniByGiudice(authentication.getName())
        );
    }
}

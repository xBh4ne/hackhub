package unicam.dreamteam.presentation.api.controllers;

import jakarta.validation.ValidationException;
import org.springframework.web.bind.annotation.*;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloStaff;
import unicam.dreamteam.presentation.dto.security.requests.LoginRequest;
import unicam.dreamteam.presentation.dto.security.requests.RegisterRequest;
import unicam.dreamteam.presentation.dto.security.AccountDTO;
import unicam.dreamteam.presentation.dto.security.TokenDTO;
import unicam.dreamteam.domain.service.security.SecurityService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import unicam.dreamteam.presentation.mapper.AccountMapper;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class SecurityController {
    private final SecurityService securityService;
    private final AccountMapper accountMapper;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                securityService.login(
                        request.username(),
                        request.password()
                ));
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(
                securityService.getAllAccounts().stream()
                        .map(this.accountMapper::toDTO)
                        .toList()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(
                securityService.register(
                        request.username(),
                        request.email(),
                        request.password()
                ));
    }

    @PostMapping("/register/staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TokenDTO> creaStaff(
            @Valid @RequestBody RegisterRequest request
    ) {
        boolean isRuoloValido = Arrays.stream(RuoloStaff.values()).allMatch(
                (r) -> r.getName().equals(request.ruolo())
        );
        if (request.ruolo() == null || isRuoloValido) throw new ValidationException("Ruolo non specificato");

        return ResponseEntity.ok(
                securityService.creaStaff(
                        request.username(),
                        request.email(),
                        request.password(),
                        RuoloStaff.valueOf(request.ruolo())
                ));
    }
}

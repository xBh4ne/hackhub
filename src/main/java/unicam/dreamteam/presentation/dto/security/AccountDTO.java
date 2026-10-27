package unicam.dreamteam.presentation.dto.security;

public record AccountDTO(
        Long id,
        String username,
        String email,
        String ruolo
) { }

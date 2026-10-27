package unicam.dreamteam.presentation.dto.security;

public record TokenDTO(
        String accessToken,
        String expiration,
        String type
) {
}

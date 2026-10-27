package unicam.dreamteam.domain.service.security;

import unicam.dreamteam.presentation.dto.security.TokenDTO;

public interface IAuthentication {
    TokenDTO login(String username, String password);
    TokenDTO register(String username, String email, String password);
}

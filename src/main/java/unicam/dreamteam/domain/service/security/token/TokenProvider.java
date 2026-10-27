package unicam.dreamteam.domain.service.security.token;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface TokenProvider {
    String generateToken(Map<String, Object> claims, UserDetails userDetails);
    long getExpirationTime();
}

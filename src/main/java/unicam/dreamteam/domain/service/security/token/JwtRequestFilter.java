package unicam.dreamteam.domain.service.security.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import unicam.dreamteam.domain.service.security.userdetails.CustomUserDetailsService;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authorizationHeaderValue = request.getHeader("Authorization"); // "Bearer <Token>"

        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer ")) {
            try {
                final String token = authorizationHeaderValue.substring(7);
                logger.info("Token estratto, lunghezza: " + token.length());

                final String username = tokenProvider.getUsernameFromToken(token);
                logger.info("Username: " + username);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                logger.info("Authentication esistente: " + authentication);

                if (username != null && authentication == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    boolean valid = tokenProvider.validateToken(token, userDetails);
                    logger.info("Token valido: " + valid);
                    if (valid) setAuthentication(request, userDetails);
                }
            } catch (Exception exception) {
                logger.error("JWT authentication error", exception);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        auth.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request));

        SecurityContextHolder.getContext()
                .setAuthentication(auth);
    }
}

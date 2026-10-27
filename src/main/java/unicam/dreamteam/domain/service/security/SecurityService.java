package unicam.dreamteam.domain.service.security;

import jakarta.persistence.EntityNotFoundException;
import unicam.dreamteam.domain.model.entity.users.Autenticabile;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.model.entity.users.Utente;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloStaff;
import unicam.dreamteam.domain.repository.StaffRepository;
import unicam.dreamteam.domain.repository.UtenteRepository;
import unicam.dreamteam.domain.service.security.token.TokenProvider;
import unicam.dreamteam.presentation.dto.security.TokenDTO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.*;

@Service
@AllArgsConstructor
public class SecurityService implements IAuthentication {
    private final TokenProvider tokenProvider;
    private final UtenteRepository utenteRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenDTO login(String username, String password) {
        try { // Try/Catch per normalizzare exception di getAccountByUsername (NotFound->BadCredentials)
            Autenticabile account = getAccountByUsername(username);
            if (passwordEncoder.matches(password, account.getPassword()))
                return getTokenResponse(account);
        } catch (EntityNotFoundException ignored) {}

        throw new BadCredentialsException("Credenziali Errate!");
    }

    /**
     * Registrazione di un nuovo account.
     * </p>
     * Crea un nuovo utente {@link Utente} nel sistema con ruolo di default {@code UTENTE}.
     * La creazione di account staff {@link Staff} è riservata al ruolo {@code ADMIN}
     * esclusivamente agli amministratori della piattaforma.
     *
     * @param username il nome utente scelto dall'utente; deve essere univoco nel sistema
     * @param email    l'indirizzo email dell'utente; deve essere univoco e in formato valido
     * @param password la password scelta dall'utente; verrà salvata codificato
     * @return un oggetto che rappresenta l'account appena creato
     *
     * @throws IllegalArgumentException se {@code username}, {@code email} o {@code password} sono null o vuoti
     * @throws BadCredentialsException se esiste già un account con lo stesso {@code username} o la stessa {@code email}
     */
    @Override
    public TokenDTO register(String username, String email, String password) {
        checkAccountUniqueUsernameAndEmail(username, email);

        Utente utente = new Utente(username, email, passwordEncoder.encode(password));
        utenteRepository.save(utente);
        return getTokenResponse(utente);
    }

    public TokenDTO creaStaff(String username, String email, String password, RuoloStaff ruolo) {
        checkAccountUniqueUsernameAndEmail(username, email);

        Staff staff = new Staff(username, email, passwordEncoder.encode(password), ruolo);
        staffRepository.save(staff);
        return getTokenResponse(staff);
    }

    private void checkAccountUniqueUsernameAndEmail(String username, String email) {
        checkAccountExistsWithUsername(username);
        checkAccountExistsWithEmail(email);
    }

    private void checkAccountExistsWithUsername(String username) {
        if (!(isUtenteExistsWithUsername(username) || isStaffExistsWithUsername(username))) return;
        throw new BadCredentialsException("Username già in uso.");
    }

    private void checkAccountExistsWithEmail(String email) {
        if (!(isUtenteExistsWithEmail(email) || isStaffExistsWithEmail(email))) return;
        throw new BadCredentialsException("Email già in uso.");
    }

    // USERNAME
    private boolean isUtenteExistsWithUsername(String username) {
        return utenteRepository.existsByUsername(username);
    }
    private boolean isStaffExistsWithUsername(String username) {
        return staffRepository.existsByUsername(username);
    }

    // EMAIL
    private boolean isUtenteExistsWithEmail(String email) {
        return utenteRepository.existsByEmail(email);
    }
    private boolean isStaffExistsWithEmail(String email) {
        return staffRepository.existsByEmail(email);
    }

    private TokenDTO getTokenResponse(Autenticabile account) {
        User user = new User(
                account.getUsername(),
                account.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(account.getRuolo().toAuthority()))
        );
        Map<String, Object> claims = new HashMap<>();

        String token = tokenProvider.generateToken(claims, user);
        return new TokenDTO(token, String.valueOf(tokenProvider.getExpirationTime()), "Bearer");
    }

    public Autenticabile getAccountByUsername(String username) {
        return utenteRepository.findByUsername(username)
                .map(u -> (Autenticabile) u)
                .or(() -> staffRepository.findByUsername(username).map(s -> (Autenticabile) s))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Account.username=%s", username)));
    }


    public List<Autenticabile> getAllAccounts() {
        List<Autenticabile> accounts = new ArrayList<>();

        accounts.addAll(this.utenteRepository.findAll().stream()
                .map((u) -> (Autenticabile) u).toList());

        accounts.addAll(this.staffRepository.findAll().stream()
                .map((u) -> (Autenticabile) u).toList());
        return accounts;
    }
}
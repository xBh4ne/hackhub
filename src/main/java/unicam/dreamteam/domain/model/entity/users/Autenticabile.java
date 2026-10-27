package unicam.dreamteam.domain.model.entity.users;

import unicam.dreamteam.domain.model.entity.users.ruolo.Ruolo;

public interface Autenticabile {
    Long getId();
    String getUsername();
    String getPassword();
    String getEmail();
    Ruolo getRuolo();
}

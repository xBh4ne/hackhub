package unicam.dreamteam.domain.model.entity.users.ruolo;

import lombok.Getter;

@Getter
public enum RuoloUtente implements Ruolo {
    UTENTE,
    TEAM_MEMBER,
    TEAM_LEADER;

    public String toAuthority() {
        return String.format("ROLE_%s", this.name()); // ROLE_NAME
    }

    @Override
    public String getName() {
        return name();
    } // NAME
}

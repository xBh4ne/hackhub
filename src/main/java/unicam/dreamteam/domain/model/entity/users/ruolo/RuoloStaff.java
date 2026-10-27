package unicam.dreamteam.domain.model.entity.users.ruolo;

import lombok.Getter;

@Getter
public enum RuoloStaff implements Ruolo {
    ADMIN,
    ORGANIZZATORE,
    GIUDICE,
    MENTORE;

    public String toAuthority() { return "ROLE_" + this.name(); }

    @Override
    public String getName() {
        return name();
    }
}

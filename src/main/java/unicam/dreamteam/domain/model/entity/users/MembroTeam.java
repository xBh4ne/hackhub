package unicam.dreamteam.domain.model.entity.users;

import unicam.dreamteam.domain.model.entity.Team;

public interface MembroTeam {
    Team getTeam();
    void setTeam(Team team);
    default boolean hasTeam() {
        return getTeam() != null;
    }
}
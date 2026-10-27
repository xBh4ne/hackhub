package unicam.dreamteam.presentation.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.presentation.dto.team.TeamDTO;

@Component
@AllArgsConstructor
public class TeamMapper implements IMapper<Team, TeamDTO> {
    private final AccountMapper accountMapper;

    public TeamDTO toDTO(Team team) {
        return new TeamDTO(
                team.getId(),
                team.getNome(),
                team.getDescrizione(),
                team.getMembri().stream()
                        .map(this.accountMapper::toDTO)
                        .toList()
        );
    }

    public TeamDTO toSimpleDTO(Team team) {
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setNome(team.getNome());
        dto.setDescrizione(team.getDescrizione());
        return dto;
    }
}

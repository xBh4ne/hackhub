package unicam.dreamteam.presentation.mapper;

import lombok.AllArgsConstructor;
import unicam.dreamteam.domain.model.entity.Invito;
import unicam.dreamteam.presentation.dto.team.InvitoDTO;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InvitoMapper implements IMapper<Invito, InvitoDTO> {
    private TeamMapper teamMapper;
    private AccountMapper accountMapper;

    @Override
    public InvitoDTO toDTO(Invito invito) {
        return new InvitoDTO(
                invito.getId(),
                this.teamMapper.toSimpleDTO(invito.getTeam()),
                this.accountMapper.toDTO(invito.getUtente()),
                invito.getDataInvito(),
                invito.getStato().getNome()
        );
    }
}

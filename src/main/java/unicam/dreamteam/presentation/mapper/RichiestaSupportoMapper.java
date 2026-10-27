package unicam.dreamteam.presentation.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import unicam.dreamteam.domain.model.entity.RichiestaSupporto;
import unicam.dreamteam.presentation.dto.team.richiesteSupporto.CallDTO;
import unicam.dreamteam.presentation.dto.team.richiesteSupporto.RichiestaSupportoDTO;

@Component
@AllArgsConstructor
public class RichiestaSupportoMapper {
    private final AccountMapper accountMapper;
    private final TeamMapper teamMapper;

    public RichiestaSupportoDTO toDTO(RichiestaSupporto richiesta) {
        RichiestaSupportoDTO dto = new RichiestaSupportoDTO();
        dto.setId(richiesta.getId());
        dto.setOggetto(richiesta.getOggetto());
        dto.setDescrizione(richiesta.getDescrizione());
        dto.setDataInvio(richiesta.getDataInvio());
        dto.setStato(richiesta.getStato().name());
        dto.setMentore(richiesta.getMentore() == null ? null : accountMapper.toDTO(richiesta.getMentore()));
        dto.setTeam(teamMapper.toDTO(richiesta.getTeam()));
        dto.setCalls(richiesta.getCalls().stream()
                .map(call -> new CallDTO(call.getId(), call.getLinkMeeting(), call.getDataOra()))
                .toList());

        return dto;
    }

    public RichiestaSupportoDTO toSimpleDTO(RichiestaSupporto richiesta) {
        RichiestaSupportoDTO dto = new RichiestaSupportoDTO();
        dto.setId(richiesta.getId());
        dto.setOggetto(richiesta.getOggetto());
        dto.setDescrizione(richiesta.getDescrizione());
        dto.setDataInvio(richiesta.getDataInvio());
        dto.setStato(richiesta.getStato().name());
        dto.setMentore(richiesta.getMentore() == null ? null : accountMapper.toDTO(richiesta.getMentore()));
        dto.setTeam(teamMapper.toSimpleDTO(richiesta.getTeam()));
        dto.setCalls(richiesta.getCalls().stream()
                .map(call -> new CallDTO(call.getId(), call.getLinkMeeting(), call.getDataOra()))
                .toList());

        return dto;
    }
}

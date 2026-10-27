package unicam.dreamteam.presentation.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import unicam.dreamteam.domain.model.entity.Segnalazione;
import unicam.dreamteam.presentation.dto.team.richiesteSupporto.SegnalazioneDTO;

@Component
@AllArgsConstructor
public class SegnalazioneMapper implements IMapper<Segnalazione, SegnalazioneDTO>{
    private final AccountMapper accountMapper;
    private final TeamMapper teamMapper;

    @Override
    public SegnalazioneDTO toDTO(Segnalazione segnalazione) {
        SegnalazioneDTO dto = new SegnalazioneDTO();
        dto.setId(segnalazione.getId());
        dto.setDescrizione(segnalazione.getDescrizione());
        dto.setDataSegnalazione(segnalazione.getDataSegnalazione());
        dto.setMentore(accountMapper.toDTO(segnalazione.getMentore()));
        dto.setTeamSegnalato(teamMapper.toDTO(segnalazione.getTeamSegnalato()));
        dto.setHackathonId(segnalazione.getHackathon().getId());
        dto.setHackathonNome(segnalazione.getHackathon().getNome());
        return dto;
    }

    public SegnalazioneDTO toSimpleDTO(Segnalazione segnalazione) {
        SegnalazioneDTO dto = new SegnalazioneDTO();
        dto.setId(segnalazione.getId());
        dto.setDescrizione(segnalazione.getDescrizione());
        dto.setDataSegnalazione(segnalazione.getDataSegnalazione());
        dto.setMentore(accountMapper.toDTO(segnalazione.getMentore()));
        dto.setTeamSegnalato(teamMapper.toSimpleDTO(segnalazione.getTeamSegnalato()));
        dto.setHackathonId(segnalazione.getHackathon().getId());
        dto.setHackathonNome(segnalazione.getHackathon().getNome());
        return dto;
    }
}

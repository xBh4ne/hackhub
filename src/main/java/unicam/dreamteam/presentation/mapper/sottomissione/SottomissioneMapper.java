package unicam.dreamteam.presentation.mapper.sottomissione;

import unicam.dreamteam.domain.model.entity.sottomissione.Sottomissione;
import unicam.dreamteam.presentation.dto.hackathon.sottomissione.SottomissioneDTO;
import unicam.dreamteam.presentation.mapper.HackathonMapper;
import unicam.dreamteam.presentation.mapper.IMapper;
import unicam.dreamteam.presentation.mapper.TeamMapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SottomissioneMapper implements IMapper<Sottomissione, SottomissioneDTO> {
    private final TeamMapper teamMapper;

    private final HackathonMapper hackathonMapper;
    private final ValutazioneMapper valutazioneMapper;

    public SottomissioneDTO toDTO(Sottomissione sottomissione) {
        return new SottomissioneDTO(
                sottomissione.getId(),
                sottomissione.getDataCaricamento(),
                sottomissione.getDataUltimoAggiornamento(),
                sottomissione.getContenuto(),
                teamMapper.toSimpleDTO(sottomissione.getTeam()),
                sottomissione.getHackathon().getId(),
                this.hackathonMapper.toSimpleDTO(sottomissione.getHackathon()),
                sottomissione.getValutazione() == null ? null : this.valutazioneMapper.toDTO(sottomissione.getValutazione())
        );
    }

    public SottomissioneDTO toSimpleDTO(Sottomissione sottomissione) {
        SottomissioneDTO sottomissioneDTO = new SottomissioneDTO();
        sottomissioneDTO.setId(sottomissione.getId());
        sottomissioneDTO.setDataCaricamento(sottomissione.getDataCaricamento());
        sottomissioneDTO.setDataUltimoAggiornamento(sottomissione.getDataUltimoAggiornamento());
        sottomissioneDTO.setContenuto(sottomissione.getContenuto());
        sottomissioneDTO.setTeam(this.teamMapper.toSimpleDTO(sottomissione.getTeam()));
        sottomissioneDTO.setHackathon(this.hackathonMapper.toSimpleDTO(sottomissione.getHackathon()));
        if (sottomissione.isValutata()) sottomissioneDTO.setValutazione(this.valutazioneMapper.toSimpleDTO(sottomissione.getValutazione()));
        return sottomissioneDTO;
    }
}

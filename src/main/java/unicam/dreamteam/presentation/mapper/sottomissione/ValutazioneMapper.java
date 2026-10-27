package unicam.dreamteam.presentation.mapper.sottomissione;

import unicam.dreamteam.domain.model.entity.sottomissione.Valutazione;
import unicam.dreamteam.presentation.dto.hackathon.sottomissione.ValutazioneDTO;
import unicam.dreamteam.presentation.mapper.AccountMapper;
import unicam.dreamteam.presentation.mapper.IMapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ValutazioneMapper implements IMapper<Valutazione, ValutazioneDTO> {
    private final AccountMapper accountMapper;

    @Override
    public ValutazioneDTO toDTO(Valutazione valutazione) {
        ValutazioneDTO dto = new ValutazioneDTO();
        dto.setId(valutazione.getId());
        dto.setPunteggio(valutazione.getPunteggio());
        dto.setGiudizio(valutazione.getGiudizio());
        dto.setGiudice(this.accountMapper.toDTO(valutazione.getGiudice()));
        return dto;
    }

    public ValutazioneDTO toSimpleDTO(Valutazione valutazione) {
        ValutazioneDTO dto = new ValutazioneDTO();
        dto.setId(valutazione.getId());
        dto.setPunteggio(valutazione.getPunteggio());
        dto.setGiudizio(valutazione.getGiudizio());
        return dto;
    }
}

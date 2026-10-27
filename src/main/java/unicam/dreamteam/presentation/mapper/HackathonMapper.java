package unicam.dreamteam.presentation.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.presentation.dto.hackathon.HackathonDTO;

@Component
@AllArgsConstructor
public class HackathonMapper implements IMapper<Hackathon, HackathonDTO> {
    private TeamMapper teamMapper;
    private AccountMapper accountMapper;

    @Override
    public HackathonDTO toDTO(Hackathon hackathon) {
        HackathonDTO dto = new HackathonDTO();
        dto.setId(hackathon.getId());
        dto.setNome(hackathon.getNome());
        dto.setDescrizione(hackathon.getDescrizione());
        dto.setRegolamento(hackathon.getRegolamento());
        dto.setDataInizio(hackathon.getDataInizio());
        dto.setDataFine(hackathon.getDataFine());
        dto.setDataScadenzaIscrizioni(hackathon.getDataScadenzaIscrizioni());
        dto.setLuogo(hackathon.getLuogo());
        dto.setPremioDenaro(hackathon.getPremioDenaro());
        dto.setDimMaxTeam(hackathon.getDimMaxTeam());
        dto.setStato(hackathon.getStato().getNome());
        dto.setOrganizzatore(hackathon.getOrganizzatore() == null ? null : accountMapper.toDTO(hackathon.getOrganizzatore()));
        dto.setGiudice(hackathon.getGiudice() == null ? null : accountMapper.toDTO(hackathon.getGiudice()));
        dto.setMentori(hackathon.getMentori().stream().map(accountMapper::toDTO).toList());
        dto.setTeamIscritti(hackathon.getTeamIscritti().stream().map(teamMapper::toSimpleDTO).toList());
        dto.setTeamVincitore(hackathon.getTeamVincitore() == null ? null : teamMapper.toDTO(hackathon.getTeamVincitore()));
        return dto;
    }

    public HackathonDTO toSimpleDTO(Hackathon hackathon) {
        HackathonDTO dto = new HackathonDTO();
        dto.setId(hackathon.getId());
        dto.setNome(hackathon.getNome());
        dto.setDescrizione(hackathon.getDescrizione());
        dto.setDataInizio(hackathon.getDataInizio());
        dto.setDataFine(hackathon.getDataFine());
        dto.setStato(hackathon.getStato().getNome());
        return dto;
    }
}

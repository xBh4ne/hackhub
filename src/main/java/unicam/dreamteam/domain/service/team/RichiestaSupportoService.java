package unicam.dreamteam.domain.service.team;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicam.dreamteam.domain.model.entity.RichiestaSupporto;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.entity.state.ticket.StatoRichiestaSupporto;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.repository.RichiestaSupportoRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class RichiestaSupportoService {
    private final RichiestaSupportoRepository richiestaSupportoRepository;

    public RichiestaSupporto crea(String oggetto, String descrizione, Team team, Staff mentore) {
        RichiestaSupporto richiesta = new RichiestaSupporto(
                oggetto, descrizione, LocalDateTime.now(), StatoRichiestaSupporto.APERTO
        );
        richiesta.setTeam(team);
        richiesta.setMentore(mentore);
        return richiestaSupportoRepository.save(richiesta);
    }

    public List<RichiestaSupporto> getAllByMentore(Staff mentore) {
        return richiestaSupportoRepository.findByMentoreId(mentore.getId());
    }

    public RichiestaSupporto getById(Long id) {
        return richiestaSupportoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RichiestaSupporto.id=" + id));
    }

    public List<RichiestaSupporto> getAllByTeam(Team team) {
        return richiestaSupportoRepository.findByTeamId(team.getId());
    }
}

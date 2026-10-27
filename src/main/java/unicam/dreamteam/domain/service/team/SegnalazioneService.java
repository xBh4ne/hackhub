package unicam.dreamteam.domain.service.team;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Segnalazione;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.repository.SegnalazioneRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SegnalazioneService {
    private final SegnalazioneRepository repository;

    public Segnalazione segnala(String descrizione, Staff mentore, Team team, Hackathon hackathon) {
        return repository.save(new Segnalazione(descrizione, mentore, team, hackathon));
    }

    public List<Segnalazione> getAll() {
        return repository.findAll();
    }

    public List<Segnalazione> getByTeam(Long teamId) {
        return repository.findByTeamSegnalatoId(teamId);
    }

    public List<Segnalazione> getByOrganizzatore(Staff organizzatore) {
        return this.repository.findAllByOrganizzatoreId(organizzatore.getId());
    }
}

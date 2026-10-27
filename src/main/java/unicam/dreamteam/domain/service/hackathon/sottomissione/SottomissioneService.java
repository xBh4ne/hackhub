package unicam.dreamteam.domain.service.hackathon.sottomissione;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.entity.sottomissione.Sottomissione;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.repository.SottomissioneRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SottomissioneService {

    private SottomissioneRepository sottomissioneRepository;

    public Sottomissione save(Sottomissione sottomissione) {
        return this.sottomissioneRepository.save(sottomissione);
    }

    public Sottomissione getByHackathonAndTeam(Hackathon hackathon, Team team) {
        Optional<Sottomissione> sottomissione = sottomissioneRepository.findByHackathonIdAndTeamId(
                hackathon.getId(),
                team.getId()
        );
        if (sottomissione.isEmpty())
            throw new EntityNotFoundException("Nessuna sottomissione trovata per il tuo team.");

        return sottomissione.get();
    }

    public Sottomissione getById(Long id) {
        Optional<Sottomissione> sottomissione = this.sottomissioneRepository.findById(id);

        if (sottomissione.isEmpty())  throw new EntityNotFoundException(
                String.format("Nessuna sottomissione trovata con l'id = %s.", id));

        return sottomissione.get();
    }

    public List<Sottomissione> getByTeam(Team team) {
        return this.sottomissioneRepository.findByTeamId(team.getId());
    }

    public List<Sottomissione> getAllByGiudice(Staff giudice) {
        return this.sottomissioneRepository.findAllByGiudice(giudice.getId());
    }
}

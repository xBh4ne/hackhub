package unicam.dreamteam.domain.service.team;

import unicam.dreamteam.domain.exception.team.TeamException;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.entity.users.Utente;
import unicam.dreamteam.domain.repository.TeamRepository;
import unicam.dreamteam.domain.repository.UtenteRepository;


import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamService {
    private TeamRepository teamRepository;
    private UtenteRepository utenteRepository;

    public List<Team> getAll() {
        return this.teamRepository.findAll();
    }

    public Team getById(Long idTeam) {
        Optional<Team> team = this.teamRepository.findById(idTeam);

        if (team.isEmpty()) throw new EntityNotFoundException(
                String.format(
                        "Team.id=%s",
                        idTeam
                )
        );
        return team.get();
    }

    public Team save(Team team) {
        return this.teamRepository.save(team);
    }

    public Team creaTeam(String nome, String descrizione, Utente owner) {
        if (owner.hasTeam()) throw new TeamException("Sei già membro di un team");
        if (teamRepository.existsByNome(nome))
            throw new TeamException(
                    String.format("Un team(nome=%s) esiste già con questo nome.", nome)
            );

        Team newTeam = new Team(nome, descrizione);
        newTeam = teamRepository.save(newTeam);
        newTeam.aggiungiMembro(owner);
        this.utenteRepository.save(owner);
        return save(newTeam);
    }

    public void delete(Team team) {
        teamRepository.deleteById(team.getId());
    }
}

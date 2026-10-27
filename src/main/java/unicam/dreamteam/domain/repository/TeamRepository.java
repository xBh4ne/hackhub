package unicam.dreamteam.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicam.dreamteam.domain.model.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByNome(String nome);
}

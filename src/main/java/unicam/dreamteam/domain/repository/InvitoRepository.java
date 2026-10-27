package unicam.dreamteam.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unicam.dreamteam.domain.model.entity.Invito;
import unicam.dreamteam.domain.model.entity.state.invito.StatoInvito;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitoRepository extends JpaRepository<Invito, Long> {
    List<Invito> findByTeamIdAndStato(@Param("teamId") Long teamId, @Param("stato") StatoInvito stato);

    List<Invito> findByStato(StatoInvito stato);
    List<Invito> findByUtenteIdAndStato(Long utenteId, StatoInvito stato);
    Optional<Invito> findAllByTeamIdAndUtenteId(Long teamId, Long utenteId, StatoInvito statoInvito);

    boolean existsByUtenteIdAndTeamId(Long idUtente, Long idTeam);
}

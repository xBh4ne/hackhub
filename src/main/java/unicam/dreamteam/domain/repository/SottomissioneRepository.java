package unicam.dreamteam.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unicam.dreamteam.domain.model.entity.sottomissione.Sottomissione;

import java.util.List;
import java.util.Optional;

@Repository
public interface SottomissioneRepository extends JpaRepository<Sottomissione, Long> {
    Optional<Sottomissione> findByHackathonIdAndTeamId(Long hackathonId, Long teamId);

    List<Sottomissione> findByTeamId(Long id);

    @Query("SELECT s FROM Sottomissione s WHERE s.hackathon.giudice.id = :giudiceId ")
    List<Sottomissione> findAllByGiudice(@Param("giudiceId") Long giudiceId);
}

package unicam.dreamteam.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unicam.dreamteam.domain.model.entity.Segnalazione;

import java.util.List;

@Repository
public interface SegnalazioneRepository extends JpaRepository<Segnalazione, Long> {
    List<Segnalazione> findByTeamSegnalatoId(Long teamId);
    List<Segnalazione> findByMentoreId(Long mentoreId);

    @Query("SELECT s FROM Segnalazione s WHERE s.teamSegnalato IN (SELECT t FROM Team t JOIN t.hackathons h WHERE h.organizzatore.id = :organizzatoreId)")
    List<Segnalazione> findAllByOrganizzatoreId(@Param("organizzatoreId") Long organizzatoreId);
}

package unicam.dreamteam.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.dreamteam.domain.model.entity.RichiestaSupporto;

import java.util.List;

@Repository
public interface RichiestaSupportoRepository extends JpaRepository<RichiestaSupporto, Long> {
    List<RichiestaSupporto> findByMentoreId(Long mentoreId);
    List<RichiestaSupporto> findByTeamId(Long teamId);
}

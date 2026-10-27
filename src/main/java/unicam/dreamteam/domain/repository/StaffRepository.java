package unicam.dreamteam.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloStaff;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUsername(String username);
    List<Staff> findByRuolo(RuoloStaff ruolo);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

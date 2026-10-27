package unicam.dreamteam.domain.service.accounts;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloStaff;
import unicam.dreamteam.domain.validator.RuoloValidator;
import unicam.dreamteam.domain.repository.StaffRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StaffService {
    private StaffRepository staffRepository;
    private RuoloValidator ruoloValidator;

    public List<Staff> getAll() {
        return this.staffRepository.findAll();
    }

    public Staff getById(Long id) {
        Optional<Staff> staff = this.staffRepository.findById(id);

        if (staff.isEmpty()) throw new EntityNotFoundException(
                String.format(
                        "Staff.id=%s",
                        id
                )
        );
        return staff.get();
    }

    public Staff getByUsername(String username) {
        Optional<Staff> staff = this.staffRepository.findByUsername(username);

        if (staff.isEmpty()) throw new EntityNotFoundException(
                String.format(
                        "Staff.username=%s",
                        username
                )
        );
        return staff.get();
    }

    public List<Staff> getAllByRuolo(RuoloStaff ruolo) {
        return this.staffRepository.findByRuolo(ruolo);
    }

    public List<Staff> getAllById(List<Long> ids) {
        return this.staffRepository.findAllById(ids);
    }
}

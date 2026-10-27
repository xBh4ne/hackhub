package unicam.dreamteam.domain.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import unicam.dreamteam.domain.model.entity.users.Staff;

@Component
@AllArgsConstructor
public class StaffValidator {
    private RuoloValidator ruoloValidator;

    public void validaMentore(Staff mentore) {
        this.ruoloValidator.validaMentore(mentore.getRuolo());
    }

    public void validaGiudice(Staff giudice) {
        this.ruoloValidator.validaGiudice(giudice.getRuolo());
    }

    public void validaOrganizzatore(Staff organizzatore) {
        this.ruoloValidator.validaOrganizzatore(organizzatore.getRuolo());
    }

    public void validaAdmin(Staff admin) {
        this.ruoloValidator.validaAdmin(admin.getRuolo());
    }
}

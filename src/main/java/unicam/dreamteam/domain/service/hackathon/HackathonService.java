package unicam.dreamteam.domain.service.hackathon;

import jakarta.persistence.EntityNotFoundException;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.builder.HackathonBuilder;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.repository.HackathonRepository;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import unicam.dreamteam.domain.validator.StaffValidator;
import unicam.dreamteam.presentation.dto.hackathon.HackathonDTO;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HackathonService {
    private final HackathonRepository hackathonRepository;
    private final StaffValidator staffValidator;

    public List<Hackathon> getAll() {
        return this.hackathonRepository.findAll();
    }

    public Hackathon getById(Long id) {
        Optional<Hackathon> hackathon = this.hackathonRepository.findById(id);
        if (hackathon.isEmpty()) throw new EntityNotFoundException(
                String.format(
                        "Hackathon.id=%s",
                        id
                )
        );
        return hackathon.get();
    }

    public List<Hackathon> getAllByStaff(Staff staff) {
        return switch (staff.getRuolo()) {
            case ADMIN -> getAll();
            case GIUDICE -> getAllByGiudice(staff);
            case ORGANIZZATORE -> getAllByOrganizzatore(staff);
            case MENTORE -> getAllByMentore(staff);
        };
    }

    public List<Hackathon> getAllByGiudice(Staff giudice) {
        this.staffValidator.validaGiudice(giudice);

        return this.hackathonRepository.findAllByGiudiceId(giudice.getId());
    }

    public List<Hackathon> getAllByMentore(Staff mentore) {
        this.staffValidator.validaMentore(mentore);

        return this.hackathonRepository.findAllByMentoriId(mentore.getId());
    }

    public List<Hackathon> getAllByOrganizzatore(Staff organizzatore) {
        this.staffValidator.validaOrganizzatore(organizzatore);

        return this.hackathonRepository.findAllByOrganizzatoreId(organizzatore.getId());
    }

    public List<Hackathon> getAllByTeam(Team team) {
        return this.hackathonRepository.findAllByTeamIscrittiId(team.getId());
    }

    public Hackathon aggiungiMentore(Hackathon hackathon, Staff mentore) {
        this.staffValidator.validaMentore(mentore);

        hackathon.aggiungiMentore(mentore);
        return save(hackathon);
    }

    public Hackathon rimuoviMentore(Hackathon hackathon, Staff mentore) {
        this.staffValidator.validaMentore(mentore);

        hackathon.getMentori().remove(mentore);
        return save(hackathon);
    }

    public Hackathon save(Hackathon hackathon) {
        return this.hackathonRepository.save(hackathon);
    }

    public Hackathon crea(HackathonDTO request, Staff organizzatore, Staff giudice) {
        Hackathon newHackathon = new HackathonBuilder()
                .nome(request.getNome())
                .descrizione(request.getDescrizione())
                .regolamento(request.getRegolamento())
                .dataInizio(request.getDataInizio())
                .dataFine(request.getDataFine())
                .dataScadenzaIscrizioni(request.getDataScadenzaIscrizioni())
                .premioDenaro(request.getPremioDenaro())
                .luogo(request.getLuogo())
                .dimMaxTeam(request.getDimMaxTeam())
                .organizzatore(organizzatore)
                .giudice(giudice)
                .build();
        return save(newHackathon);
    }
}

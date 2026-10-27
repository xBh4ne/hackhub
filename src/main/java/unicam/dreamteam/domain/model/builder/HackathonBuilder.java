package unicam.dreamteam.domain.model.builder;

import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.users.Staff;

import java.time.LocalDateTime;

public class HackathonBuilder implements Builder<Hackathon> {
    private Hackathon hackathon;

    public HackathonBuilder() {
        this.hackathon = new Hackathon();
    }

    public HackathonBuilder nome(String nome) {
        hackathon.setNome(nome);
        return this;
    }

    public HackathonBuilder descrizione(String descrizione) {
        hackathon.setDescrizione(descrizione);
        return this;
    }

    public HackathonBuilder regolamento(String regolamento) {
        hackathon.setRegolamento(regolamento);
        return this;
    }

    public HackathonBuilder dataInizio(LocalDateTime dataInizio) {
        hackathon.setDataInizio(dataInizio);
        return this;
    }

    public HackathonBuilder dataFine(LocalDateTime dataFine) {
        hackathon.setDataFine(dataFine);
        return this;
    }

    public HackathonBuilder dataScadenzaIscrizioni(LocalDateTime data) {
        hackathon.setDataScadenzaIscrizioni(data);
        return this;
    }

    public HackathonBuilder luogo(String luogo) {
        hackathon.setLuogo(luogo);
        return this;
    }

    public HackathonBuilder premioDenaro(Double premio) {
        hackathon.setPremioDenaro(premio);
        return this;
    }

    public HackathonBuilder dimMaxTeam(Integer dim) {
        hackathon.setDimMaxTeam(dim);
        return this;
    }

    public HackathonBuilder organizzatore(Staff organizzatore) {
        hackathon.setOrganizzatore(organizzatore);
        return this;
    }

    public HackathonBuilder giudice(Staff giudice) {
        hackathon.setGiudice(giudice);
        return this;
    }

    @Override
    public Hackathon build() {
        validate();
        return hackathon;
    }

    @Override
    public HackathonBuilder reset() {
        this.hackathon = new Hackathon();
        return this;
    }

    private void validate() {
        if (hackathon.getNome() == null || hackathon.getNome().isBlank())
            throw new IllegalStateException("nome è obbligatorio");
        if (hackathon.getRegolamento() == null || hackathon.getRegolamento().isBlank())
            throw new IllegalStateException("regolamento è obbligatorio");
        if (hackathon.getDataInizio() == null)
            throw new IllegalStateException("dataInizio è obbligatoria");
        if (hackathon.getDataFine() == null)
            throw new IllegalStateException("dataFine è obbligatoria");
        if (hackathon.getDataScadenzaIscrizioni() == null)
            throw new IllegalStateException("dataScadenzaIscrizioni è obbligatoria");
        if (hackathon.getOrganizzatore() == null)
            throw new IllegalStateException("organizzatore è obbligatorio");
        if (!hackathon.getDataFine().isAfter(hackathon.getDataInizio()))
            throw new IllegalStateException("dataFine deve essere successiva a dataInizio");
        if (!hackathon.getDataScadenzaIscrizioni().isBefore(hackathon.getDataInizio()))
            throw new IllegalStateException("dataScadenzaIscrizioni deve precedere dataInizio");
    }
}
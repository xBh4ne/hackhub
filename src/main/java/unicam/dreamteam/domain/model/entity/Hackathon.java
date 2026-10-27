package unicam.dreamteam.domain.model.entity;

import lombok.*;
import unicam.dreamteam.domain.model.entity.users.Staff;
import unicam.dreamteam.domain.model.entity.sottomissione.Sottomissione;
import unicam.dreamteam.domain.model.entity.state.hackathon.StatoHackathon;
import unicam.dreamteam.domain.model.entity.state.hackathon.StatoHackathonCreato;
import unicam.dreamteam.infrastructure.persistence.converter.StatoHackathonConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hackathons")
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hackathon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String descrizione;

    @Column(nullable = false)
    private String regolamento;

    @Column(nullable = false)
    private LocalDateTime dataInizio;

    @Column(nullable = false)
    private LocalDateTime dataFine;

    @Column(nullable = false)
    private LocalDateTime dataScadenzaIscrizioni;

    private String luogo;
    private Double premioDenaro;
    private Integer dimMaxTeam;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizzatore_id", nullable = false)
    private Staff organizzatore;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "giudice_id")
    private Staff giudice;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "hackathon_mentori",
            joinColumns = @JoinColumn(name = "hackathon_id"),
            inverseJoinColumns = @JoinColumn(name = "mentore_id")
    )
    private Set<Staff> mentori = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vincitore_id")
    private Team teamVincitore;

    @ManyToMany(mappedBy = "hackathons", fetch = FetchType.EAGER)
    private Set<Team> teamIscritti = new HashSet<>();

    @OneToMany(mappedBy = "hackathon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Sottomissione> sottomissioni = new HashSet<>();

    @Convert(converter = StatoHackathonConverter.class)
    @Column(nullable = false)
    private StatoHackathon stato = new StatoHackathonCreato();

    private boolean teamVincitorePagato = false;

    public Hackathon(
            Long id,
            String nome,
            String descrizione,
            String regolamento,
            LocalDateTime dataInizio,
            LocalDateTime dataFine,
            LocalDateTime dataScadenzaIscrizioni,
            String luogo,
            Double premioDenaro,
            Integer dimMaxTeam,
            Staff organizzatore,
            Staff giudice) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.regolamento = regolamento;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.dataScadenzaIscrizioni = dataScadenzaIscrizioni;
        this.luogo = luogo;
        this.premioDenaro = premioDenaro;
        this.dimMaxTeam = dimMaxTeam;
        this.organizzatore = organizzatore;
        this.giudice = giudice;
    }

    public Hackathon(
            String nome,
            String descrizione,
            String regolamento,
            LocalDateTime dataInizio,
            LocalDateTime dataFine,
            LocalDateTime dataScadenzaIscrizioni,
            String luogo,
            Double premioDenaro,
            Integer dimMaxTeam,
            Staff organizzatore,
            Staff giudice) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.regolamento = regolamento;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.dataScadenzaIscrizioni = dataScadenzaIscrizioni;
        this.luogo = luogo;
        this.premioDenaro = premioDenaro;
        this.dimMaxTeam = dimMaxTeam;
        this.organizzatore = organizzatore;
        this.giudice = giudice;
    }

    public void aggiungiMentore(Staff mentore) {
        this.mentori.add(mentore);
    }
    public void iscrivi(Team team) {
        this.stato.iscrivi(this, team);
    }

    public void apriIscrizioni() {
        this.stato.apriIscrizioni(this);
    }

    public void avvia() {
        this.stato.avvia(this);
    }

    public void inviaSottomissione(Sottomissione sottomissione) {
        this.stato.inviaSottomissione(this, sottomissione);
    }

    public boolean hasSottomissione(Team team) {
        return this.sottomissioni.stream()
                .anyMatch(s -> s.getTeam().getId().equals(team.getId()));
    }

    public void avviaValutazione() {
        this.stato.avviaValutazione(this);
    }

    public void concludiValutazione() {
        this.stato.concludiValutazione(this);
    }

    public void proclamaVincitore(Team team) {
        this.stato.proclamaVincitore(this, team);
    }
}

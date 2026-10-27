package unicam.dreamteam.domain.model.entity.sottomissione;

import lombok.AccessLevel;
import unicam.dreamteam.domain.model.entity.Hackathon;
import unicam.dreamteam.domain.model.entity.Team;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sottomissioni")
@Access(AccessType.FIELD)
@Getter
@Setter
public class Sottomissione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataCaricamento;

    private LocalDateTime dataUltimoAggiornamento;

    @Column(nullable = false)
    private String contenuto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    @OneToOne(mappedBy = "sottomissione", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Valutazione valutazione;

    public Sottomissione(String contenuto, Team team, Hackathon hackathon) {
        this.contenuto = contenuto;
        this.team = team;
        this.hackathon = hackathon;
        this.dataCaricamento = LocalDateTime.now();
        this.dataUltimoAggiornamento = dataCaricamento;
    }

    public Sottomissione() {
    }

    public void aggiorna(String nuovoContenuto) {
        if (isValutata()) throw new RuntimeException("Non consentito aggiornare una sottomissione valutata.");
        this.contenuto = nuovoContenuto;
        this.dataUltimoAggiornamento = LocalDateTime.now();
    }

    public boolean isValutata() {
        return this.valutazione != null;
    }
}

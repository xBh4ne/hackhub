package unicam.dreamteam.domain.model.entity.sottomissione;

import jakarta.persistence.*;
import lombok.Getter;
import unicam.dreamteam.domain.model.entity.users.Staff;

@Entity
@Table(name = "valutazioni")
@Access(AccessType.FIELD)
@Getter
public class Valutazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer punteggio;

    @Column(nullable = false)
    private String giudizio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "giudice_id", nullable = false)
    private Staff giudice;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sottomissione_id", nullable = false)
    private Sottomissione sottomissione;

    public Valutazione(Sottomissione sottomissione, Integer punteggio, String giudizio, Staff giudice) {
        this.sottomissione = sottomissione;
        this.punteggio = punteggio;
        this.giudizio = giudizio;
        this.giudice = giudice;
    }

    public Valutazione() {}

    public void aggiorna(Integer punteggio, String giudizio) {
        this.punteggio = punteggio;
        this.giudizio = giudizio;
    }
}

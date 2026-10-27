package unicam.dreamteam.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import unicam.dreamteam.domain.model.entity.users.Staff;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "segnalazioni")
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Segnalazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private LocalDateTime dataSegnalazione;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentore_id", nullable = false)
    private Staff mentore;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    private Team teamSegnalato;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    public Segnalazione(String descrizione, Staff mentore, Team teamSegnalato, Hackathon hackathon) {
        this.descrizione = descrizione;
        this.mentore = mentore;
        this.teamSegnalato = teamSegnalato;
        this.dataSegnalazione = LocalDateTime.now();
        this.hackathon = hackathon;
    }
}

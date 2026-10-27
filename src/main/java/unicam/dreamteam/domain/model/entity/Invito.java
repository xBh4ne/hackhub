package unicam.dreamteam.domain.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.dreamteam.domain.model.entity.state.invito.StatoInvito;
import unicam.dreamteam.domain.model.entity.state.invito.StatoInvitoPendente;
import unicam.dreamteam.domain.model.entity.users.Utente;
import unicam.dreamteam.infrastructure.persistence.converter.StatoInvitoConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "inviti")
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Invito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataInvito;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @Convert(converter = StatoInvitoConverter.class)
    @Column(nullable = false)
    private StatoInvito stato;

    public Invito(Team team, Utente destinatario) {
        this.team = team;
        this.utente = destinatario;
        this.dataInvito = LocalDateTime.now();
        this.stato = new StatoInvitoPendente();
    }

    public void accetta() {
        this.stato = this.stato.accetta();
        this.team.aggiungiMembro(this.utente);
    }

    public void rifiuta() {
        this.stato = this.stato.rifiuta();
    }
}

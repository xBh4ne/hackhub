package unicam.dreamteam.domain.model.entity;

import unicam.dreamteam.domain.exception.team.TeamException;
import unicam.dreamteam.domain.model.entity.sottomissione.Sottomissione;
import unicam.dreamteam.domain.model.entity.users.Utente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "teams")
@Access(AccessType.FIELD)
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Setter
    private String nome; // TODO: Mettere un dispalyName e name(una sorta di username ma per il team)

    @Column
    @Setter
    private String descrizione;

    @Column
    @Setter
    private String email;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private final Set<Sottomissione> sottomissioni = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "team_utenti",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "utente_id")
    )
    private final Set<Utente> membri = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "team_hackathons",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "hackathon_id")
    )
    private final Set<Hackathon> hackathons = new HashSet<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Invito> inviti = new HashSet<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<RichiestaSupporto> richiesteSupporto = new HashSet<>();

    @OneToMany(mappedBy = "teamSegnalato", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Segnalazione> segnalazioni = new HashSet<>();

    public Team() {
    }

    public Team(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public void aggiungiMembro(Utente utente) {
        if (utente.getTeam() != null) throw new TeamException("L'utente è già in un team");
        membri.add(utente);
        utente.setTeam(this);
    }

    public void rimuoviMembro(Utente utente) {
        membri.remove(utente);
        utente.setTeam(null);
    }

    public boolean isEmpty() {
        return this.membri.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Team team
                && Objects.equals(id, team.id)
                && Objects.equals(nome, team.nome);
    }
}
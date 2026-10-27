package unicam.dreamteam.domain.model.entity.users;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Setter;
import unicam.dreamteam.domain.model.entity.Team;
import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloUtente;

import java.util.Objects;

@Entity
@Table(name = "utenti")
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Utente implements Autenticabile, MembroTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloUtente ruolo;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Utente(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.ruolo = RuoloUtente.UTENTE;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Utente utente
                && Objects.equals(id, utente.id)
                && Objects.equals(username, utente.username)
                && Objects.equals(email, utente.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
}

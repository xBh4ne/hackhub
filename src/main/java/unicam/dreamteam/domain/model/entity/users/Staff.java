package unicam.dreamteam.domain.model.entity.users;

import unicam.dreamteam.domain.model.entity.users.ruolo.RuoloStaff;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "staff")
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Staff implements Autenticabile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloStaff ruolo;

    public Staff(String username, String email, String password, RuoloStaff ruolo) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Staff staff
                && Objects.equals(id, staff.id)
                && Objects.equals(username, staff.username)
                && Objects.equals(email, staff.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
}

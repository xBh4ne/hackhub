package unicam.dreamteam.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "calls")
@Getter
@Setter
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String linkMeeting;

    @Column(nullable = false)
    private LocalDateTime dataOra;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "richiesta_supporto_id", nullable = false)
    private RichiestaSupporto richiestaSupporto;

    public Call() {}
}

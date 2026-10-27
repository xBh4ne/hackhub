package unicam.dreamteam.presentation.dto.hackathon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.dreamteam.presentation.dto.security.AccountDTO;
import unicam.dreamteam.presentation.dto.team.TeamDTO;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HackathonDTO {
    private Long id;
    private String nome;
    private String descrizione;
    private String regolamento;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm") private LocalDateTime dataInizio;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm") private LocalDateTime dataFine;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm") private LocalDateTime dataScadenzaIscrizioni;
    private String luogo;
    private Double premioDenaro;
    private Integer dimMaxTeam;
    private Long giudiceId;
    private String stato;
    private AccountDTO organizzatore;
    private AccountDTO giudice;
    private List<AccountDTO> mentori;
    private List<TeamDTO> teamIscritti;
    private TeamDTO teamVincitore;
}

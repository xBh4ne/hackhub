package unicam.dreamteam.presentation.dto.team.richiesteSupporto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.dreamteam.presentation.dto.security.AccountDTO;
import unicam.dreamteam.presentation.dto.team.TeamDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SegnalazioneDTO {
    private Long id;
    private String descrizione;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm") private LocalDateTime dataSegnalazione;
    private AccountDTO mentore;
    private TeamDTO teamSegnalato;
    private Long hackathonId;
    private String hackathonNome;
}

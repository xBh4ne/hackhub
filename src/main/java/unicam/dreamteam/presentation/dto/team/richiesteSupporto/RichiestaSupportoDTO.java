package unicam.dreamteam.presentation.dto.team.richiesteSupporto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.dreamteam.presentation.dto.security.AccountDTO;
import unicam.dreamteam.presentation.dto.team.TeamDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RichiestaSupportoDTO {
    private Long id;
    @NotBlank(message = "L'oggetto non può essere vuoto") private String oggetto;
    @NotBlank(message = "La descrizione non può essere vuota") private String descrizione;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm") private LocalDateTime dataInvio;
    private String stato;
    private AccountDTO mentore;
    private TeamDTO team;
    private List<CallDTO> calls;
}

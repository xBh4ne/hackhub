package unicam.dreamteam.presentation.dto.hackathon.sottomissione;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.dreamteam.presentation.dto.hackathon.HackathonDTO;
import unicam.dreamteam.presentation.dto.team.TeamDTO;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SottomissioneDTO {
    private Long id;
    private @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataCaricamento;
    private @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataUltimoAggiornamento;
    private String contenuto;
    private TeamDTO team;
    private Long hackathonId;
    private HackathonDTO hackathon;
    private ValutazioneDTO valutazione;
}
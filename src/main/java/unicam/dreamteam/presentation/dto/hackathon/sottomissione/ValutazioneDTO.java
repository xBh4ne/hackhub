package unicam.dreamteam.presentation.dto.hackathon.sottomissione;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.dreamteam.presentation.dto.security.AccountDTO;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ValutazioneDTO {
    private Long id;
    private SottomissioneDTO sottomissione;
    private Integer punteggio;
    private String giudizio;
    private AccountDTO giudice;
}

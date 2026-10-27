package unicam.dreamteam.presentation.dto.team.richiesteSupporto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CallDTO {
    private Long id;
    private String linkMeeting;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm") private LocalDateTime dataOra;
}

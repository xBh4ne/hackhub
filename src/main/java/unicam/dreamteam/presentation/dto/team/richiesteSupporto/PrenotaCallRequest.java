package unicam.dreamteam.presentation.dto.team.richiesteSupporto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PrenotaCallRequest(
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataOra
) {}

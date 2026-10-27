package unicam.dreamteam.presentation.dto.team;

import com.fasterxml.jackson.annotation.JsonFormat;
import unicam.dreamteam.presentation.dto.security.AccountDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record InvitoDTO(
        Long id,
        TeamDTO team,
        AccountDTO utenteInvitato,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataInvito,
        String stato
) {}

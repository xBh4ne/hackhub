package unicam.dreamteam.infrastructure.exception.team;

import unicam.dreamteam.domain.exception.team.TeamException;
import unicam.dreamteam.infrastructure.api.response.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class TeamExceptionHandler {
    @ExceptionHandler(TeamException.class)
    public ResponseEntity<Response> handleTeamException(TeamException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Response error = new Response(
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }
}

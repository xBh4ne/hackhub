package unicam.dreamteam.infrastructure.exception.team;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import unicam.dreamteam.domain.exception.invito.InvitoException;
import unicam.dreamteam.domain.exception.team.TeamException;
import unicam.dreamteam.infrastructure.api.response.Response;

import java.time.LocalDateTime;

@ControllerAdvice
public class InvitoExceptionHandler {
    @ExceptionHandler(InvitoException.class)
    public ResponseEntity<Response> handleInvitoException(InvitoException exception) {
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

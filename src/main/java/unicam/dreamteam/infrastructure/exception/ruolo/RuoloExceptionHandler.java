package unicam.dreamteam.infrastructure.exception.ruolo;

import unicam.dreamteam.domain.exception.ruolo.RuoloException;
import unicam.dreamteam.infrastructure.api.response.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RuoloExceptionHandler {
    @ExceptionHandler(RuoloException.class)
    public ResponseEntity<Response> handleRuoloNonAutorizzatoException(RuoloException exception) {
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

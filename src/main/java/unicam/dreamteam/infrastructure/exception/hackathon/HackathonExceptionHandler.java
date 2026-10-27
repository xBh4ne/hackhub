package unicam.dreamteam.infrastructure.exception.hackathon;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import unicam.dreamteam.domain.exception.hackathon.HackathonException;
import unicam.dreamteam.infrastructure.api.response.Response;

import java.time.LocalDateTime;

@ControllerAdvice
public class HackathonExceptionHandler {
    @ExceptionHandler(HackathonException.class)
    public ResponseEntity<Response> handleHackathonException(HackathonException exception) {
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

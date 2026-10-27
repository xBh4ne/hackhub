package unicam.dreamteam.infrastructure.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import unicam.dreamteam.infrastructure.api.response.Response;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Response> handleEntityExistsException(EntityExistsException exception) {
        HttpStatus status = HttpStatus.CONFLICT;
        Response error = new Response(
                status.value(),
                status.getReasonPhrase(),
                String.format(
                        "L'entità esiste già. Con queste informazioni: %s",
                        exception.getMessage()
                ),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Response> handleEntityNotFoundException(EntityNotFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Response error = new Response(
                status.value(),
                status.getReasonPhrase(),
                String.format(
                        "Entità non trovata. %s",
                        exception.getMessage()
                ),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Response> handleValidationException(ValidationException exception) {
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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Response> handleMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        return ResponseEntity.status(status).body(new Response(
                status.value(),
                status.getReasonPhrase(),
                String.format("Metodo '%s' non supportato per questo endpoint.", exception.getMethod()),
                LocalDateTime.now()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Response error = new Response(
                status.value(),
                status.getReasonPhrase(),
                "Argomenti non validi.",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Response> handleIllegalStateException(IllegalStateException exception) {
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

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Response> handleNoResourceFound(NoResourceFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Response error = new Response(
                status.value(),
                status.getReasonPhrase(),
                "Risorsa non trovata.",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleRuntimeException(RuntimeException exception) {
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

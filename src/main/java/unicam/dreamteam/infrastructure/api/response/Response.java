package unicam.dreamteam.infrastructure.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
public class Response {
    private int code;
    private String status;
    private String message;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    public Response(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}

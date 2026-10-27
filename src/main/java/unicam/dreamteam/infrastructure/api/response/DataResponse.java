package unicam.dreamteam.infrastructure.api.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataResponse<T> extends Response {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    private final T object;

    public DataResponse(int code, String status, String message, T object) {
        super(code, status, message);
        this.object = object;
    }
}

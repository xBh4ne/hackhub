package unicam.dreamteam.presentation.dto.team;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.dreamteam.presentation.dto.security.AccountDTO;
import java.util.List;

@JsonRootName("team")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamDTO {
    private Long id;
    private String nome;
    private String descrizione;
    private List<AccountDTO> membri;
}

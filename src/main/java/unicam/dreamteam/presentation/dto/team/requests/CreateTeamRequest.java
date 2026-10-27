package unicam.dreamteam.presentation.dto.team.requests;

import jakarta.validation.constraints.NotBlank;
import unicam.dreamteam.domain.model.entity.Invito;

import java.util.Set;

public record CreateTeamRequest (
        @NotBlank(message = "Team name è obbligatorio")
        String name,
        String descrizione,
        Set<Invito> inviti
) {}

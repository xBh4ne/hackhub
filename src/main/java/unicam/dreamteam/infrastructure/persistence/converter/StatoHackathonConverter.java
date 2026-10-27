package unicam.dreamteam.infrastructure.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import unicam.dreamteam.domain.model.entity.state.hackathon.*;

@Converter(autoApply = true)
public class StatoHackathonConverter implements AttributeConverter<StatoHackathon, String> {
    @Override
    public String convertToDatabaseColumn(StatoHackathon stato) {
        return stato == null ? null : stato.getNome();
    }

    @Override
    public StatoHackathon convertToEntityAttribute(String name) {
        if (name == null) return null;
        return switch (name) {
            case "CREATO"     -> new StatoHackathonCreato();
            case "ISCRIZIONE"    -> new StatoHackathonIscrizione();
            case "IN_CORSO"    -> new StatoHackathonInCorso();
            case "VALUTAZIONE"    -> new StatoHackathonValutazione();
            case "PROCLAMAZIONE_VINCITORE" -> new StatoHackathonProclamazioneVincitore();
            case "CONCLUSO" -> new StatoHackathonConcluso();
            default -> throw new IllegalArgumentException(String.format("Stato sconosciuto: %s", name));
        };
    }
}

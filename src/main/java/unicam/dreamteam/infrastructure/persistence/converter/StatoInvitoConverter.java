package unicam.dreamteam.infrastructure.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import unicam.dreamteam.domain.model.entity.state.invito.StatoInvito;
import unicam.dreamteam.domain.model.entity.state.invito.StatoInvitoAccettato;
import unicam.dreamteam.domain.model.entity.state.invito.StatoInvitoPendente;
import unicam.dreamteam.domain.model.entity.state.invito.StatoInvitoRifiutato;

@Converter(autoApply = true)
public class StatoInvitoConverter implements AttributeConverter<StatoInvito, String> {

    @Override
    public String convertToDatabaseColumn(StatoInvito stato) {
        if (stato == null) return null;
        return stato.getNome();
    }

    @Override
    public StatoInvito convertToEntityAttribute(String dbValue) {
        if (dbValue == null) return null;
        return switch (dbValue) {
            case "PENDENTE"  -> new StatoInvitoPendente();
            case "ACCETTATO" -> new StatoInvitoAccettato();
            case "RIFIUTATO" -> new StatoInvitoRifiutato();
            default -> throw new IllegalArgumentException("Stato sconosciuto: " + dbValue);
        };
    }
}

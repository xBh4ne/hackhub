package unicam.dreamteam.infrastructure.adapter;

import org.springframework.stereotype.Component;
import unicam.dreamteam.domain.adapter.CalendarAdapter;
import unicam.dreamteam.domain.model.entity.Call;
import unicam.dreamteam.domain.model.entity.RichiestaSupporto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Template Implementazione sistema di Prenotazione di Slot per richiesta di supporto
 */
@Component
public class CalendarAdapterImpl implements CalendarAdapter {
    @Override
    public Call prenotaSlot(RichiestaSupporto richiestaSupporto, LocalDateTime dataOra) {
        throw new RuntimeException("Non implmentato");
    }

    @Override
    public List<Call> getCallByMentore(Long mentoreId) {
        throw new RuntimeException("Non implmentato");
    }

    @Override
    public List<Call> getCallByTeam(Long teamId) {
        throw new RuntimeException("Non implmentato");
    }
}

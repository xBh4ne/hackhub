package unicam.dreamteam.domain.adapter;

import unicam.dreamteam.domain.model.entity.Call;
import unicam.dreamteam.domain.model.entity.RichiestaSupporto;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarAdapter {
    Call prenotaSlot(RichiestaSupporto richiestaSupporto, LocalDateTime dataOra);
    List<Call> getCallByMentore(Long mentoreId);
    List<Call> getCallByTeam(Long teamId);
}

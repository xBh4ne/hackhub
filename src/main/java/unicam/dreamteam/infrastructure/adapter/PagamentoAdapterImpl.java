package unicam.dreamteam.infrastructure.adapter;

import org.springframework.stereotype.Component;
import unicam.dreamteam.domain.adapter.PagamentoAdapter;

/**
 * Template Implementazione sistema di Pagamento di Team Vincitori
 */
@Component
public class PagamentoAdapterImpl implements PagamentoAdapter {

    @Override
    public void pagaPremio(Long hackathonId, Long teamId, double importo) {
        throw new RuntimeException("Non implementato");
    }
}

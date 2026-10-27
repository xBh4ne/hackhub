package unicam.dreamteam.domain.model.entity.state.invito;

public interface StatoInvito {
    StatoInvito accetta();
    StatoInvito rifiuta();
    String getNome();
}

package unicam.dreamteam.domain.model.entity.state.invito;

public class StatoInvitoPendente implements StatoInvito {

    @Override
    public StatoInvito accetta() {
        return new StatoInvitoAccettato();
    }

    @Override
    public StatoInvito rifiuta() {
        return new StatoInvitoRifiutato();
    }

    @Override
    public String getNome() { return "PENDENTE"; }
}

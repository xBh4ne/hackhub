package unicam.dreamteam.domain.model.entity.state.invito;

import unicam.dreamteam.domain.exception.invito.InvitoException;

public class StatoInvitoAccettato implements StatoInvito {

    @Override
    public StatoInvito accetta() {
        throw new InvitoException("L'invito è già stato accettato");
    }

    @Override
    public StatoInvito rifiuta() {
        throw new InvitoException("Non puoi rifiutare un invito già accettato");
    }

    @Override
    public String getNome() { return "ACCETTATO"; }
}

package unicam.dreamteam.domain.model.entity.state.invito;

import unicam.dreamteam.domain.exception.invito.InvitoException;

public class StatoInvitoRifiutato implements StatoInvito {

    @Override
    public StatoInvito accetta() {
        throw new InvitoException("Non puoi accettare un invito rifiutato");
    }

    @Override
    public StatoInvito rifiuta() {
        throw new InvitoException("L'invito è già stato rifiutato");
    }

    @Override
    public String getNome() { return "RIFIUTATO"; }
}

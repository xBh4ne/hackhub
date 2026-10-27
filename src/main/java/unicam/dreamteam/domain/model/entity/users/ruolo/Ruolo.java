package unicam.dreamteam.domain.model.entity.users.ruolo;

public interface Ruolo {
    /**
     * Restituisce la rappresentazione testuale del ruolo
     * utilizzata come authority nel sistema di sicurezza.
     *
     * @return la authority associata al ruolo
     */
    String toAuthority();

    String getName();

}
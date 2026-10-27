package unicam.dreamteam.domain.model.builder;

public interface Builder<T> {

    /**
     * Costruisce ed effettua la validazione finale dell'oggetto.
     *
     * @return l'istanza costruita
     * @throws IllegalStateException se lo stato del builder non è valido
     */
    T build();

    Builder<T> reset();
}

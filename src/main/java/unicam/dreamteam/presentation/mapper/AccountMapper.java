package unicam.dreamteam.presentation.mapper;

import unicam.dreamteam.domain.model.entity.users.Autenticabile;
import unicam.dreamteam.presentation.dto.security.AccountDTO;

import org.springframework.stereotype.Component;

@Component
public class AccountMapper implements IMapper<Autenticabile, AccountDTO> {
    @Override
    public AccountDTO toDTO(Autenticabile autenticabile) {
        return new AccountDTO(
                autenticabile.getId(),
                autenticabile.getUsername(),
                autenticabile.getEmail(),
                autenticabile.getRuolo().getName()
        );
    }
}

package igorekpotworek.transfer.service;

import igorekpotworek.transfer.domain.Account;
import igorekpotworek.transfer.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Singleton
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    public Account create(BigDecimal balance) {
        return accountRepository.save(Account.of(balance));
    }

    public Account findById(String id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }
}

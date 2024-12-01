package revolut.transfer.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import revolut.transfer.domain.Account;
import revolut.transfer.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

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

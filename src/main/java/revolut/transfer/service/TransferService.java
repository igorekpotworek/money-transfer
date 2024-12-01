package revolut.transfer.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import revolut.transfer.dto.TransferDto;
import revolut.transfer.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Singleton
public class TransferService {

    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(TransferDto transferDto) {
        if (transferDto.getDestinationAccountId().compareTo(transferDto.getSourceAccountId()) < 0) {
            withdraw(transferDto.getSourceAccountId(), transferDto.getAmount());
            deposit(transferDto.getDestinationAccountId(), transferDto.getAmount());
        } else {
            deposit(transferDto.getDestinationAccountId(), transferDto.getAmount());
            withdraw(transferDto.getSourceAccountId(), transferDto.getAmount());
        }
    }

    private void withdraw(String accountId, BigDecimal amount) {
        var destination = accountRepository.findByIdPessimistic(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        accountRepository.update(destination.withdraw(amount));
    }

    private void deposit(String accountId, BigDecimal amount) {
        var source = accountRepository.findByIdPessimistic(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        accountRepository.update(source.deposit(amount));
    }
}

package revolut.transfer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import revolut.transfer.service.InsufficientBalanceException;
import lombok.Value;

import java.math.BigDecimal;

import static java.util.UUID.randomUUID;

@Value
@Entity
public class Account {
    @Id
    String id;
    BigDecimal balance;

    public Account(String id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public static Account of(BigDecimal balance) {
        return new Account(randomUUID().toString(), balance);
    }

    public Account withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }
        return new Account(id, balance.subtract(amount));
    }

    public Account deposit(BigDecimal amount) {
        return new Account(id, balance.add(amount));
    }
}

package igorekpotworek.transfer.domain;


import igorekpotworek.transfer.service.InsufficientBalanceException;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

import static java.util.UUID.randomUUID;

@Value
@Entity
public class Account {
    @Id
    private String id;
    private BigDecimal balance;

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

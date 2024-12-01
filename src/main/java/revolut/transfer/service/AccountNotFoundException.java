package revolut.transfer.service;

public class AccountNotFoundException extends IllegalStateException {
    public AccountNotFoundException(String accountId) {
        super(String.format("There is no account with id: %s", accountId));
    }
}

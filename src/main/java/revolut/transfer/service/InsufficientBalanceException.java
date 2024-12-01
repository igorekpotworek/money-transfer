package revolut.transfer.service;

public class InsufficientBalanceException extends IllegalStateException {
    public InsufficientBalanceException() {
        super("There is no enough founds on account");
    }
}

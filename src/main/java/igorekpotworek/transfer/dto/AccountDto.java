package igorekpotworek.transfer.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Value
@Introspected
public class AccountDto {
    @Positive
    @NotNull
    private BigDecimal initialBalance;
}

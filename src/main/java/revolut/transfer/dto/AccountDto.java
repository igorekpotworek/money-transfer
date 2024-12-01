package revolut.transfer.dto;

import io.micronaut.core.annotation.Introspected;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Introspected
public class AccountDto {
    @Positive
    @NotNull
    BigDecimal initialBalance;
}

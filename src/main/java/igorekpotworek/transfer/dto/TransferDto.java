package igorekpotworek.transfer.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Value
@Introspected
public class TransferDto {
    @NotNull
    private String sourceAccountId;
    @NotNull
    private String destinationAccountId;
    @NotNull
    @Positive
    private BigDecimal amount;
}

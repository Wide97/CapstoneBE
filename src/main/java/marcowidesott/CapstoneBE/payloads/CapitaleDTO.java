package marcowidesott.CapstoneBE.payloads;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CapitaleDTO(
        @NotNull(message = "Il capitale iniziale è obbligatorio.")
        BigDecimal capitaleIniziale
) {
}

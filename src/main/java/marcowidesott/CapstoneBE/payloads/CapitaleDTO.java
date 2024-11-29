package marcowidesott.CapstoneBE.payloads;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CapitaleDTO(
        @NotNull(message = "Il capitale iniziale è obbligatorio.")
        BigDecimal capitaleIniziale,

        @NotNull(message = "Il capitale attuale è obbligatorio.")
        BigDecimal capitaleAttuale,

        UUID userId
) {
}
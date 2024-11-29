package marcowidesott.CapstoneBE.payloads;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Month;
import java.util.UUID;

public record ReportMensileDTO(
        @NotNull(message = "Il mese è obbligatorio.")
        Month mese,

        @NotNull(message = "Il profitto è obbligatorio.")
        BigDecimal profitto,

        @NotNull(message = "La perdita è obbligatoria.")
        BigDecimal perdita,

        @NotNull(message = "Il capitale finale è obbligatorio.")
        BigDecimal capitaleFinale,

        UUID userId
) {
}
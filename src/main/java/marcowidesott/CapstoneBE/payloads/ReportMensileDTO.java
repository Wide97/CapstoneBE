package marcowidesott.CapstoneBE.payloads;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Month;
import java.util.UUID;

public record ReportMensileDTO(
        @NotNull Month mese,
        @NotNull int anno,
        @NotNull BigDecimal profitto,
        @NotNull BigDecimal perdita,
        @NotNull BigDecimal capitaleFinale,
        @NotNull UUID id
) {
}

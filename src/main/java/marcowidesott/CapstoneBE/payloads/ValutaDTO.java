package marcowidesott.CapstoneBE.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ValutaDTO(
        @NotNull(message = "Il codice valuta è obbligatorio.")
        String codice,

        @NotNull(message = "Il tasso di cambio è obbligatorio.")
        Double tassoDiCambio,

        UUID userId
) {
}
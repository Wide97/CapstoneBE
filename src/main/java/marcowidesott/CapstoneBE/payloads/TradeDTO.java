package marcowidesott.CapstoneBE.payloads;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import marcowidesott.CapstoneBE.entities.Asset;
import marcowidesott.CapstoneBE.entities.PositionType;
import marcowidesott.CapstoneBE.entities.TradeResult;

import java.math.BigDecimal;
import java.util.UUID;

public record TradeDTO(
        @NotNull(message = "La data di acquisto è obbligatoria.")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Formato data di acquisto non valido. Usa il formato: yyyy-MM-dd")
        String purchaseDate,  // Data di acquisto (yyyy-MM-dd)

        @NotNull(message = "La data di vendita è obbligatoria.")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Formato data di vendita non valido. Usa il formato: yyyy-MM-dd")
        String saleDate,  // Data di vendita (yyyy-MM-dd)

        @NotNull(message = "L'ora di acquisto è obbligatoria.")
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Formato ora di acquisto non valido. Usa il formato: HH:mm")
        String purchaseTime,  // Ora di acquisto (HH:mm)

        @NotNull(message = "L'ora di vendita è obbligatoria.")
        @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Formato ora di vendita non valido. Usa il formato: HH:mm")
        String saleTime,  // Ora di vendita (HH:mm)

        @NotNull(message = "Il lottaggio della posizione è obbligatorio.")
        Double positionSize,

        @NotNull(message = "La leva è obbligatoria.")
        String leverage,

        @NotNull(message = "La strategia è obbligatoria.")
        String strategy,

        @NotNull(message = "La tipologia di trade è obbligatoria.")
        PositionType tradeType,  // LONG o SHORT

        @NotNull(message = "I costi di apertura sono obbligatori.")
        Double openingCosts,

        @NotNull(message = "I costi di chiusura sono obbligatori.")
        Double closingCosts,

        @NotNull(message = "L'esito del trade è obbligatorio.")
        TradeResult result,  // PROFIT, STOP LOSS, o BREAK EVEN

        @NotNull(message = "L'ammontare del profitto/perdita è obbligatorio.")
        BigDecimal profitLoss,

        @NotNull(message = "L'asset è obbligatorio.")
        Asset asset,  // Asset selezionato come EUR/USD, BTC/USD, ecc.

        UUID tradeId


) {
}


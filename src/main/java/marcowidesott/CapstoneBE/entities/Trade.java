package marcowidesott.CapstoneBE.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "trades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDate purchaseDate;
    private LocalDate saleDate;
    private LocalTime purchaseTime;
    private LocalTime saleTime;
    private double positionSize; // Lottaggio della posizione
    private String leverage; // Leva, esempio: "1:30", "1:100", etc.
    private String strategy; // Strategia utilizzata dall'utente
    @Enumerated(EnumType.STRING)
    private PositionType tradeType;  // LONG o SHORT


    private double openingCosts; // Costi di apertura
    private double closingCosts; // Costi di chiusura


    @Enumerated(EnumType.STRING)
    private TradeResult result;  // Esito del trade (Profitto, Perdita, Break Even)
    
    private Double profitLoss;

    @Enumerated(EnumType.STRING)
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
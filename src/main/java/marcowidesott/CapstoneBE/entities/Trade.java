package marcowidesott.CapstoneBE.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "trades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDate purchaseDate;
    private LocalDate saleDate;
    private LocalTime purchaseTime;
    private LocalTime saleTime;
    private double positionSize;
    private String leverage;
    private String strategy;
    @Enumerated(EnumType.STRING)
    private PositionType tradeType;


    private double openingCosts;
    private double closingCosts;


    @Enumerated(EnumType.STRING)
    private TradeResult result;

    private BigDecimal profitLoss;

    @Enumerated(EnumType.STRING)
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "valuta_id")
    private Valuta valuta;


}
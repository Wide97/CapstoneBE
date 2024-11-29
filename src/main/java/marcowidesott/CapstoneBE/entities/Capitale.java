package marcowidesott.CapstoneBE.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "capitale")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Capitale {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal capitaleIniziale;

    @Column(nullable = false)
    private BigDecimal capitaleAttuale;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

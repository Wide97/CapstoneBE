package marcowidesott.CapstoneBE.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Month;
import java.util.UUID;

@Entity
@Table(name = "report_mensili")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportMensile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Month mese; // Mese del report

    @Column(nullable = false)
    private BigDecimal profitto;

    @Column(nullable = false)
    private BigDecimal perdita;

    @Column(nullable = false)
    private BigDecimal capitaleFinale;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

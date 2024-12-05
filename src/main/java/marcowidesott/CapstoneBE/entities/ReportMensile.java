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

    @Column(nullable = false, columnDefinition = "integer default 2023")
    private int anno;
    
    @Column(nullable = false)
    private BigDecimal profitto; // Profitto totale

    @Column(nullable = false)
    private BigDecimal perdita; // Perdita totale

    @Column(nullable = false)
    private BigDecimal capitaleFinale; // Capitale al termine del mese

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}




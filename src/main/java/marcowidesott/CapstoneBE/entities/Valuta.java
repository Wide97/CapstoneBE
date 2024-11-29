package marcowidesott.CapstoneBE.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "valute")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Valuta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String codice;

    @Column(nullable = false)
    private double tassoDiCambio;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

package marcowidesott.CapstoneBE.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    private String codice; // Es. "USD", "EUR", ecc.

    @Column(nullable = false)
    private String nome; // Nome completo della valuta, es. "Dollaro Statunitense"

    @Column(nullable = false)
    private String simbolo; // Simbolo della valuta, es. "$", "€", ecc.

    @OneToMany(mappedBy = "valuta", fetch = FetchType.LAZY)
    private List<User> utenti;
}


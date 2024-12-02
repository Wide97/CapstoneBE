package marcowidesott.CapstoneBE.repositories;

import marcowidesott.CapstoneBE.entities.Valuta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ValutaRepository extends JpaRepository<Valuta, UUID> {
    Optional<Valuta> findByCodice(String codice);
}

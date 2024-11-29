package marcowidesott.CapstoneBE.repositories;

import marcowidesott.CapstoneBE.entities.Valuta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ValutaRepository extends JpaRepository<Valuta, UUID> {
    List<Valuta> findByUserId(UUID userId);
}

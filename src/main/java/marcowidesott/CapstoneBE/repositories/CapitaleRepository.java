package marcowidesott.CapstoneBE.repositories;

import marcowidesott.CapstoneBE.entities.Capitale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CapitaleRepository extends JpaRepository<Capitale, UUID> {
    Optional<Capitale> findByUserId(UUID userId);
}

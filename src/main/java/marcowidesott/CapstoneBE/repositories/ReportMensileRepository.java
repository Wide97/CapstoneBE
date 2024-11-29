package marcowidesott.CapstoneBE.repositories;

import marcowidesott.CapstoneBE.entities.ReportMensile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportMensileRepository extends JpaRepository<ReportMensile, UUID> {
    List<ReportMensile> findByUserId(UUID userId);
}

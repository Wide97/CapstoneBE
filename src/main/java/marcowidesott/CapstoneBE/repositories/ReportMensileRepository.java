package marcowidesott.CapstoneBE.repositories;

import marcowidesott.CapstoneBE.entities.ReportMensile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportMensileRepository extends JpaRepository<ReportMensile, UUID> {
    List<ReportMensile> findByUserId(UUID userId);

    Optional<ReportMensile> findByUserIdAndMeseAndAnno(UUID userId, Month mese, int anno);
    
}

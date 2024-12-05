package marcowidesott.CapstoneBE.repositories;

import marcowidesott.CapstoneBE.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TradeRepository extends JpaRepository<Trade, UUID> {

    List<Trade> findByUserId(UUID userId); // Trova i trade per un dato utente

    List<Trade> findByAsset(String asset); // Trova i trade per asset

    List<Trade> findByResult(String result);// Trova i trade per risultato (profitto, stop loss, ecc.)

    void deleteById(UUID id);

    List<Trade> findAllByUserIdAndSaleDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);


}

package marcowidesott.CapstoneBE.services;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Capitale;
import marcowidesott.CapstoneBE.entities.Trade;
import marcowidesott.CapstoneBE.repositories.CapitaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CapitaleService {

    private final CapitaleRepository capitaleRepository;

    public Capitale getCapitaleByUserId(UUID userId) {
        return capitaleRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Capitale non trovato per l'utente con ID: " + userId));
    }

    @Transactional
    public Capitale aggiornaCapitale(Trade trade) {
        Capitale capitale = getCapitaleByUserId(trade.getUser().getId());
        BigDecimal profitLoss = trade.getProfitLoss();

        // Aggiorna il capitale attuale con il risultato del trade
        BigDecimal nuovoCapitaleAttuale = capitale.getCapitaleAttuale().add(profitLoss);
        capitale.setCapitaleAttuale(nuovoCapitaleAttuale);

        return capitaleRepository.save(capitale);
    }
}
package marcowidesott.CapstoneBE.services;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Capitale;
import marcowidesott.CapstoneBE.entities.Trade;
import marcowidesott.CapstoneBE.repositories.CapitaleRepository;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CapitaleService {

    private final CapitaleRepository capitaleRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public void setCapitaleIniziale(UUID userId, BigDecimal capitaleIniziale) {
        Capitale capitale = capitaleRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Capitale newCapitale = new Capitale();
                    newCapitale.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + userId)));
                    return newCapitale;
                });

        capitale.setCapitaleIniziale(capitaleIniziale);
        capitale.setCapitaleAttuale(capitaleIniziale);  // Impostiamo anche il capitale attuale al capitale iniziale

        capitaleRepository.save(capitale);
    }
}
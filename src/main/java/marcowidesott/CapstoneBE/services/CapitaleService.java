package marcowidesott.CapstoneBE.services;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Capitale;
import marcowidesott.CapstoneBE.entities.Trade;
import marcowidesott.CapstoneBE.repositories.CapitaleRepository;
import marcowidesott.CapstoneBE.repositories.TradeRepository;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CapitaleService {

    private final CapitaleRepository capitaleRepository;
    private final UserRepository userRepository;
    private final TradeRepository tradeRepository;

    public Capitale getCapitaleByUserId(UUID userId) {
        Capitale capitale = capitaleRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Capitale non trovato per l'utente con ID: " + userId));

        if (capitale.getCapitaleAttuale() == null) {
            throw new RuntimeException("Capitale attuale non disponibile per l'utente: " + userId);
        }
        return capitale;
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
        capitale.setCapitaleAttuale(capitaleIniziale);

        capitaleRepository.save(capitale);
    }

    @Transactional
    public Capitale ricalcolaCapitale(UUID userId) {
        Capitale capitale = getCapitaleByUserId(userId);

        // Recuperiamo tutti i trade dell'utente
        List<Trade> trades = tradeRepository.findByUserId(userId);

        // Sommiamo tutti i profitti/perdite dei trade
        BigDecimal totaleProfitLoss = trades.stream()
                .map(Trade::getProfitLoss)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcoliamo il nuovo capitale attuale
        BigDecimal nuovoCapitaleAttuale = capitale.getCapitaleIniziale().add(totaleProfitLoss);
        capitale.setCapitaleAttuale(nuovoCapitaleAttuale);

        return capitaleRepository.save(capitale);
    }

    public BigDecimal getCapitaleAttualeByUserId(UUID userId) {
        Capitale capitale = getCapitaleByUserId(userId);
        return capitale.getCapitaleAttuale();
    }

    public BigDecimal getCapitaleInizialeByUserId(UUID userId) {
        Capitale capitale = getCapitaleByUserId(userId);
        return capitale.getCapitaleIniziale();
    }

}


package marcowidesott.CapstoneBE.services;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Capitale;
import marcowidesott.CapstoneBE.entities.Trade;
import marcowidesott.CapstoneBE.entities.TradeResult;
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

        List<Trade> trades = tradeRepository.findByUserId(userId);

        BigDecimal totaleProfitLoss = trades.stream()
                .map(trade -> {
                    BigDecimal profitLoss = trade.getProfitLoss();
                    BigDecimal costiTotali = BigDecimal.valueOf(trade.getOpeningCosts() + trade.getClosingCosts());

                    if (trade.getResult() == TradeResult.BREAK_EVEN) {
                        return costiTotali.negate();
                    } else if (trade.getResult() == TradeResult.STOP_LOSS) {
                        return profitLoss;
                    } else {
                        return profitLoss;
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

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


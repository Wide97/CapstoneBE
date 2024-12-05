package marcowidesott.CapstoneBE.services;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Capitale;
import marcowidesott.CapstoneBE.entities.ReportMensile;
import marcowidesott.CapstoneBE.entities.Trade;
import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.repositories.CapitaleRepository;
import marcowidesott.CapstoneBE.repositories.ReportMensileRepository;
import marcowidesott.CapstoneBE.repositories.TradeRepository;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportMensileService {
    private final ReportMensileRepository reportMensileRepository;
    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final CapitaleRepository capitaleRepository;

    @Transactional
    public void generaReportMensile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Capitale capitale = user.getCapitale();
        if (capitale == null) {
            throw new RuntimeException("Capitale non trovato per l'utente");
        }

        LocalDate oggi = LocalDate.now();
        Month mesePrecedente = oggi.minusMonths(1).getMonth();
        int annoPrecedente = oggi.minusMonths(1).getYear();

        // Verifica se il report per il mese esiste già
        if (reportMensileRepository.findByUserIdAndMeseAndAnno(userId, mesePrecedente, annoPrecedente).isPresent()) {
            throw new RuntimeException("Report mensile già generato per il mese precedente");
        }

        // Trova solo i trade non contabilizzati
        List<Trade> trades = tradeRepository.findAllByUserIdAndSaleDateBetweenAndIsAccountedFalse(
                userId,
                oggi.withDayOfMonth(1).minusMonths(1),
                oggi.withDayOfMonth(1).minusDays(1)
        );

        BigDecimal profitto = BigDecimal.ZERO;
        BigDecimal perdita = BigDecimal.ZERO;

        for (Trade trade : trades) {
            BigDecimal risultato = trade.getProfitLoss();
            if (risultato.compareTo(BigDecimal.ZERO) > 0) {
                profitto = profitto.add(risultato);
            } else {
                perdita = perdita.add(risultato.abs());
            }
            trade.setAccounted(true);
        }

        BigDecimal capitaleFinale = capitale.getCapitaleAttuale().add(profitto).subtract(perdita);

        ReportMensile reportMensile = ReportMensile.builder()
                .mese(mesePrecedente)
                .anno(annoPrecedente)
                .profitto(profitto)
                .perdita(perdita)
                .capitaleFinale(capitaleFinale)
                .user(user)
                .build();

        reportMensileRepository.save(reportMensile);
        tradeRepository.saveAll(trades);
    }


    public List<ReportMensile> getReportMensiliByUserId(UUID userId) {
        return reportMensileRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteReportMensile(UUID reportId) {
        if (!reportMensileRepository.existsById(reportId)) {
            throw new RuntimeException("Report mensile non trovato");
        }
        reportMensileRepository.deleteById(reportId);
    }
}



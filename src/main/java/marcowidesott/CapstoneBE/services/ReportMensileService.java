package marcowidesott.CapstoneBE.services;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Capitale;
import marcowidesott.CapstoneBE.entities.ReportMensile;
import marcowidesott.CapstoneBE.entities.Trade;
import marcowidesott.CapstoneBE.repositories.CapitaleRepository;
import marcowidesott.CapstoneBE.repositories.ReportMensileRepository;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final CapitaleService capitaleService;
    private final CapitaleRepository capitaleRepository;

    @Scheduled(cron = "0 0 0 1 * ?") // Esegui ogni primo giorno del mese
    @Transactional
    public void generaReportMensile(UUID userId) {
        Capitale capitale = capitaleService.getCapitaleByUserId(userId);
        BigDecimal profitto = BigDecimal.ZERO;
        BigDecimal perdita = BigDecimal.ZERO;

        // Calcola profitto e perdita totale del mese precedente
        List<Trade> trades = capitale.getUser().getTrades();
        Month mesePrecedente = LocalDate.now().minusMonths(1).getMonth();
        for (Trade trade : trades) {
            if (trade.getSaleDate().getMonth() == mesePrecedente) {
                if (trade.getProfitLoss().compareTo(BigDecimal.ZERO) > 0) {
                    profitto = profitto.add(trade.getProfitLoss());
                } else {
                    perdita = perdita.add(trade.getProfitLoss().abs());
                }
            }
        }

        // Crea e salva il report mensile
        ReportMensile reportMensile = ReportMensile.builder()
                .mese(mesePrecedente)
                .profitto(profitto)
                .perdita(perdita)
                .capitaleFinale(capitale.getCapitaleAttuale())
                .user(capitale.getUser())
                .build();
        reportMensileRepository.save(reportMensile);

        // Azzera profitto e perdita
        capitale.setCapitaleAttuale(capitale.getCapitaleAttuale());
        capitaleRepository.save(capitale); // Aggiornamento senza aggiunta di nuovo trade
    }

    public List<ReportMensile> getReportMensiliByUserId(UUID userId) {
        return reportMensileRepository.findByUserId(userId);
    }
}

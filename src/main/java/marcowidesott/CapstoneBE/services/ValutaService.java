package marcowidesott.CapstoneBE.services;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Valuta;
import marcowidesott.CapstoneBE.repositories.ValutaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ValutaService {

    private final ValutaRepository valutaRepository;

    public List<Valuta> getValuteByUserId(UUID userId) {
        return valutaRepository.findByUserId(userId);
    }

    @Transactional
    public Valuta creaValuta(Valuta valuta) {
        return valutaRepository.save(valuta);
    }

    @Transactional
    public Valuta aggiornaTassoDiCambio(UUID valutaId, double nuovoTassoDiCambio) {
        Valuta valuta = valutaRepository.findById(valutaId)
                .orElseThrow(() -> new RuntimeException("Valuta non trovata con ID: " + valutaId));
        valuta.setTassoDiCambio(nuovoTassoDiCambio);
        return valutaRepository.save(valuta);
    }
}
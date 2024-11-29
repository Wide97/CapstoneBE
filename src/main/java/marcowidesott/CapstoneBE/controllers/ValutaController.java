package marcowidesott.CapstoneBE.controllers;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Valuta;
import marcowidesott.CapstoneBE.payloads.ValutaDTO;
import marcowidesott.CapstoneBE.services.ValutaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/valute")
@RequiredArgsConstructor
public class ValutaController {

    private final ValutaService valutaService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Valuta>> getValuteByUserId(@PathVariable UUID userId) {
        List<Valuta> valute = valutaService.getValuteByUserId(userId);
        return ResponseEntity.ok(valute);
    }

    @PostMapping("/crea")
    public ResponseEntity<Valuta> creaValuta(@RequestBody ValutaDTO valutaDTO) {
        Valuta valuta = Valuta.builder()
                .codice(valutaDTO.codice())
                .tassoDiCambio(valutaDTO.tassoDiCambio())
                .user(null) // User da collegare nel contesto
                .build();
        Valuta nuovaValuta = valutaService.creaValuta(valuta);
        return ResponseEntity.ok(nuovaValuta);
    }

    @PutMapping("/aggiorna/{valutaId}")
    public ResponseEntity<Valuta> aggiornaTassoDiCambio(@PathVariable UUID valutaId, @RequestBody Double nuovoTassoDiCambio) {
        Valuta aggiornata = valutaService.aggiornaTassoDiCambio(valutaId, nuovoTassoDiCambio);
        return ResponseEntity.ok(aggiornata);
    }
}
package marcowidesott.CapstoneBE.controllers;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Capitale;
import marcowidesott.CapstoneBE.entities.Trade;
import marcowidesott.CapstoneBE.payloads.CapitaleDTO;
import marcowidesott.CapstoneBE.services.CapitaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/capitale")
@RequiredArgsConstructor
public class CapitaleController {

    private final CapitaleService capitaleService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CapitaleDTO> getCapitaleByUserId(@PathVariable UUID userId) {
        Capitale capitale = capitaleService.getCapitaleByUserId(userId);
        CapitaleDTO capitaleDTO = new CapitaleDTO(capitale.getCapitaleIniziale(), capitale.getCapitaleAttuale(), userId);
        return ResponseEntity.ok(capitaleDTO);
    }

    @PostMapping("/iniziale/{userId}")
    public ResponseEntity<String> setCapitaleIniziale(@PathVariable UUID userId, @RequestBody BigDecimal capitaleIniziale) {
        capitaleService.setCapitaleIniziale(userId, capitaleIniziale);
        return ResponseEntity.ok("Capitale iniziale impostato con successo.");
    }

    @PutMapping("/aggiorna")
    public ResponseEntity<Capitale> aggiornaCapitale(@RequestBody Trade trade) {
        Capitale capitaleAggiornato = capitaleService.aggiornaCapitale(trade);
        return ResponseEntity.ok(capitaleAggiornato);
    }
}
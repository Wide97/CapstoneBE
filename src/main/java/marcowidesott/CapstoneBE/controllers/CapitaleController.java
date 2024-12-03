package marcowidesott.CapstoneBE.controllers;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Capitale;
import marcowidesott.CapstoneBE.payloads.CapitaleDTO;
import marcowidesott.CapstoneBE.services.CapitaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/capitale")
@RequiredArgsConstructor
public class CapitaleController {

    private final CapitaleService capitaleService;

    // Endpoint per ottenere il capitale attuale di un utente
    @GetMapping("/utente/{userId}")
    public ResponseEntity<Capitale> getCapitaleByUserId(@PathVariable UUID userId) {
        Capitale capitale = capitaleService.getCapitaleByUserId(userId);
        return ResponseEntity.ok(capitale);
    }

    // Endpoint per impostare il capitale iniziale di un utente
    @PostMapping("/utente/{userId}/iniziale")
    public ResponseEntity<Map<String, String>> setCapitaleIniziale(@PathVariable UUID userId, @RequestBody CapitaleDTO capitaleDTO) {
        capitaleService.setCapitaleIniziale(userId, capitaleDTO.capitaleIniziale());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Capitale iniziale impostato con successo.");
        return ResponseEntity.ok(response);
    }


    // Endpoint per ricalcolare il capitale attuale di un utente (in base ai trade esistenti)
    @PutMapping("/utente/{userId}/ricalcola")
    public ResponseEntity<Capitale> ricalcolaCapitale(@PathVariable UUID userId) {
        Capitale capitaleRicalcolato = capitaleService.ricalcolaCapitale(userId);
        return ResponseEntity.ok(capitaleRicalcolato);
    }

    @GetMapping("/utente/{userId}/attuale")
    public ResponseEntity<BigDecimal> getCapitaleAttualeByUserId(@PathVariable UUID userId) {
        BigDecimal capitaleAttuale = capitaleService.getCapitaleAttualeByUserId(userId);
        if (capitaleAttuale == null) {
            throw new RuntimeException("Capitale attuale non trovato per l'utente con ID: " + userId);
        }
        return ResponseEntity.ok(capitaleAttuale);
    }

    @GetMapping("/utente/{userId}/iniziale")
    public ResponseEntity<String> getCapitaleInizialeByUserId(@PathVariable UUID userId) {
        BigDecimal capitaleIniziale = capitaleService.getCapitaleInizialeByUserId(userId);
        return ResponseEntity.ok(capitaleIniziale.toString());
    }


}



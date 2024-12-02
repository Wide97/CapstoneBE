package marcowidesott.CapstoneBE.controllers;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Capitale;
import marcowidesott.CapstoneBE.payloads.CapitaleDTO;
import marcowidesott.CapstoneBE.services.CapitaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> setCapitaleIniziale(@PathVariable UUID userId, @RequestBody CapitaleDTO capitaleDTO) {
        capitaleService.setCapitaleIniziale(userId, capitaleDTO.capitaleIniziale());
        return ResponseEntity.ok("Capitale iniziale impostato con successo.");
    }

    // Endpoint per ricalcolare il capitale attuale di un utente (in base ai trade esistenti)
    @PutMapping("/utente/{userId}/ricalcola")
    public ResponseEntity<Capitale> ricalcolaCapitale(@PathVariable UUID userId) {
        Capitale capitaleRicalcolato = capitaleService.ricalcolaCapitale(userId);
        return ResponseEntity.ok(capitaleRicalcolato);
    }

    // Endpoint per aggiornare manualmente il capitale attuale di un utente
    @PutMapping("/utente/{userId}/aggiorna")
    public ResponseEntity<Capitale> aggiornaCapitale(@PathVariable UUID userId, @RequestBody CapitaleDTO capitaleDTO) {
        Capitale capitaleAggiornato = capitaleService.aggiornaCapitaleManuale(userId, capitaleDTO.capitaleIniziale());
        return ResponseEntity.ok(capitaleAggiornato);
    }
}



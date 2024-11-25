package marcowidesott.CapstoneBE.controllers;

import jakarta.validation.Valid;
import marcowidesott.CapstoneBE.entities.Trade;
import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.exceptions.UserNotFoundException;
import marcowidesott.CapstoneBE.payloads.TradeDTO;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import marcowidesott.CapstoneBE.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createTrade")
    public ResponseEntity<String> createTrade(@RequestBody @Valid TradeDTO tradeDTO, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        Trade newTrade = tradeService.createTrade(user.getId(), tradeDTO);

        return ResponseEntity.ok("Trade creato con successo: " + newTrade.getId());
    }

    // 2. Modifica di un trade
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTrade(
            @PathVariable UUID id,
            @RequestBody @Valid TradeDTO tradeDTO) {


        Trade updatedTrade = tradeService.updateTrade(id, tradeDTO);

        return ResponseEntity.ok("Trade aggiornato con successo: " + updatedTrade.getId());
    }

    // 3. Visualizzazione della lista di tutti i trade dell'utente
    @GetMapping("/")
    public ResponseEntity<List<Trade>> getAllTrades(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        List<Trade> trades = tradeService.getAllTradesForUser(user.getId());
        return ResponseEntity.ok(trades);
    }

    // 4. Eliminazione di un trade
    @DeleteMapping("/{tradeId}")
    public ResponseEntity<String> deleteTrade(@PathVariable UUID tradeId, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        tradeService.deleteTrade(user.getId(), tradeId);
        return ResponseEntity.ok("Trade eliminato con successo.");
    }
}


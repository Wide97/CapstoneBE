package marcowidesott.CapstoneBE.controllers;

import jakarta.validation.Valid;
import marcowidesott.CapstoneBE.entities.Trade;
import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.exceptions.UserNotFoundException;
import marcowidesott.CapstoneBE.payloads.TradeDTO;
import marcowidesott.CapstoneBE.repositories.UserRepository;
import marcowidesott.CapstoneBE.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Map<String, Object>> createTrade(@RequestBody @Valid TradeDTO tradeDTO, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        Trade newTrade = tradeService.createTrade(user.getId(), tradeDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Trade creato con successo");
        response.put("tradeId", newTrade.getId());
        return ResponseEntity.ok(response);
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
    @GetMapping("/user/{userId}/trades")
    public ResponseEntity<List<TradeDTO>> getAllTradesByUserId(@PathVariable UUID userId) {
        List<TradeDTO> trades = tradeService.getAllTradesByUserId(userId);
        return ResponseEntity.ok(trades);

    }

    // 4. Eliminazione di un trade
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTrade(@PathVariable UUID id, Principal principal) {
        try {
            String username = principal.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

            // Chiama il servizio per eliminare il trade e aggiornare il capitale
            tradeService.deleteTrade(id, user.getId());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Trade eliminato con successo");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "ID trade non valido"));
        }
    }


}


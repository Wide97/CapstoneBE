package marcowidesott.CapstoneBE.controllers;

import marcowidesott.CapstoneBE.entities.User;
import marcowidesott.CapstoneBE.entities.Valuta;
import marcowidesott.CapstoneBE.services.ValutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/valuta")
public class ValutaController {

    @Autowired
    private ValutaService valutaService;

    @GetMapping
    public ResponseEntity<List<Valuta>> getAllValute() {
        List<Valuta> valute = valutaService.getAllValute();
        return ResponseEntity.ok(valute);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Valuta> getValutaById(@PathVariable UUID id) {
        Valuta valuta = valutaService.getValutaById(id);
        return ResponseEntity.ok(valuta);
    }

    @PostMapping
    public ResponseEntity<Valuta> createValuta(@RequestBody Valuta valuta) {
        Valuta nuovaValuta = valutaService.createValuta(valuta);
        return ResponseEntity.ok(nuovaValuta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Valuta> updateValuta(@PathVariable UUID id, @RequestBody Valuta valuta) {
        Valuta updatedValuta = valutaService.updateValuta(id, valuta);
        return ResponseEntity.ok(updatedValuta);
    }

    @PutMapping("/utente/{userId}")
    public ResponseEntity<User> aggiornaValutaUtente(@PathVariable UUID userId, @RequestBody Map<String, String> body) {
        UUID valutaId = UUID.fromString(body.get("valutaId"));
        User updatedUser = valutaService.aggiornaValutaPerUtente(userId, valutaId);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValuta(@PathVariable UUID id) {
        valutaService.deleteValuta(id);
        return ResponseEntity.noContent().build();
    }
}


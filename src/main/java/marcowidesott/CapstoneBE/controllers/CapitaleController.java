package marcowidesott.CapstoneBE.controllers;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.Capitale;
import marcowidesott.CapstoneBE.payloads.CapitaleDTO;
import marcowidesott.CapstoneBE.services.CapitaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
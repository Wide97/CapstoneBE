package marcowidesott.CapstoneBE.controllers;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.ReportMensile;
import marcowidesott.CapstoneBE.services.ReportMensileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportMensileController {

    private final ReportMensileService reportMensileService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportMensile>> getReportMensiliByUserId(@PathVariable UUID userId) {
        List<ReportMensile> reportMensili = reportMensileService.getReportMensiliByUserId(userId);
        return ResponseEntity.ok(reportMensili);
    }

    @PostMapping("/genera/{userId}")
    public ResponseEntity<String> generaReportMensile(@PathVariable UUID userId) {
        reportMensileService.generaReportMensile(userId);
        return ResponseEntity.ok("Report mensile generato con successo.");
    }
}
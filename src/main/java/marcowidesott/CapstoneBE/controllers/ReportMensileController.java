package marcowidesott.CapstoneBE.controllers;

import lombok.RequiredArgsConstructor;
import marcowidesott.CapstoneBE.entities.ReportMensile;
import marcowidesott.CapstoneBE.payloads.ReportMensileDTO;
import marcowidesott.CapstoneBE.services.ReportMensileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportMensileController {
    private final ReportMensileService reportMensileService;

    @PostMapping("/genera/{userId}")
    public ResponseEntity<Map<String, String>> generaReportMensile(@PathVariable UUID userId) {
        reportMensileService.generaReportMensile(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Report mensile generato con successo.");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportMensileDTO>> getReportMensiliByUserId(@PathVariable UUID userId) {
        List<ReportMensile> reportMensili = reportMensileService.getReportMensiliByUserId(userId);

        // Trasforma i report in DTO per limitare la profondità dei dati
        List<ReportMensileDTO> dtoList = reportMensili.stream()
                .map(report -> new ReportMensileDTO(
                        report.getMese(),
                        report.getAnno(),
                        report.getProfitto(),
                        report.getPerdita(),
                        report.getCapitaleFinale(),
                        report.getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }


    @DeleteMapping("/{reportId}")
    public ResponseEntity<Map<String, String>> deleteReportMensile(@PathVariable UUID reportId) {
        reportMensileService.deleteReportMensile(reportId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Report mensile eliminato con successo.");
        return ResponseEntity.ok(response);
    }

}

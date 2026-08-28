package com.nectux.mizan.hyban.paie.web;

import com.nectux.mizan.hyban.paie.entity.HeureSupplementaire;
import com.nectux.mizan.hyban.paie.entity.RegleHeureSupplementaire;
import com.nectux.mizan.hyban.paie.enums.StatutHS;
import com.nectux.mizan.hyban.paie.service.HeureSupplementaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/heures-supplementaires")
@CrossOrigin(origins = {"http://localhost:7153", "http://192.168.1.7:7153", "http://localhost:7200", "http://192.168.1.7:7200", "http://localhost:4200", "http://127.0.0.1:3000"},
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PATCH},
        allowCredentials = "true")
public class HeureSupplementaireController {

    @Autowired
    private HeureSupplementaireService heureSuppService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping
    @PreAuthorize("hasAnyAuthority('HS_CREATE') or hasRole('ADMIN')")
    public ResponseEntity<HeureSupplementaire> create(@RequestBody HeureSupplementaire hs) {
        return ResponseEntity.ok(heureSuppService.create(hs));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('HS_READ') or hasRole('ADMIN')")
    public ResponseEntity<List<HeureSupplementaire>> findAll(
            @RequestParam(required = false) Long employeId,
            @RequestParam(required = false) Long periodePaie,
            @RequestParam(required = false) StatutHS statut,
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin) {

        Date dDebut = null;
        Date dFin = null;
        try {
            if (dateDebut != null) dDebut = dateFormat.parse(dateDebut);
            if (dateFin != null) dFin = dateFormat.parse(dateFin);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(heureSuppService.findWithFilters(employeId, periodePaie, statut, dDebut, dFin));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('HS_READ') or hasRole('ADMIN')")
    public ResponseEntity<HeureSupplementaire> findById(@PathVariable Long id) {
        return ResponseEntity.ok(heureSuppService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('HS_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<HeureSupplementaire> update(@PathVariable Long id, @RequestBody HeureSupplementaire hs) {
        return ResponseEntity.ok(heureSuppService.update(id, hs));
    }

    @PostMapping("/{id}/soumettre")
    @PreAuthorize("hasAnyAuthority('HS_SUBMIT') or hasRole('ADMIN')")
    public ResponseEntity<HeureSupplementaire> soumettre(@PathVariable Long id) {
        return ResponseEntity.ok(heureSuppService.soumettre(id));
    }

    @PostMapping("/{id}/valider")
    @PreAuthorize("hasAnyAuthority('HS_VALIDATE') or hasRole('ADMIN')")
    public ResponseEntity<HeureSupplementaire> valider(@PathVariable Long id, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "system";
        return ResponseEntity.ok(heureSuppService.valider(id, username));
    }

    @PostMapping("/{id}/rejeter")
    @PreAuthorize("hasAnyAuthority('HS_REJECT') or hasRole('ADMIN')")
    public ResponseEntity<HeureSupplementaire> rejeter(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String motifRejet = body.get("motifRejet");
        return ResponseEntity.ok(heureSuppService.rejeter(id, motifRejet));
    }

    @PostMapping("/{id}/integrer-paie")
    @PreAuthorize("hasAnyAuthority('HS_INTEGRATE_PAIE') or hasRole('ADMIN')")
    public ResponseEntity<HeureSupplementaire> integrerPaie(@PathVariable Long id) {
        return ResponseEntity.ok(heureSuppService.integrerPaie(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('HS_UPDATE') or hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        heureSuppService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/regles")
    @PreAuthorize("hasAnyAuthority('HS_READ') or hasRole('ADMIN')")
    public ResponseEntity<List<RegleHeureSupplementaire>> findAllRegles() {
        return ResponseEntity.ok(heureSuppService.findAllRegles());
    }
}

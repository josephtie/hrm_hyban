package com.nectux.mizan.hyban.paie.web;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.StockCongeRequest;
import com.nectux.mizan.hyban.common.dto.StockCongeResponse;
import com.nectux.mizan.hyban.paie.dto.StockCongeDTO;
import com.nectux.mizan.hyban.paie.service.StockCongeService;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/personnel/stock-conges")
public class StockCongeRestController {

    private static final Logger logger = LoggerFactory.getLogger(StockCongeRestController.class);

    @Autowired
    private StockCongeService stockCongeService;
    @Autowired
    private PeriodePaieService periodePaieService;
    // @Autowired
    // private UtilisateurService utilisateurService;

    @PostMapping("/enregistrer")
    public ResponseEntity<StockCongeDTO> saveStockConge(@RequestBody StockCongeRequest request) {
        try {
            logger.info(">>> ENREGISTRER STOCK CONGE");
            StockCongeDTO result = stockCongeService.save(
                request.getId(),
                request.getIdConge(),
                request.getDateDepart(),
                request.getDateRetour(),
                request.getMontantVerse()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement du stock congé", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/trouver")
    public ResponseEntity<StockCongeDTO> findStockConge(@RequestBody StockCongeRequest request) {
        try {
            logger.info(">>> TROUVER STOCK CONGE");
            StockCongeDTO result = stockCongeService.findStockConge(request.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du stock congé", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/lister")
    public ResponseEntity<StockCongeDTO> listerStockConges() {
        try {
            logger.info(">>> LISTE STOCK CONGES");
            StockCongeDTO result = stockCongeService.findStockConges();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des stocks congés", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/lister/personnel/{idPersonnel}")
    public ResponseEntity<StockCongeDTO> listerStockCongesParPersonnel(@PathVariable Long idPersonnel) {
        try {
            logger.info(">>> LISTE STOCK CONGES PAR PERSONNEL");
            StockCongeDTO result = stockCongeService.findStockCongesByPersonnel(idPersonnel);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la liste des stocks congés par personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<StockCongeDTO> deleteStockConge(@PathVariable Long id) {
        try {
            logger.info(">>> SUPPRIMER STOCK CONGE");
            StockCongeDTO result = stockCongeService.delete(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du stock congé", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paginer")
    public ResponseEntity<StockCongeResponse<?>> getStockCongeListJSON(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            logger.info(">>> LISTE STOCK CONGES AVEC PAGINATION");
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
            
            StockCongeDTO stockCongeDTO;
            if (search == null || search.trim().isEmpty()) {
                stockCongeDTO = stockCongeService.loadStockConges(pageRequest);
            } else {
                stockCongeDTO = stockCongeService.loadStockConges(pageRequest);
            }
            
            StockCongeResponse<Object> response = new StockCongeResponse<>();
            response.setRows(stockCongeDTO.getRows().stream().map(stock -> (Object) stock).collect(Collectors.toList()));
            response.setTotal((int)stockCongeDTO.getTotal());
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la pagination des stocks congés", e);
            StockCongeResponse<Object> response = new StockCongeResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

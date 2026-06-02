package com.nectux.mizan.hyban.paie.web;

import com.nectux.mizan.hyban.paie.entity.PrimePersonnel;
import com.nectux.mizan.hyban.paie.repository.PrimePersonnelRepository;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.personnel.repository.ContratPersonnelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/paie/debug")
@CrossOrigin(origins = "*", allowCredentials = "false")
public class PaieDebugRestController {

    private static final Logger logger = LoggerFactory.getLogger(PaieDebugRestController.class);

    @Autowired
    private PrimePersonnelRepository primePersonnelRepository;

    @Autowired
    private ContratPersonnelRepository contratPersonnelRepository;

    @GetMapping("/primes/{idPeriode}/{idPersonnel}")
    public ResponseEntity<PrimeDebugResponse> debugPrimes(
            @PathVariable Long idPeriode,
            @PathVariable Long idPersonnel
    ) {
        PrimeDebugResponse response = new PrimeDebugResponse();
        response.setIdPeriode(idPeriode);
        response.setIdPersonnel(idPersonnel);

        Optional<ContratPersonnel> contratActifOpt =
                contratPersonnelRepository.findTopByPersonnelIdAndStatutTrueOrderByIdDesc(idPersonnel);

        Long idContratActif = contratActifOpt.map(ContratPersonnel::getId).orElse(null);
        response.setIdContratActif(idContratActif);

        List<PrimePersonnel> primesContrat = (idContratActif != null)
                ? primePersonnelRepository.findByContratPersonnelIdAndPeriodePaieId(idContratActif, idPeriode)
                : new ArrayList<>();

        List<PrimePersonnel> primesPersonnel =
                primePersonnelRepository.findByContratPersonnelPersonnelIdAndPeriodePaieId(idPersonnel, idPeriode);

        response.setPrimesContrat(toItems(primesContrat, "CONTRAT"));
        response.setPrimesPersonnel(toItems(primesPersonnel, "PERSONNEL"));

        Map<Long, PrimeDebugItem> fusion = new LinkedHashMap<>();
        for (PrimeDebugItem item : response.getPrimesContrat()) {
            if (item.getPrimeId() != null) {
                fusion.put(item.getPrimeId(), item);
            }
        }
        for (PrimeDebugItem item : response.getPrimesPersonnel()) {
            if (item.getPrimeId() != null) {
                fusion.putIfAbsent(item.getPrimeId(), item.withSource("PERSONNEL_FALLBACK"));
            }
        }

        response.setPrimesFusionnees(new ArrayList<>(fusion.values()));

        logger.info("[DEBUG-PRIMES] periode={}, personnel={}, contratActif={}, contrat={}, personnel={}, fusion={}",
                idPeriode,
                idPersonnel,
                idContratActif,
                response.getPrimesContrat().size(),
                response.getPrimesPersonnel().size(),
                response.getPrimesFusionnees().size());

        return ResponseEntity.ok(response);
    }

    private List<PrimeDebugItem> toItems(List<PrimePersonnel> primes, String source) {
        List<PrimeDebugItem> items = new ArrayList<>();
        for (PrimePersonnel pp : primes) {
            PrimeDebugItem item = new PrimeDebugItem();
            item.setSource(source);
            item.setPrimePersonnelId(pp.getId());
            item.setContratId(pp.getContratPersonnel() != null ? pp.getContratPersonnel().getId() : null);
            item.setMontant(pp.getMontant().doubleValue());

            if (pp.getPrime() != null) {
                item.setPrimeId(pp.getPrime().getId());
                item.setPrimeCode(pp.getPrime().getCode());
                item.setPrimeLibelle(pp.getPrime().getLibelle());
                item.setEtatImposition(pp.getPrime().getEtatImposition());
            }
            items.add(item);
        }
        return items;
    }

    public static class PrimeDebugResponse {
        private Long idPeriode;
        private Long idPersonnel;
        private Long idContratActif;
        private List<PrimeDebugItem> primesContrat;
        private List<PrimeDebugItem> primesPersonnel;
        private List<PrimeDebugItem> primesFusionnees;

        public Long getIdPeriode() {
            return idPeriode;
        }

        public void setIdPeriode(Long idPeriode) {
            this.idPeriode = idPeriode;
        }

        public Long getIdPersonnel() {
            return idPersonnel;
        }

        public void setIdPersonnel(Long idPersonnel) {
            this.idPersonnel = idPersonnel;
        }

        public Long getIdContratActif() {
            return idContratActif;
        }

        public void setIdContratActif(Long idContratActif) {
            this.idContratActif = idContratActif;
        }

        public List<PrimeDebugItem> getPrimesContrat() {
            return primesContrat;
        }

        public void setPrimesContrat(List<PrimeDebugItem> primesContrat) {
            this.primesContrat = primesContrat;
        }

        public List<PrimeDebugItem> getPrimesPersonnel() {
            return primesPersonnel;
        }

        public void setPrimesPersonnel(List<PrimeDebugItem> primesPersonnel) {
            this.primesPersonnel = primesPersonnel;
        }

        public List<PrimeDebugItem> getPrimesFusionnees() {
            return primesFusionnees;
        }

        public void setPrimesFusionnees(List<PrimeDebugItem> primesFusionnees) {
            this.primesFusionnees = primesFusionnees;
        }
    }

    public static class PrimeDebugItem {
        private String source;
        private Long primePersonnelId;
        private Long primeId;
        private String primeCode;
        private String primeLibelle;
        private Integer etatImposition;
        private Double montant;
        private Long contratId;

        public PrimeDebugItem withSource(String source) {
            this.source = source;
            return this;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Long getPrimePersonnelId() {
            return primePersonnelId;
        }

        public void setPrimePersonnelId(Long primePersonnelId) {
            this.primePersonnelId = primePersonnelId;
        }

        public Long getPrimeId() {
            return primeId;
        }

        public void setPrimeId(Long primeId) {
            this.primeId = primeId;
        }

        public String getPrimeCode() {
            return primeCode;
        }

        public void setPrimeCode(String primeCode) {
            this.primeCode = primeCode;
        }

        public String getPrimeLibelle() {
            return primeLibelle;
        }

        public void setPrimeLibelle(String primeLibelle) {
            this.primeLibelle = primeLibelle;
        }

        public Integer getEtatImposition() {
            return etatImposition;
        }

        public void setEtatImposition(Integer etatImposition) {
            this.etatImposition = etatImposition;
        }

        public Double getMontant() {
            return montant;
        }

        public void setMontant(Double montant) {
            this.montant = montant;
        }

        public Long getContratId() {
            return contratId;
        }

        public void setContratId(Long contratId) {
            this.contratId = contratId;
        }
    }
}

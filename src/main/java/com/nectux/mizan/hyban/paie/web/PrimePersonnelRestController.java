package com.nectux.mizan.hyban.paie.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.common.dto.PrimeCollectiveBatchRequest;
import com.nectux.mizan.hyban.common.dto.PrimePersonnelRequest;
import com.nectux.mizan.hyban.common.dto.PrimePersonnelResponse;
import com.nectux.mizan.hyban.paie.dto.PretDTO;
import com.nectux.mizan.hyban.paie.dto.PrimePersonnelDTO;
import com.nectux.mizan.hyban.paie.entity.Pret;
import com.nectux.mizan.hyban.paie.entity.PrimePersonnel;
import com.nectux.mizan.hyban.paie.repository.PrimePersonnelRepository;
import com.nectux.mizan.hyban.paie.service.PretService;
import com.nectux.mizan.hyban.paie.service.PrimePersonnelService;
import com.nectux.mizan.hyban.parametrages.dto.PeriodePaieDTO;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Rubrique;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.RubriqueService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

@RestController
@RequestMapping("/api/paie/prime-personnel")
public class PrimePersonnelRestController {

    private static final Logger logger = LoggerFactory.getLogger(PrimePersonnelRestController.class);

    // @Autowired
    // private UtilisateurService userService;
    @Autowired
    private PeriodePaieService periodePaieService;
    @Autowired
    private PretService pretService;
    @Autowired
    private PrimePersonnelService primePersonnelService;
    @Autowired
    private PrimePersonnelRepository primePersonnelRepository;
    @Autowired
    private SocieteService societeService;
    @Autowired
    private RubriqueService rubriqueService;
    // @Autowired
    // private UtilisateurService utilisateurService;
    // @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

    @GetMapping("/list")
    public ResponseEntity<PrimePersonnelResponse<PrimePersonnel>> getPrimePersonnelList(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            int pageNumber = (limit != null && limit > 0) ? offset / limit : 0;
            PageRequest pageRequest = PageRequest.of(pageNumber, limit, Direction.DESC, "id");
            PrimePersonnelDTO primePersonnelDTO;
            
            if (search == null || search.trim().isEmpty()) {
                primePersonnelDTO = primePersonnelService.loadPrimePersonnel(pageRequest);
            } else {
                primePersonnelDTO = primePersonnelService.loadPrimePersonnel(pageRequest, search);
            }
            
            PrimePersonnelResponse<PrimePersonnel> response = new PrimePersonnelResponse<>();
            if (primePersonnelDTO != null) {
                response.setRows(primePersonnelDTO.getRows() != null ? primePersonnelDTO.getRows() : new ArrayList<>());
                response.setTotal((int) primePersonnelDTO.getTotal());
            } else {
                response.setRows(new ArrayList<>());
                response.setTotal(0);
            }
            response.setResult("success");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des primes personnel", e);
            PrimePersonnelResponse<PrimePersonnel> response = new PrimePersonnelResponse<>();
            response.setResult("error");
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/enregistrer")
    public ResponseEntity<PrimePersonnelDTO> savePrimePersonnel(@RequestBody PrimePersonnelRequest request) {
        try {
            PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
            Long idCtrat = request.getIdPersonnel();
            
            PrimePersonnelDTO result = primePersonnelService.saver(
                request.getId(),
                request.getMontantop(),
                request.getValeurop(),
                request.getIdAbsence(),
                idCtrat,
                request.getIdPeriode()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la prime personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/rubriques-contrat-periode")
    public ResponseEntity<RubriquesContratPeriodeResponse> getRubriquesContratPeriode(
            @RequestParam Long idPeriode,
            @RequestParam Long idCtrat) {
        try {
            List<PrimePersonnel> primesContratPeriode =
                    primePersonnelRepository.findByContratPersonnelIdAndPeriodePaieId(idCtrat, idPeriode);

            Map<Long, PrimePersonnel> primeByRubriqueId = new LinkedHashMap<Long, PrimePersonnel>();
            for (PrimePersonnel pp : primesContratPeriode) {
                if (pp == null || pp.getPrime() == null || pp.getPrime().getId() == null) {
                    continue;
                }
                primeByRubriqueId.put(pp.getPrime().getId(), pp);
            }

            RubriquesContratPeriodeResponse response = new RubriquesContratPeriodeResponse();
            response.setIdPeriode(idPeriode);
            response.setIdCtrat(idCtrat);
            response.setListePrimesImp(resolveRubriquesWithMontants(1, primeByRubriqueId));
            response.setListePrimesNonImpos(resolveRubriquesWithMontants(2, primeByRubriqueId));
            response.setListePrimesImposetNon(resolveRubriquesWithMontants(3, primeByRubriqueId));
            response.setListePrimesMutuelle(resolveRubriquesWithMontants(4, primeByRubriqueId));
            response.setListePrimesGains(resolveRubriquesWithMontants(5, primeByRubriqueId));
            response.setListePrimesSociale(resolveRubriquesWithMontants(6, primeByRubriqueId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des rubriques par contrat/période", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<RubriquePrimeMontant> resolveRubriquesWithMontants(Integer etatType, Map<Long, PrimePersonnel> primeByRubriqueId) {
        List<RubriquePrimeMontant> result = new ArrayList<RubriquePrimeMontant>();
        List<Rubrique> rubriques = rubriqueService.getRubriquesActivesType(etatType);

        for (Rubrique rubrique : rubriques) {
            RubriquePrimeMontant item = new RubriquePrimeMontant();
            item.setRubriqueId(rubrique.getId());
            item.setCode(rubrique.getCode());
            item.setLibelle(rubrique.getLibelle());
            item.setEtatImposition(rubrique.getEtatImposition());
            item.setMtExedent(rubrique.getMtExedent());

            PrimePersonnel pp = primeByRubriqueId.get(rubrique.getId());
            if (pp != null) {
                item.setPrimePersonnelId(pp.getId());
                item.setMontant(pp.getMontant());
                item.setValeur(pp.getValeur());
            } else {
                item.setMontant(BigDecimal.ZERO);
                item.setValeur(0);
            }

            result.add(item);
        }

        return result;
    }

    public static class RubriquesContratPeriodeResponse {
        private Long idPeriode;
        private Long idCtrat;
        private List<RubriquePrimeMontant> listePrimesImp;
        private List<RubriquePrimeMontant> listePrimesNonImpos;
        private List<RubriquePrimeMontant> listePrimesImposetNon;
        private List<RubriquePrimeMontant> listePrimesMutuelle;
        private List<RubriquePrimeMontant> listePrimesSociale;
        private List<RubriquePrimeMontant> listePrimesGains;

        public Long getIdPeriode() {
            return idPeriode;
        }

        public void setIdPeriode(Long idPeriode) {
            this.idPeriode = idPeriode;
        }

        public Long getIdCtrat() {
            return idCtrat;
        }

        public void setIdCtrat(Long idCtrat) {
            this.idCtrat = idCtrat;
        }

        public List<RubriquePrimeMontant> getListePrimesImp() {
            return listePrimesImp;
        }

        public void setListePrimesImp(List<RubriquePrimeMontant> listePrimesImp) {
            this.listePrimesImp = listePrimesImp;
        }

        public List<RubriquePrimeMontant> getListePrimesNonImpos() {
            return listePrimesNonImpos;
        }

        public void setListePrimesNonImpos(List<RubriquePrimeMontant> listePrimesNonImpos) {
            this.listePrimesNonImpos = listePrimesNonImpos;
        }

        public List<RubriquePrimeMontant> getListePrimesImposetNon() {
            return listePrimesImposetNon;
        }

        public void setListePrimesImposetNon(List<RubriquePrimeMontant> listePrimesImposetNon) {
            this.listePrimesImposetNon = listePrimesImposetNon;
        }

        public List<RubriquePrimeMontant> getListePrimesMutuelle() {
            return listePrimesMutuelle;
        }

        public void setListePrimesMutuelle(List<RubriquePrimeMontant> listePrimesMutuelle) {
            this.listePrimesMutuelle = listePrimesMutuelle;
        }

        public List<RubriquePrimeMontant> getListePrimesSociale() {
            return listePrimesSociale;
        }

        public void setListePrimesSociale(List<RubriquePrimeMontant> listePrimesSociale) {
            this.listePrimesSociale = listePrimesSociale;
        }

        public List<RubriquePrimeMontant> getListePrimesGains() {
            return listePrimesGains;
        }

        public void setListePrimesGains(List<RubriquePrimeMontant> listePrimesGains) {
            this.listePrimesGains = listePrimesGains;
        }
    }

    public static class RubriquePrimeMontant {
        private Long rubriqueId;
        private Long primePersonnelId;
        private String code;
        private String libelle;
        private Integer etatImposition;
        private BigDecimal montant;
        private Integer valeur;
        private BigDecimal mtExedent;

        public Long getRubriqueId() {
            return rubriqueId;
        }

        public void setRubriqueId(Long rubriqueId) {
            this.rubriqueId = rubriqueId;
        }

        public Long getPrimePersonnelId() {
            return primePersonnelId;
        }

        public void setPrimePersonnelId(Long primePersonnelId) {
            this.primePersonnelId = primePersonnelId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLibelle() {
            return libelle;
        }

        public void setLibelle(String libelle) {
            this.libelle = libelle;
        }

        public Integer getEtatImposition() {
            return etatImposition;
        }

        public void setEtatImposition(Integer etatImposition) {
            this.etatImposition = etatImposition;
        }

        public BigDecimal getMontant() {
            return montant;
        }

        public void setMontant(BigDecimal montant) {
            this.montant = montant;
        }

        public Integer getValeur() {
            return valeur;
        }

        public void setValeur(Integer valeur) {
            this.valeur = valeur;
        }

        public BigDecimal getMtExedent() {
            return mtExedent;
        }

        public void setMtExedent(BigDecimal mtExedent) {
            this.mtExedent = mtExedent;
        }
    }

    @GetMapping("/periodes")
    public ResponseEntity<PeriodePaieDTO> getPeriodes() {
        try {
            PeriodePaieDTO periodeDTO = new PeriodePaieDTO();
            List<PeriodePaie> listPeriodepaie = periodePaieService.listperiodesupAuPret();
            
            if (listPeriodepaie.size() > 0) {
                periodeDTO.setRows(listPeriodepaie);
                periodeDTO.setResult("success");
            }
            
            return ResponseEntity.ok(periodeDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des périodes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/prets")
    public ResponseEntity<PretDTO> getPrets() {
        try {
            PretDTO pretDTO = new PretDTO();
            List<Pret> listPrets = pretService.listdesPret();
            
            if (listPrets.size() > 0) {
                pretDTO.setRows(listPrets);
                pretDTO.setResult("success");
            }
            
            return ResponseEntity.ok(pretDTO);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des prêts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<PrimePersonnelDTO> deletePrimePersonnel(@PathVariable Long id) {
        try {
            PrimePersonnelDTO result = primePersonnelService.delete(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la prime personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/rechercher/{id}")
    public ResponseEntity<PrimePersonnel> rechercherPrimePersonnel(@PathVariable Long id) {
        try {
            PrimePersonnel result = primePersonnelService.findprimepersonnel(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de la prime personnel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/prime-individuel")
    public ResponseEntity<List<PrimePersonnel>> getPrimeIndividuel(
            @RequestParam Long idPrime,
            @RequestParam Long idPeriode,
            @RequestParam Long idCtrat) {
        try {
            List<PrimePersonnel> result = primePersonnelService.listdesprimepersonnelPeriodePrime(idPeriode, idPrime, idCtrat);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la prime individuelle", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/prime-mois-encours/{id}")
    public ResponseEntity<PrimePersonnelDTO> getPrimeMoisEncours(
            @PathVariable Long id,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String search) {
        
        try {
            PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
            PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
            
            PrimePersonnelDTO result = primePersonnelService.listdesprimepersonnelMoisEnPrime(pageRequest, periodePaie.getId(), id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des primes du mois en cours", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/types-elements")
    public ResponseEntity<?> getTypesElements() {
        try {
            List<?> typesElements = rubriqueService.getRubriquesActives();
            return ResponseEntity.ok(typesElements);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des types d'éléments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        try {
            List<?> categories = societeService.findtsmois();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des catégories", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Enregistrement EN LOT (1 transaction, 1 saveAll) d'une prime collective.
     * Accepte la liste des idPersonnels et crée tous les PrimePersonnel en une seule fois.
     */
    @PostMapping("/enregistrer-collective-batch")
    public ResponseEntity<PrimePersonnelDTO> savePrimeCollectiveBatch(@RequestBody PrimeCollectiveBatchRequest request) {
        try {
            Long idPeriode = request.getIdPeriode();
            if (idPeriode == null) {
                PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
                idPeriode = periodePaie != null ? periodePaie.getId() : null;
            }

            PrimePersonnelDTO result = primePersonnelService.savePrimeCollectiveBatch(
                request.getIdPrime(),
                request.getMontantop(),
                request.getValeurop(),
                idPeriode,
                request.getIdPersonnels()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur enregistrement prime collective en lot", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/enregistrer-collective")
    public ResponseEntity<PrimePersonnelDTO> savePrimeCollective(@RequestBody PrimePersonnelRequest request) {
        try {
            PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
            Long idCtrat = request.getIdCtrat() != null ? request.getIdCtrat() : request.getCtrat();
            
            PrimePersonnelDTO result = primePersonnelService.savePrimeCollective(
                request.getId(),
                request.getMontantop(),
                request.getValeurop(),
                request.getIdAbsence(),
                idCtrat,
                periodePaie.getId(),
                request.getIdPrime(),

                request.getIdPersonnel()

            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la prime collective", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

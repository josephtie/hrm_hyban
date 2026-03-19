package com.nectux.mizan.hyban.personnel.web;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.personnel.dto.ContratPersonnelDTO;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.personnel.entity.Personnel;
import com.nectux.mizan.hyban.personnel.repository.ContratPersonnelRepository;
import com.nectux.mizan.hyban.personnel.service.ContratPersonnelService;
import com.nectux.mizan.hyban.personnel.service.PersonnelService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.repository.PeriodePaieRepository;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

import jakarta.persistence.EntityNotFoundException;

import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.PaginationIdRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.ContratPersonnelRequest;
import com.nectux.mizan.hyban.common.dto.EndContractRequest;
import com.nectux.mizan.hyban.common.dto.SursalaireRequest;
import com.nectux.mizan.hyban.common.dto.ContratPersonnelFilterRequest;

@RestController
@RequestMapping("/api/personnels")
@CrossOrigin(
    origins = {"http://localhost:7153", "http://192.168.1.2:7153", "http://192.168.1.2:8080", "http://192.168.1.3:7156", "http://192.168.1.3:7157", "http://192.168.1.3:7158", "http://192.168.1.3:7159", "http://192.168.1.3:7160", "http://192.168.1.3:7161", "http://192.168.1.3:7162", "http://192.168.1.3:7163", "http://192.168.1.3:7164", "http://192.168.1.3:7165", "http://192.168.1.3:7166", "http://192.168.1.3:7167", "http://192.168.1.3:7168", "http://192.168.1.3:7169", "http://192.168.1.3:7170", "http://192.168.1.3:7171", "http://192.168.1.3:7172", "http://192.168.1.3:7173", "http://192.168.1.3:7174", "http://192.168.1.3:7175", "http://192.168.1.3:7176", "http://192.168.1.3:7177", "http://192.168.1.3:7178", "http://192.168.1.3:7179", "http://192.168.1.3:7180", "http://192.168.1.3:7181", "http://192.168.1.3:7182", "http://192.168.1.3:7183", "http://192.168.1.3:7184", "http://192.168.1.3:7185", "http://192.168.1.3:7186", "http://192.168.1.3:7187", "http://192.168.1.3:7188", "http://192.168.1.3:7189", "http://192.168.1.3:7190", "http://192.168.1.3:7191", "http://192.168.1.3:7192", "http://192.168.1.3:7193", "http://192.168.1.3:7194", "http://192.168.1.3:7195", "http://192.168.1.3:7196", "http://192.168.1.3:7197", "http://192.168.1.3:7198", "http://192.168.1.3:7199"},
    allowCredentials = "true"
)
public class ContratPersonnelController {

    private static final Logger logger = LogManager.getLogger(PersonnelController.class);

	@Autowired private PersonnelService personnelService;
	@Autowired private ContratPersonnelService contratPersonnelService;
	// @Autowired private UtilisateurService userService;
	@Autowired private PeriodePaieService periodePaieService;
	@Autowired PeriodePaieRepository periodePaieRepository;
    @Autowired private SocieteService societeService;
    // @Autowired private UtilisateurRoleService utilisateurRoleService;
    @Autowired private ContratPersonnelRepository contratPersonnelRepository;

	@RequestMapping("/contrat")
    public String viewService(org.springframework.ui.ModelMap modelMap, Principal principal) throws IOException {
        logger.info(">>>>> Utilisateurs");
        
        modelMap.addAttribute("activeEmployers", "active");
        modelMap.addAttribute("blockEmployer", "block");
        modelMap.addAttribute("activeContract", "active");
        // modelMap.addAttribute("user", userService.findByUsername(principal.getName()));
        // Utilisateur utilisateur=userService.findByUsername(principal.getName());
        // System.out.println("utilisateur    " +utilisateur.toString());
        // modelMap.addAttribute("profil", utilisateur.getUtilisateurRoles().stream()
        // .map(utilisateurRole -> utilisateurRole.getRole().getName().name()) 
        // .findFirst().orElse(""));
        modelMap.addAttribute("icon", "iconfa-group");
        modelMap.addAttribute("littleTitle", "Personnel");
        modelMap.addAttribute("bigTitle", "Contrat");
        PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
        if(periodePaie != null){
            modelMap.addAttribute("activeMois", periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
            modelMap.addAttribute("activeMoisId", periodePaie.getId());
            modelMap.addAttribute("periode",  periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
        }
        Societe mysociete=null;
        List<Societe> malist=societeService.findtsmois();
        if(malist.size()>0)
        {   mysociete=malist.get(0);            
            modelMap.addAttribute("urllogo",mysociete.getUrlLogo());
        }

        return "contrat";
    }

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listcontratparpersonneljson")
	public @ResponseBody ContratPersonnelDTO getContratPersonnelListJSON(@RequestBody PaginationIdRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		String search = req.getSearch();
		Personnel personnel = personnelService.findPersonnel(req.getId());
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "statut");
		ContratPersonnelDTO contratPersonnelDTO =
                (search == null || search.isEmpty()) ? contratPersonnelService.loadContratByPersonnel(personnel, pageRequest) : contratPersonnelService.loadContratByPersonnel(personnel, pageRequest, search);
		return contratPersonnelDTO;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listcontratpersonnelActifjson")
	public @ResponseBody ContratPersonnelDTO getContratPersonnelListActifJSON(@RequestBody PaginationRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		String search = req.getSearch();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
		ContratPersonnelDTO contratPersonnelDTO = (search == null || search.isEmpty()) ?
                contratPersonnelService.loadContratPersonnelActif(pageRequest) : contratPersonnelService.loadContratPersonnelActif(pageRequest, search, search);
		return contratPersonnelDTO;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listcontratparpersonnel")
	public @ResponseBody List<ContratPersonnel> listContratParPersonnel(@RequestBody IdRequest req) {
		Personnel personnel = personnelService.findPersonnel(req.getId());
		return contratPersonnelService.findByPersonnel(personnel);
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listcontratpersonneljson")
	public @ResponseBody ContratPersonnelDTO getContratListJSON(@RequestBody PaginationRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		String search = req.getSearch();
		PageRequest pageRequest = PageRequest.of(offset , limit, Direction.DESC, "id");
		ContratPersonnelDTO contratPersonnelDTO = (search == null || search.isEmpty()) ? contratPersonnelService.loadContratActif(pageRequest) : contratPersonnelService.loadContratActif(pageRequest, search);
		return contratPersonnelDTO;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listcontratpersonnelfilterjson")
	public @ResponseBody ContratPersonnelDTO getContratListFilterJSON(@RequestBody ContratPersonnelFilterRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset , limit, Direction.DESC, "id");
		
		// Créer les filtres pour la méthode findAllfilter
		Map<String, String> filters = new java.util.HashMap<>();
		
		// Ajouter le filtre de recherche textuelle si présent
		if (req.getSearch() != null && !req.getSearch().isEmpty()) {
			filters.put("search", req.getSearch());
		}
		
		// Filtre par statut
		if (req.getStatut() != null && !req.getStatut().isEmpty()) {
			filters.put("statut", req.getStatut());
		}
		
		// Filtre par type de contrat
		if (req.getTypeContrat() != null && !req.getTypeContrat().isEmpty()) {
			filters.put("typeContrat", req.getTypeContrat());
		}
		
		// Filtre par salaire
		if (req.getSalaireRange() != null && !req.getSalaireRange().isEmpty()) {
			filters.put("salaireRange", req.getSalaireRange());
		}
		
		// Filtre par état contractuel (carec)
		if (req.getCarec() != null) {
			filters.put("carec", req.getCarec().toString());
		}
		
		// Filtres avancés d'expiration
		if (req.getExpireDate() != null && !req.getExpireDate().isEmpty()) {
			filters.put("expireDate", req.getExpireDate());
		}
		
		if (req.getExpirePeriodStart() != null && !req.getExpirePeriodStart().isEmpty() &&
			req.getExpirePeriodEnd() != null && !req.getExpirePeriodEnd().isEmpty()) {
			filters.put("expirePeriodStart", req.getExpirePeriodStart());
			filters.put("expirePeriodEnd", req.getExpirePeriodEnd());
		}
		
		return contratPersonnelService.findAllfilter(filters, pageRequest);
	}

	// Méthode de test pour diagnostiquer le problème carec
	@GetMapping("/debug-carec")
	@ResponseBody
	public Map<String, Object> debugCarec() {
		Map<String, Object> result = new HashMap<>();
		
		// Test simple: compter tous les contrats et voir les carec associés
		List<ContratPersonnel> allContrats = contratPersonnelRepository.findAll();
		Map<Boolean, Long> carecCount = allContrats.stream()
			.collect(java.util.stream.Collectors.groupingBy(
				c -> c.getPersonnel() != null ? c.getPersonnel().getCarec() : false,
				java.util.stream.Collectors.counting()
			));
		
		result.put("totalContrats", allContrats.size());
		result.put("carecStats", carecCount);
		result.put("message", "Statistiques des contrats par état carec du personnel");
		
		return result;
	}
	
	// Méthode de test pour diagnostiquer le problème des contrats inactifs
	@GetMapping("/debug-inactifs")
	@ResponseBody
	public Map<String, Object> debugInactifs() {
		Map<String, Object> result = new HashMap<>();
		
		// Récupérer tous les contrats
		List<ContratPersonnel> allContrats = contratPersonnelRepository.findAll();
		
		// Analyser les contrats selon le statut
		List<Map<String, Object>> details = new ArrayList<>();
		int countStatutFalse = 0;
		int countStatutTrue = 0;
		int countStatutNull = 0;
		
		for (ContratPersonnel contrat : allContrats) {
			Boolean statut = contrat.getStatut();
			boolean isStatutFalse = statut != null && !statut;
			boolean isStatutTrue = statut != null && statut;
			boolean isStatutNull = statut == null;
			
			Map<String, Object> detail = new HashMap<>();
			detail.put("id", contrat.getId());
			detail.put("personnel", contrat.getPersonnel().getNom() + " " + contrat.getPersonnel().getPrenom());
			detail.put("dateFin", contrat.getDateFin());
			detail.put("statut", statut);
			detail.put("isStatutFalse", isStatutFalse);
			detail.put("isStatutTrue", isStatutTrue);
			detail.put("isStatutNull", isStatutNull);
			detail.put("shouldBeInactive", isStatutFalse); // Logique simplifiée
			
			details.add(detail);
			
			if (isStatutFalse) countStatutFalse++;
			if (isStatutTrue) countStatutTrue++;
			if (isStatutNull) countStatutNull++;
		}
		
		result.put("today", new java.util.Date());
		result.put("totalContrats", allContrats.size());
		result.put("countStatutFalse", countStatutFalse);
		result.put("countStatutTrue", countStatutTrue);
		result.put("countStatutNull", countStatutNull);
		result.put("expectedInactive", countStatutFalse); // Uniquement les statut = false
		result.put("details", details);
		result.put("message", "Analyse des contrats inactifs (statut = false uniquement)");
		
		return result;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listcontratpersonnelDepartjson")
	public @ResponseBody ContratPersonnelDTO getContratListDepartJSON(@RequestBody PaginationRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		String search = req.getSearch();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
		ContratPersonnelDTO contratPersonnelDTO = (search == null || search.isEmpty()) ? contratPersonnelService.loadContratDepart(pageRequest) : contratPersonnelService.loadContratDepart(pageRequest, search);
		return contratPersonnelDTO;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/savecontratpersonnel")
	public @ResponseBody ContratPersonnelDTO saveContratpers(@RequestBody ContratPersonnelRequest req) {
		return contratPersonnelService.save(req.getId(), req.getIdPersonnel(), req.getIdCategorie(), req.getIdFonction(), req.getIdTypeContrat(), req.getDateDebut(), req.getDateFin(), req.getNetAPayer(), req.getIndemniteLogement(), req.getAncienete(), true, req.getSursalaire(), req.getIndemnitetransport(), req.getIndemniterespons(), req.getIndemniterepresent());
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/endcontratpersonnel")
	public @ResponseBody ContratPersonnelDTO updateContratPersonnel(@RequestBody EndContractRequest req) {
		String [] part = req.getDateFin().split("-");
		String [] part1 = req.getDateMod().split("-");
		String date = part[2] + "/" + part[1] + "/" + part[0];
		String datemod1 = part1[2] + "/" + part1[1] + "/" + part1[0];
		return contratPersonnelService.endContract(req.getId(), date, datemod1, req.getPermanent(), req.getObservCtrat());
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/updatecontratpersonnelSursal")
	public @ResponseBody ContratPersonnelDTO updateContratPersonnelsursal(@RequestBody SursalaireRequest req) {
		ContratPersonnel ctratpersonnel = contratPersonnelRepository.findByPersonnelIdAndStatut(req.getIdPersonnel(), true);
		return contratPersonnelService.updateContractSursalaire(ctratpersonnel.getId(), req.getSursalaire());
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/deletecontratpersonnel")
	public @ResponseBody ContratPersonnelDTO deletecontratpers(@RequestBody IdRequest req) {
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		contratPersonnelDTO.setResult(contratPersonnelService.delete(req.getId()));
		return contratPersonnelDTO;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listcontratpersonnelExpjson")
	public @ResponseBody ContratPersonnelDTO getContratListExpiredJSON(@RequestBody PaginationIdRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset , limit, Direction.DESC, "id");
		PeriodePaie period = periodePaieRepository.findById(req.getId()).orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + req.getId()));
		String dateDeb = period.getDdeb();
		String dateFin = period.getDfin();
		ContratPersonnelDTO contratPersonnelDTO = contratPersonnelService.loadContratExpieredumois(pageRequest, 2L, dateDeb, dateFin);
		return contratPersonnelDTO;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listcontratpersonnelExpPeriodejson")
	public @ResponseBody ContratPersonnelDTO getContratListExpiredPeriodeJSON(@RequestBody PaginationIdRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset , limit, Direction.DESC, "id");
		ContratPersonnelDTO contratPersonnelDTO = contratPersonnelService.loadContratExpieredumois(pageRequest, 2L, req.getId().toString(), req.getSearch());
		return contratPersonnelDTO;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listcontratpersonnelExpDatejson")
	public @ResponseBody ContratPersonnelDTO getContratListExpiredDateJSON(@RequestBody PaginationIdRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset , limit, Direction.DESC, "id");
		ContratPersonnelDTO contratPersonnelDTO = contratPersonnelService.loadContratExpieredumois(pageRequest, 2L, convertirDate(req.getSearch()), convertirDate(req.getId().toString()));
		return contratPersonnelDTO;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listexpirecontratpersonnel")
	public @ResponseBody List<ContratPersonnel> getExpireContract(Principal principal) throws Exception {
		return contratPersonnelService.findExpireContract();
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listexpirecontratpersonneldays")
	public @ResponseBody List<ContratPersonnel> getExpireContractday15(Principal principal) throws Exception {
		return contratPersonnelService.findExpireContract(14);
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/cherchcontratpersonnelActif")
	public @ResponseBody ContratPersonnelDTO getChearchContract(@RequestBody IdRequest req, Principal principal) throws Exception {
		return contratPersonnelService.findContratPersonnelk(req.getId());
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listexpirecontratpersonnelDelai")
	public @ResponseBody List<ContratPersonnel> getExpireContractDelai(@RequestBody com.nectux.mizan.hyban.common.dto.NbreRequest req,Principal principal) throws Exception {
		return contratPersonnelService.findExpireContract(req.getNbre());
	}


	public static String convertirDate(String dateIso) {
		if (dateIso == null || !dateIso.matches("\\d{4}-\\d{2}-\\d{2}")) {
			throw new IllegalArgumentException("Format de date invalide. Attendu : AAAA-MM-JJ");
		}

		String[] parties = dateIso.split("-");
		String annee = parties[0];
		String mois = parties[1];
		String jour = parties[2];

		return jour + "/" + mois + "/" + annee;
	}

}

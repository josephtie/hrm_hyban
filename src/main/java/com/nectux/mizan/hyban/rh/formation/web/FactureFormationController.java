package com.nectux.mizan.hyban.rh.formation.web;

import com.nectux.mizan.hyban.common.dto.FactureFormationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.formation.dto.FactureFormationDTO;
import com.nectux.mizan.hyban.rh.formation.service.FactureFormationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/formation")
public class FactureFormationController {
	
	private static final Logger logger = LogManager.getLogger(FactureFormationController.class);
	
	@Autowired
    private FactureFormationService factureFormationService;
	@Autowired
    private SocieteService societeService;
	// @Autowired
	// private UtilisateurService utilisateurService;
	@Autowired
    private PeriodePaieService periodePaieService;
	// @Autowired
	// private UtilisateurRoleService utilisateurRoleService;
	 
	@RequestMapping("/factureformation")
	public String viewFactureFormation(ModelMap modelMap, Principal principal) throws IOException {
		logger.info(">>>>> FactureFormations");

		// modelMap.addAttribute("user", utilisateurService.findByUsername(principal.getName()));
		// Utilisateur utilisateur=utilisateurService.findByUsername(principal.getName());
		// System.out.println("utilisateur    " +utilisateur.toString());
		// modelMap.addAttribute("profil", utilisateur.getUtilisateurRoles().stream()
		//		.map(utilisateurRole -> utilisateurRole.getRole().getName().name())
		//		.findFirst().orElse(""));
		modelMap.addAttribute("icon", "iconfa-book");
		modelMap.addAttribute("littleTitle", "Formations");
		modelMap.addAttribute("bigTitle", "Facture Formation");
		PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
  		if(periodePaie != null){
 	    	modelMap.addAttribute("activeMois", periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
 			modelMap.addAttribute("activeMoisId", periodePaie.getId());
 			 modelMap.addAttribute("periode",  periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
 	    }
		Societe mysociete=null;
		java.util.List<Societe> malist=societeService.findtsmois();
		if(malist.size()>0)
		{mysociete=malist.get(0);
			modelMap.addAttribute("urllogo",mysociete.getUrlLogo());}
		return "factureformation";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/enregistrerfactureformation")
	public @ResponseBody
    FactureFormationDTO saveFactureFormation(@RequestBody FactureFormationRequest request) throws Exception {
		logger.info(">>> ENREGISTRER FACTURE FORMATION");
		return factureFormationService.save(request.getId(), request.getIdCabinet(), request.getIdFormation(), request.getReference(), request.getEmission(), request.getMontant(), request.getStatut());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/trouverfactureformation")
	public @ResponseBody
    FactureFormationDTO findFactureFormation(@RequestBody IdRequest request) {
		logger.info(">>> TROUVER FACTURE FORMATION");
		return factureFormationService.findFactureFormation(request.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listerfactureformations")
	public @ResponseBody
    FactureFormationDTO findFactureFormations() {
		logger.info(">>> LISTE FACTURE FORMATIONS");
		return factureFormationService.findFactureFormations();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listerfactureformationsparformation")
	public @ResponseBody
    FactureFormationDTO findFactureFormationsByFormation(@RequestBody IdRequest request) {
		logger.info(">>> LISTE FACTURE FORMATIONS");
		return factureFormationService.findFactureFormationByFormation(request.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/supprimerfactureformation")
	public @ResponseBody
    FactureFormationDTO deleteFactureFormation(@RequestBody IdRequest request) {
		logger.info(">>> SUPPRIMER FACTURE FORMATION");
		return factureFormationService.delete(request.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/paginerfactureformations")
	public @ResponseBody
    FactureFormationDTO getUserListJSON(@RequestBody PaginationRequest request, Principal principal) {
		logger.info(">>> LISTE FACTURE FORMATIONS AVEC PAGINATION");
		Integer offset = request.getOffset() == null ? 0 : request.getOffset();
    	Integer limit = request.getLimit() == null ? 10 : request.getLimit();
    	PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
    	FactureFormationDTO factureFormationDTO;
    	if (request.getSearch() == null || request.getSearch().isEmpty()) {
    	    factureFormationDTO = factureFormationService.loadFactureFormations(pageRequest);
    	} else {
    	    factureFormationDTO = factureFormationService.loadFactureFormations(pageRequest, request.getSearch(), request.getSearch(), request.getSearch());
    	}
    	return factureFormationDTO;
	}

}

package com.nectux.mizan.hyban.rh.formation.web;

import com.nectux.mizan.hyban.common.dto.CabinetFormationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.carriere.web.PosteController;
import com.nectux.mizan.hyban.rh.formation.dto.CabinetFormationDTO;
import com.nectux.mizan.hyban.rh.formation.service.CabinetFormationService;
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
public class CabinetFormationController {
	
	private static final Logger logger = LogManager.getLogger(CabinetFormationController.class);
	@Autowired private SocieteService societeService;
	@Autowired
    private CabinetFormationService cabinetFormationService;
	// @Autowired
	// private UtilisateurService utilisateurService;
	@Autowired
    private PeriodePaieService periodePaieService;
	// @Autowired
	// private UtilisateurRoleService utilisateurRoleService;
	@RequestMapping("/cabinetformation")
	public String viewPoste(ModelMap modelMap, Principal principal) throws IOException {
		logger.info(">>>>> Postes");
		
		modelMap.addAttribute("activeTraining", "active");
		modelMap.addAttribute("blockTraining", "block");
		modelMap.addAttribute("activeFirmTraning", "active");
		// modelMap.addAttribute("user", utilisateurService.findByUsername(principal.getName()));
		// Utilisateur utilisateur=utilisateurService.findByUsername(principal.getName());
		// System.out.println("utilisateur    " +utilisateur.toString());
		// modelMap.addAttribute("profil", utilisateur.getUtilisateurRoles().stream()
		//		.map(utilisateurRole -> utilisateurRole.getRole().getName().name())
		//		.findFirst().orElse(""));

		modelMap.addAttribute("icon", "iconfa-book");
		modelMap.addAttribute("littleTitle", "Formation");
		modelMap.addAttribute("bigTitle", "Cabinet Formation");
		PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
		if(periodePaie != null){
			modelMap.addAttribute("activeMois", periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
			modelMap.addAttribute("activeMoisId", periodePaie.getId());
			modelMap.addAttribute("periode",  periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
		}
		Societe mysociete=null;
		java.util.List<Societe> malist=societeService.findtsmois();
		if(malist.size()>0)
		{	mysociete=malist.get(0);
			modelMap.addAttribute("urllogo",mysociete.getUrlLogo());}
		return "cabinetformations";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/enregistrercabinetformation", method = RequestMethod.POST)
	public @ResponseBody
    CabinetFormationDTO saveCabinetFormation(@RequestBody CabinetFormationRequest request) throws Exception {
		logger.info(">>> ENREGISTRER POSTE");
		return cabinetFormationService.save(request.getId(), request.getNom(), request.getAdresse(), request.getEmail(), request.getTelephone());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/trouvercabinetformation", method = RequestMethod.POST)
	public @ResponseBody
    CabinetFormationDTO findPoste(@RequestBody IdRequest request) {
		logger.info(">>> TROUVER POSTE");
		return cabinetFormationService.findCabinetFormation(request.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/listercabinetformations", method = RequestMethod.POST)
	public @ResponseBody
    CabinetFormationDTO findPostes() {
		logger.info(">>> LISTE POSTES");
		return cabinetFormationService.findCabinetFormations();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/supprimercabinetformation", method = RequestMethod.POST)
	public @ResponseBody
    CabinetFormationDTO deletePoste(@RequestBody IdRequest request) {
		logger.info(">>> SUPPRIMER POSTE");
		return cabinetFormationService.delete(request.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/paginercabinetformations", method = RequestMethod.POST)
	public @ResponseBody
    CabinetFormationDTO getUserListJSON(@RequestBody PaginationRequest request, Principal principal) {
		logger.info(">>> LISTE POSTES AVEC PAGINATION");
		Integer offset = request.getOffset() == null ? 0 : request.getOffset();
		Integer limit = request.getLimit() == null ? 10 : request.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		CabinetFormationDTO cabinetFormationDTO;
		if (request.getSearch() == null || request.getSearch().isEmpty()) {
			cabinetFormationDTO = cabinetFormationService.loadCabinetFormations(pageRequest);
		} else {
			cabinetFormationDTO = cabinetFormationService.loadCabinetFormations(pageRequest, request.getSearch(), request.getSearch(), request.getSearch(), request.getSearch());
		}
		return cabinetFormationDTO;
	}

}

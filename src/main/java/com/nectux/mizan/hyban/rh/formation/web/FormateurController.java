package com.nectux.mizan.hyban.rh.formation.web;

import com.nectux.mizan.hyban.common.dto.FormateurRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.formation.dto.FormateurDTO;
import com.nectux.mizan.hyban.rh.formation.service.FormateurService;
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
public class FormateurController {
	
	private static final Logger logger = LogManager.getLogger(FormateurController.class);
	
	@Autowired
	private FormateurService formateurService;
	@Autowired
    private SocieteService societeService;
	// @Autowired
	// private UtilisateurService utilisateurService;
	@Autowired
    private PeriodePaieService periodePaieService;
	// @Autowired
	// private UtilisateurRoleService utilisateurRoleService;
	 
	@RequestMapping("/formateurs")
	public String viewFormateur(ModelMap modelMap, Principal principal) throws IOException {
		logger.info(">>>>> Formateur");
		
		modelMap.addAttribute("activeTraining", "active");
		modelMap.addAttribute("blockTraining", "block");
		modelMap.addAttribute("activeTeacher", "active");
		// modelMap.addAttribute("user", utilisateurService.findByUsername(principal.getName()));
		// Utilisateur utilisateur=utilisateurService.findByUsername(principal.getName());
		// System.out.println("utilisateur    " +utilisateur.toString());

		// modelMap.addAttribute("profil", utilisateur.getUtilisateurRoles().stream()
		// 			.map(utilisateurRole -> utilisateurRole.getRole().getName().name())
		// 			.findFirst().orElse(""));
		modelMap.addAttribute("icon", "iconfa-book");
		modelMap.addAttribute("littleTitle", "Formation");
		modelMap.addAttribute("bigTitle", "Formateur");
		PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
  	    if(periodePaie != null){
 	    	modelMap.addAttribute("activeMois", periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
 			modelMap.addAttribute("activeMoisId", periodePaie.getId());
 			 modelMap.addAttribute("periode",  periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
 	    }
		Societe mysociete=null;
		java.util.List<Societe> malist=societeService.findtsmois();
		if(malist.size()>0)
		{ mysociete=malist.get(0);
			modelMap.addAttribute("urllogo",mysociete.getUrlLogo());}
		return "formateurs";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/enregistrerformateur")
	public @ResponseBody FormateurDTO saveFormateur(@RequestBody FormateurRequest req) throws Exception {
		logger.info(">>> ENREGISTRER FORMATEUR");
		char sexeChar = (req.getSexe() == null || req.getSexe().isEmpty()) ? ' ' : req.getSexe().charAt(0);
		int sitMat = 0;
		try {
			if(req.getSituationMatrimonial() != null && !req.getSituationMatrimonial().isEmpty()) {
				sitMat = Integer.parseInt(req.getSituationMatrimonial());
			}
		} catch(NumberFormatException nfe) {
			// fallback to 0
		}
		return formateurService.save(req.getId(), req.getCabinetFormationId(), req.getNom(), req.getCivilite(), sexeChar, sitMat, req.getDateNaissance(), req.getLieuNaissance(), req.getFixe(), req.getMobile());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/trouverformateur")
	public @ResponseBody FormateurDTO findFormateur(@RequestBody IdRequest req) {
		logger.info(">>> TROUVER FORMATEUR");
		return formateurService.findFormateur(req.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listerformateurs")
	public @ResponseBody FormateurDTO findFormateurs() {
		logger.info(">>> LISTE FORMATEURS");
		return formateurService.findFormateurs();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/supprimerformateur")
	public @ResponseBody FormateurDTO deleteFormateur(@RequestBody IdRequest req) {
		logger.info(">>> SUPPRIMER FORMATEUR");
		return formateurService.delete(req.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/paginerformateurs")
	public @ResponseBody FormateurDTO getUserListJSON(@RequestBody PaginationRequest req) {
		logger.info(">>> LISTE FORMATEURS AVEC PAGINATION");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		FormateurDTO formateurDTO = (req.getSearch() == null || req.getSearch().isEmpty()) ? formateurService.loadFormateurs(pageRequest) : formateurService.loadFormateurs(pageRequest, req.getSearch(), req.getSearch(), req.getSearch(), req.getSearch(), req.getSearch(), req.getSearch(), req.getSearch());
		return formateurDTO;
	}
}

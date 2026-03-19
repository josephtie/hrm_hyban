package com.nectux.mizan.hyban.rh.formation.web;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.carriere.web.PosteController;
import com.nectux.mizan.hyban.rh.formation.dto.FormationPersonnelDTO;
import com.nectux.mizan.hyban.rh.formation.service.FormationPersonnelService;
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

import com.nectux.mizan.hyban.common.dto.FormationPersonnelRequest;
import com.nectux.mizan.hyban.common.dto.ListPersonnelRequest;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.PaginationIdRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;

@Controller
@RequestMapping("/formation")
public class FormationPersonnelController {
	
	private static final Logger logger = LogManager.getLogger(PosteController.class);


	@Autowired
    private FormationPersonnelService formationPersonnelService;
	@Autowired
    private SocieteService societeService;
	// @Autowired
    // private UtilisateurService utilisateurService;
	@Autowired
    private PeriodePaieService periodePaieService;
	// @Autowired
    // private UtilisateurRoleService utilisateurRoleService;
	@RequestMapping("/formationpersonnel")
	public String viewPoste(ModelMap modelMap, Principal principal) throws IOException {
		logger.info(">>>>> Formation Personnel");

		// modelMap.addAttribute("user", utilisateurService.findByUsername(principal.getName()));
		// Utilisateur utilisateur=utilisateurService.findByUsername(principal.getName());
		// System.out.println("utilisateur    " +utilisateur.toString());
		// modelMap.addAttribute("profil", utilisateur.getUtilisateurRoles().stream()
		// 			.map(utilisateurRole -> utilisateurRole.getRole().getName().name())
		// 			.findFirst().orElse(""));
		modelMap.addAttribute("icon", "iconfa-book");
		modelMap.addAttribute("littleTitle", "Formations");
		modelMap.addAttribute("bigTitle", "Participants Formation");
		PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
 	    	if(periodePaie != null){
 	    		modelMap.addAttribute("activeMois", periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
 			modelMap.addAttribute("activeMoisId", periodePaie.getId());
 			 modelMap.addAttribute("periode",  periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
 	    	}

		Societe mysociete=null;
		java.util.List<Societe> malist=societeService.findtsmois();
		if(malist.size()>0) {
            mysociete = malist.get(0);
            modelMap.addAttribute("urllogo", mysociete.getUrlLogo());
        }
		return "formationpersonnel";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/enregistrerformationpersonnel")
	public @ResponseBody FormationPersonnelDTO saveFormationPeronnel(@RequestBody FormationPersonnelRequest req) throws Exception {
		logger.info(">>> ENREGISTRER FORMATION PERSONNEL");
		return formationPersonnelService.save(req.getId(), req.getIdFormation(), req.getIdPersonnel());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/enregistrerformationpersonnellist")
	public @ResponseBody FormationPersonnelDTO savePointagesByPersonnelsAndDate(@RequestBody ListPersonnelRequest req) throws Exception {
		System.out.println("variable personnel  "+req.getListPersonnel());
		return formationPersonnelService.save(req.getListPersonnel(), req.getListSize() == null ? 0 : req.getListSize(), req.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/trouverformationpersonnel")
	public @ResponseBody FormationPersonnelDTO findFormationPersonnel(@RequestBody IdRequest req) {
		logger.info(">>> TROUVER FORMATION PERSONNEL");
		return formationPersonnelService.findFormationPersonnel(req.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listerformationpersonnels")
	public @ResponseBody FormationPersonnelDTO findPostes() {
		logger.info(">>> LISTE FORMATION PERSONNELS");
		return formationPersonnelService.findFormationPersonnels();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listerformationpersonnelsparlistpersonneletidformation")
	public @ResponseBody FormationPersonnelDTO findPointagesByPersonnelsAndDate(@RequestBody ListPersonnelRequest req) throws Exception {
		return formationPersonnelService.findFormationPersonnel(req.getListPersonnel(), req.getListSize() == null ? 0 : req.getListSize(), req.getId());
	}



	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/supprimerformationpersonnel")
	public @ResponseBody FormationPersonnelDTO deletePoste(@RequestBody IdRequest req) {
		logger.info(">>> SUPPRIMER FORMATION PERSONNEL");
		return formationPersonnelService.delete(req.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/paginerformationpersonnels")
	public @ResponseBody FormationPersonnelDTO getFormationPersonnelListJSON(@RequestBody PaginationRequest req, Principal principal) {
		logger.info(">>> LISTE FORMATION PERSONNELS AVEC PAGINATION");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		FormationPersonnelDTO formationPersonnelDTO = (req.getSearch() == null || req.getSearch().isEmpty()) ? formationPersonnelService.loadFormationPersonnels(pageRequest) : formationPersonnelService.loadFormationPersonnels(pageRequest, req.getSearch(), req.getSearch(), req.getSearch());
		return formationPersonnelDTO;
	}


	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/paginerformationpersonnelsduneformation")
	public @ResponseBody FormationPersonnelDTO getUserListJSON(@RequestBody PaginationIdRequest req, Principal principal) {
		logger.info(">>> LISTE FORMATION PERSONNELS AVEC PAGINATION");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		FormationPersonnelDTO formationPersonnelDTO = (req.getSearch() == null || req.getSearch().isEmpty()) ? formationPersonnelService.loadFormationPersonnelsduneFormation(pageRequest, req.getId()) : formationPersonnelService.loadFormationPersonnelsduneFormation(pageRequest, req.getId(), req.getSearch(), req.getSearch(), req.getSearch());
		return formationPersonnelDTO;
	}

}

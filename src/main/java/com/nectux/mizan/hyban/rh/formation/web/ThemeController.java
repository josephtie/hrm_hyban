package com.nectux.mizan.hyban.rh.formation.web;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.carriere.web.PosteController;
import com.nectux.mizan.hyban.rh.formation.dto.ThemeDTO;
import com.nectux.mizan.hyban.rh.formation.service.ThemeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

import com.nectux.mizan.hyban.common.dto.ThemeRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;

@RestController
@RequestMapping("/formation")
public class ThemeController {

	private static final Logger logger = LogManager.getLogger(ThemeController.class);

	@Autowired
    private ThemeService themeService;
	// @Autowired
    // private UtilisateurService utilisateurService;
	@Autowired
    private PeriodePaieService periodePaieService;
	// @Autowired
    // private UtilisateurRoleService utilisateurRoleService;

	@RequestMapping("/themes")
	public String viewPoste(org.springframework.ui.ModelMap modelMap, Principal principal) throws IOException {
		logger.info(">>>>> Theme init");
		return "themes";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/enregistrertheme")
	public ThemeDTO saveTheme(@RequestBody ThemeRequest req) throws Exception {
		logger.info(">>> ENREGISTRER POSTE");
		return themeService.save(req.getId(), req.getIntitule(), req.getDescription());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/trouvertheme")
	public ThemeDTO findTheme(@RequestBody IdRequest req) {
		logger.info(">>> TROUVER THEME");
		return themeService.findTheme(req.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listerthemes")
	public ThemeDTO findThemes() {
		logger.info(">>> LISTE THEMES");
		return themeService.findThemes();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/supprimertheme")
	public ThemeDTO deleteTheme(@RequestBody IdRequest req) {
		logger.info(">>> SUPPRIMER THEME");
		return themeService.delete(req.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/paginerthemes")
	public ThemeDTO getUserListJSON(@RequestBody PaginationRequest req, Principal principal) {
		logger.info(">>> LISTE THEMES AVEC PAGINATION");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		ThemeDTO themeDTO = (req.getSearch() == null || req.getSearch().isEmpty()) ? themeService.loadThemes(pageRequest) : themeService.loadThemes(pageRequest, req.getSearch(), req.getSearch());
		return themeDTO;
	}

}

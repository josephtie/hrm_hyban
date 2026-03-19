package com.nectux.mizan.hyban.personnel.web;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.parametrages.web.UtilisateurController;
import com.nectux.mizan.hyban.personnel.dto.CategorieDTO;
import com.nectux.mizan.hyban.personnel.dto.RequestCategory;
import com.nectux.mizan.hyban.personnel.entity.Categorie;
import com.nectux.mizan.hyban.personnel.service.CategorieService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;

@RestController

@RequestMapping("/api/categories")

//@CrossOrigin(
//        origins = {"http://localhost:7153", "http://localhost:4200", "http://127.0.0.1:3000"},
//        allowCredentials = "true"
//)
public class CategorieApiController {

	private static final Logger logger = LogManager.getLogger(CategorieApiController.class);

	@Autowired private CategorieService categorieService;
	// @Autowired private UtilisateurService utilisateurService;
	@Autowired private PeriodePaieService periodePaieService;
	@Autowired private SocieteService societeService;
	// @Autowired private UtilisateurRoleService utilisateurRoleService;

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listcategoriejson")
	public @ResponseBody CategorieDTO getCategoryListJSON(@RequestBody PaginationRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		String search = req.getSearch();

		PageRequest pageRequest = PageRequest.of(offset , limit, Direction.ASC, "salaireDeBase");
		CategorieDTO categorieDTO = new CategorieDTO();
		if(search == null)
			categorieDTO = categorieService.loadCategorie(pageRequest);
		else
			categorieDTO = categorieService.loadCategorie(pageRequest, search );

		System.out.println("les categories " +categorieDTO.getRows());
		return categorieDTO;
	}

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public CategorieDTO saveCategory(@RequestBody RequestCategory dto) {
        return categorieService.save(
                dto.getId(),
                dto.getLibelle(),
                dto.getSalaireDeBase(),
                dto.getIndemniteLogement()
        );
    }

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/supprimercategorie")
	public @ResponseBody CategorieDTO deleteCategory(@RequestBody IdRequest req) {
		CategorieDTO categorieDTO = new CategorieDTO();
		categorieDTO.setResult(categorieService.delete(req.getId()));
		return categorieDTO;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/listcategorie")
	public @ResponseBody List<Categorie> getCategoryList() {
		return categorieService.findCategories();
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/affichcategorie")
	public @ResponseBody Categorie getCategory(@RequestBody IdRequest req) {
		return categorieService.findCategorie(req.getId());
	}
}

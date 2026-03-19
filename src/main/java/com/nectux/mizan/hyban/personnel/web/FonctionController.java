package com.nectux.mizan.hyban.personnel.web;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.personnel.service.FonctionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.personnel.dto.FonctionDTO;
import com.nectux.mizan.hyban.personnel.dto.RequestFonction;
import com.nectux.mizan.hyban.personnel.entity.Fonction;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;

@RestController
@RequestMapping("/api/personnels/fonctions")
public class FonctionController {

	private static final Logger logger = LogManager.getLogger(FonctionController.class);

	@Autowired private FonctionService fonctionService;
	// @Autowired private UtilisateurService utilisateurService;
	@Autowired private PeriodePaieService periodePaieService;
	@Autowired private SocieteService societeService;
	// @Autowired private UtilisateurRoleService utilisateurRoleService;

	@RequestMapping("/fonction")
    public ResponseEntity<Object> viewFonction(Principal principal) throws IOException {
		logger.info(">>>>> Fonction init");
		return ResponseEntity.ok().build();
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listfonctionjson")
	public @ResponseBody FonctionDTO getFonctionListJSON(@RequestBody PaginationRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		String search = req.getSearch();

		// Utiliser offset directement comme Categories (pas de division)
		PageRequest pageRequest = PageRequest.of(offset, limit, Direction.ASC, "libelle");
		FonctionDTO fonctionDTO = new FonctionDTO();
		if(search == null)
			fonctionDTO = fonctionService.loadFonction(pageRequest);
		else
			fonctionDTO = fonctionService.loadFonction(pageRequest, search );

		System.out.println("les fonctions " +fonctionDTO.getRows());
		return fonctionDTO;
	}

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public FonctionDTO saveFonction(@RequestBody RequestFonction dto) {
        return fonctionService.save(
                dto.getId(),
                dto.getLibelle(),
                dto.getDescription(),
                null, // serviceId retiré
                dto.getNiveau(),
                dto.getDepartement(),
                dto.getStatut()
        );
    }

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/deletefonction")
	public @ResponseBody FonctionDTO deleteFonction(@RequestBody IdRequest req) {
		FonctionDTO fonctionDTO = new FonctionDTO();
		fonctionDTO.setResult(fonctionService.delete(req.getId()));
		return fonctionDTO;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/listfonction")
	public @ResponseBody List<Fonction> getFonctionList() {
		return fonctionService.findFonctions();
	}

}

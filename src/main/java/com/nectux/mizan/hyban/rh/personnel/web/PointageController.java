package com.nectux.mizan.hyban.rh.personnel.web;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
import com.nectux.mizan.hyban.rh.personnel.dto.PointageDTO;
import com.nectux.mizan.hyban.rh.personnel.service.PointageService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.PointageSearchRequest;
import com.nectux.mizan.hyban.common.dto.PointageRequest;

@RestController
@RequestMapping("/api/personnel")
public class PointageController {

	private static final Logger logger = LogManager.getLogger(PointageController.class);

	@Autowired private PointageService pointageService;
	// @Autowired private UtilisateurService utilisateurService;
	@Autowired private PeriodePaieService periodePaieService;
	@Autowired private SocieteService societeService;
	// @Autowired private UtilisateurRoleService utilisateurRoleService;

	// Endpoint d'initialisation (remplace la vue JSP)
	@GetMapping("/pointages/init")
	public ResponseEntity<java.util.Map<String, Object>> initPointage(Principal principal) throws IOException {
		logger.info(">>>>> Pointages init");
		java.util.Map<String, Object> model = new java.util.HashMap<>();
		// Utilisateur utilisateur = utilisateurService.findByUsername(principal.getName());
		// model.put("user", utilisateur);
		// model.put("profil", utilisateur.getUtilisateurRoles().stream()
		//		.map(utilisateurRole -> utilisateurRole.getRole().getName().name())
		//		.findFirst().orElse(""));

		PeriodePaie periodePaie = periodePaieService.findPeriodeactive();
		if(periodePaie != null){
			model.put("activeMois", periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
			model.put("activeMoisId", periodePaie.getId());
			model.put("periode", periodePaie.getMois().getMois()+" "+ periodePaie.getAnnee().getAnnee());
		}
		List<Societe> malist = societeService.findtsmois();
		if(malist.size()>0) { model.put("urllogo", malist.get(0).getUrlLogo()); }
		return ResponseEntity.ok(model);
	}

	@PostMapping(value = "/enregistrerpointage")
	public ResponseEntity<PointageDTO> savePointage(@RequestBody PointageRequest req) throws Exception {
		logger.info(">>> ENREGISTRER POINTAGE");
		PointageDTO result = pointageService.save(req.getId(), req.getIdPersonnel(), req.getDatePointage(),
					req.getHeureArrivee(), req.getHeureDepart(), req.getHeurePause(), req.getHeureReprise(), req.getDescription());
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PostMapping(value = "/trouverpointage")
	public ResponseEntity<PointageDTO> findPointage(@RequestBody IdRequest req) {
		logger.info(">>> TROUVER POINTAGE");
		PointageDTO dto = pointageService.findPointage(req.getId());
		return ResponseEntity.ok(dto);
	}

	@PostMapping(value = "/trouverpointageparperiode")
	public ResponseEntity<PointageDTO> findPointageperiode(@RequestBody PointageSearchRequest req) {
		logger.info(">>> TROUVER POINTAGE PAR PERIODE");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		PointageDTO pointageDTO = pointageService.findPointagesByDatePointageBetween(pageRequest, req.getDatePointage(), req.getDatePointage1());
		return ResponseEntity.ok(pointageDTO);
	}

	@PostMapping(value = "/trouverpointagePersonneparperiode")
	public ResponseEntity<PointageDTO> findPointagePersonneperiode(@RequestBody PointageSearchRequest req) {
		logger.info(">>> TROUVER POINTAGE PERSONNE PAR PERIODE");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		PointageDTO pointageDTO = pointageService.findPointagesByPersonnelAndDatePointageBetween(pageRequest, req.getIdPersonnel(), req.getDatePointage(), req.getDatePointage1());
		return ResponseEntity.ok(pointageDTO);
	}

	@PostMapping(value = "/listerpointages")
	public ResponseEntity<PointageDTO> findPointages() {
		logger.info(">>> LISTE POINTAGES");
		return ResponseEntity.ok(pointageService.findPointages());
	}

	@PostMapping(value = "/listerpointagesparlistpersonneletdate")
	public ResponseEntity<PointageDTO> findPointagesByPersonnelsAndDate(@RequestBody PointageSearchRequest req) throws Exception {
		logger.info(">>> FIND BY LIST PERSONNELS AND DATE");
		List<Long> listPersonnel = req.getListPersonnel();
		if(listPersonnel == null) listPersonnel = new ArrayList<>();
		return ResponseEntity.ok(pointageService.findPointagesByPersonnelsAndDate(listPersonnel, req.getDatePointage()));
	}

	@PostMapping(value = "/supprimerpointage")
	public ResponseEntity<PointageDTO> deletePointage(@RequestBody IdRequest req) {
		logger.info(">>> SUPPRIMER POINTAGE");
		return ResponseEntity.ok(pointageService.delete(req.getId()));
	}

	@PostMapping(value = "/paginerpointages")
	public ResponseEntity<PointageDTO> getUserListJSON(@RequestBody PaginationRequest req, Principal principal) {
		logger.info(">>> LISTE POINTAGES AVEC PAGINATION");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		PointageDTO pointageDTO;
		if(req.getSearch() == null || req.getSearch().isEmpty())
			pointageDTO = pointageService.loadPointages(pageRequest);
		else
			pointageDTO = pointageService.loadPointages(pageRequest, req.getSearch(), req.getSearch());
		return ResponseEntity.ok(pointageDTO);
	}
}

package com.nectux.mizan.hyban.rh.personnel.web;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.rh.personnel.service.EnfantService;
import com.nectux.mizan.hyban.utils.DateManager;
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

//import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.personnel.dto.EnfantDTO;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.EnfantRequest;

@RestController
@RequestMapping("/api/personnel")
public class EnfantController {

	private static final Logger logger = LogManager.getLogger(EnfantController.class);

	@Autowired private EnfantService enfantService;
	//@Autowired private UtilisateurService utilisateurService;
	@Autowired private PeriodePaieService periodePaieService;

	@GetMapping("/enfants/init")
	public ResponseEntity<Object> viewEnfant(Principal principal) throws IOException {
		logger.info(">>>>> Enfants init");
		// Kept minimal info for frontend
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/enregistrerenfant")
	public ResponseEntity<EnfantDTO> saveEnfant(@RequestBody EnfantRequest req) throws Exception {
		logger.info(">>> ENREGISTRER ENFANT");
		Date dateNaissance = DateManager.stringToDate(req.getDateNaissanceString(), "dd/MM/yyyy");
		EnfantDTO res = enfantService.save(req.getId(), req.getIdPersonnel(), req.getMatricule(), req.getNom(), dateNaissance, req.getLieuNaissance(), req.getSexe());
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@PostMapping(value = "/trouverenfant")
	public ResponseEntity<EnfantDTO> findEnfant(@RequestBody com.nectux.mizan.hyban.common.dto.IdRequest req) {
		logger.info(">>> TROUVER ENFANT");
		return ResponseEntity.ok(enfantService.findEnfant(req.getId()));
	}

	@PostMapping(value = "/listerenfants")
	public ResponseEntity<EnfantDTO> findEnfants() {
		logger.info(">>> LISTE ENFANTS");
		return ResponseEntity.ok(enfantService.findEnfants());
	}

	@PostMapping(value = "/listerenfantsparpersonnel")
	public ResponseEntity<EnfantDTO> findEnfantsByPersonnel(@RequestBody com.nectux.mizan.hyban.common.dto.IdRequest req) {
		logger.info(">>> LISTE ENFANTS PAR PERSONNEL");
		return ResponseEntity.ok(enfantService.findEnfantsByPersonnel(req.getId()));
	}

	@PostMapping(value = "/supprimerenfant")
	public ResponseEntity<EnfantDTO> deleteEnfant(@RequestBody com.nectux.mizan.hyban.common.dto.IdRequest req) {
		logger.info(">>> SUPPRIMER ENFANT");
		return ResponseEntity.ok(enfantService.delete(req.getId()));
	}

	@PostMapping(value = "/paginerenfants")
	public ResponseEntity<EnfantDTO> getUserListJSON(@RequestBody PaginationRequest req, Principal principal) {
		logger.info(">>> LISTE ENFANTS AVEC PAGINATION");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		EnfantDTO enfantDTO = enfantService.loadEnfants(pageRequest);
		return ResponseEntity.ok(enfantDTO);
	}

}

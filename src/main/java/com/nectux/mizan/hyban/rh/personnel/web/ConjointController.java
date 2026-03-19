package com.nectux.mizan.hyban.rh.personnel.web;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.personnel.dto.ConjointDTO;
import com.nectux.mizan.hyban.rh.personnel.service.ConjointService;
import com.nectux.mizan.hyban.utils.DateManager;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.EnfantRequest;

@RestController
@RequestMapping("/api/personnel")
public class ConjointController {

	private static final Logger logger = LogManager.getLogger(ConjointController.class);

	@Autowired private ConjointService conjointService;
	// @Autowired private UtilisateurService utilisateurService;
	@Autowired private PeriodePaieService periodePaieService;
	 
	@RequestMapping("/conjoints")
	public ResponseEntity<Object> viewEnfant(Principal principal) throws IOException {
		logger.info(">>>>> Conjoints init");
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/enregistrerconjoint")
	public ResponseEntity<ConjointDTO> saveConjoint(@RequestBody EnfantRequest req) throws Exception {
		logger.info(">>> ENREGISTRER CONJOINT");
		Date dateNaissance = DateManager.stringToDate(req.getDateNaissanceString(), "dd/MM/yyyy");
		ConjointDTO res = conjointService.save(req.getId(), req.getIdPersonnel(), req.getMatricule(), req.getNom(), dateNaissance, req.getLieuNaissance(), req.getLieuNaissance(), req.getTelephone(), null, req.getSexe());
		return ResponseEntity.ok(res);
	}

	@PostMapping(value = "/trouverconjoint")
	public ResponseEntity<ConjointDTO> findEnfant(@RequestBody IdRequest req) {
		logger.info(">>> TROUVER CONJOINT");
		return ResponseEntity.ok(conjointService.findConjoint(req.getId()));
	}

	@PostMapping(value = "/listerconjoints")
	public ResponseEntity<ConjointDTO> findConjoints() {
		logger.info(">>> LISTE CONJOINTS");
		return ResponseEntity.ok(conjointService.findConjoints());
	}

	@PostMapping(value = "/listerconjointsparpersonnel")
	public ResponseEntity<ConjointDTO> findConjointsByPersonnel(@RequestBody IdRequest req) {
		logger.info(">>> LISTE CONJOINTS");
		return ResponseEntity.ok(conjointService.findConjointsByPersonnel(req.getId()));
	}

	@PostMapping(value = "/activerconjoint")
	public ResponseEntity<ConjointDTO> enableConjoint(@RequestBody IdRequest req) {
		logger.info(">>> ACTIVER CONJOINT");
		return ResponseEntity.ok(conjointService.enable(req.getId(), req.getId()));
	}

	@PostMapping(value = "/desactiverconjoint")
	public ResponseEntity<ConjointDTO> disableConjoint(@RequestBody IdRequest req) {
		logger.info(">>> DESACTIVER CONJOINT");
		return ResponseEntity.ok(conjointService.delete(req.getId()));
	}

	@PostMapping(value = "/supprimerconjoint")
	public ResponseEntity<ConjointDTO> deleteConjoint(@RequestBody IdRequest req) {
		logger.info(">>> SUPPRIMER CONJOINT");
		return ResponseEntity.ok(conjointService.delete(req.getId()));
	}

	@PostMapping(value = "/paginerconjoints")
	public ResponseEntity<ConjointDTO> getConjointListJSON(@RequestBody PaginationRequest req, Principal principal) {
		logger.info(">>> LISTE CONJOINTS AVEC PAGINATION");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		ConjointDTO enfantDTO = conjointService.loadConjoints(pageRequest);
		return ResponseEntity.ok(enfantDTO);
	}

}

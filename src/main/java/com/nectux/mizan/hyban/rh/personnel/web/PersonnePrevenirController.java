package com.nectux.mizan.hyban.rh.personnel.web;

import java.io.IOException;
import java.security.Principal;

import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.rh.personnel.service.PersonnePrevenirService;
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
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.rh.personnel.dto.PersonnePrevenirDTO;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.EnfantRequest;

@RestController
@RequestMapping("/api/personnel")
public class PersonnePrevenirController {

	private static final Logger logger = LogManager.getLogger(PersonnePrevenirController.class);

	@Autowired private PersonnePrevenirService personnePrevenirService;
	// @Autowired private UtilisateurService utilisateurService;
	@Autowired private PeriodePaieService periodePaieService;
	@RequestMapping("/personnesprevenir")
	public ResponseEntity<Object> viewEnfant(Principal principal) throws IOException {
		logger.info(">>>>> Personne a Prevenir init");
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/enregistrerpersonneprevenir")
	public ResponseEntity<PersonnePrevenirDTO> savePersonnePrevenir(@RequestBody EnfantRequest req) throws Exception {
		logger.info(">>> ENREGISTRER PERSONNE A PREVENIR");
		PersonnePrevenirDTO res = personnePrevenirService.save(req.getId(), req.getIdPersonnel(), null, req.getNom(), req.getTelephone(), null, req.getSexe());
		return ResponseEntity.ok(res);
	}

	@PostMapping(value = "/trouverpersonneprevenir")
	public ResponseEntity<PersonnePrevenirDTO> findEnfant(@RequestBody IdRequest req) {
		logger.info(">>> TROUVER PERSONNE A PREVENIR");
		return ResponseEntity.ok(personnePrevenirService.findPersonnePrevenir(req.getId()));
	}

	@PostMapping(value = "/listerpersonnesprevenir")
	public ResponseEntity<PersonnePrevenirDTO> findPersonnePrevenirs() {
		logger.info(">>> LISTE PERSONNES A PREVENIR");
		return ResponseEntity.ok(personnePrevenirService.findPersonnesPrevenir());
	}

	@PostMapping(value = "/listerpersonnesprevenirparpersonnel")
	public ResponseEntity<PersonnePrevenirDTO> findPersonnePrevenirsByPersonnel(@RequestBody IdRequest req) {
		logger.info(">>> LISTE PERSONNES A PREVENIR");
		return ResponseEntity.ok(personnePrevenirService.findPersonnesPrevenirByPersonnel(req.getId()));
	}

	@PostMapping(value = "/activerpersonneprevenir")
	public ResponseEntity<PersonnePrevenirDTO> enablePersonnePrevenir(@RequestBody IdRequest req) {
		logger.info(">>> ACTIVER PERSONNE A PREVENIR");
		return ResponseEntity.ok(personnePrevenirService.enable(req.getId(), req.getId()));
	}

	@PostMapping(value = "/desactiverpersonneprevenir")
	public ResponseEntity<PersonnePrevenirDTO> disablePersonnePrevenir(@RequestBody IdRequest req) {
		logger.info(">>> DESACTIVER PERSONNE A PREVENIR");
		return ResponseEntity.ok(personnePrevenirService.delete(req.getId()));
	}

	@PostMapping(value = "/supprimerpersonneprevenir")
	public ResponseEntity<PersonnePrevenirDTO> deletePersonnePrevenir(@RequestBody IdRequest req) {
		logger.info(">>> SUPPRIMER PERSONNE A PREVENIR");
		return ResponseEntity.ok(personnePrevenirService.delete(req.getId()));
	}

	@PostMapping(value = "/paginerpersonnesprevenir")
	public ResponseEntity<PersonnePrevenirDTO> getPersonnePrevenirListJSON(@RequestBody PaginationRequest req, Principal principal) {
		logger.info(">>> LISTE PERSONNES A PREVENIR AVEC PAGINATION");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		PersonnePrevenirDTO enfantDTO = personnePrevenirService.loadPersonnesPrevenir(pageRequest);
		return ResponseEntity.ok(enfantDTO);
	}

}

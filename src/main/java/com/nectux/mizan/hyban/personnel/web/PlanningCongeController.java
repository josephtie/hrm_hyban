package com.nectux.mizan.hyban.personnel.web;

import java.io.IOException;
import java.security.Principal;

import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.parametrages.service.PlanningCongeService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nectux.mizan.hyban.parametrages.dto.PlanningCongeDTO;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.PlanningConge;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.PlanningCongeRequest;
import com.nectux.mizan.hyban.utils.DateManager;

@RestController
@RequestMapping("/api/personnels")
public class PlanningCongeController {

	private static final Logger logger = LogManager.getLogger(PlanningCongeController.class);

	// @Autowired private UtilisateurService utilisateurService;
	@Autowired private PlanningCongeService planningCongeService;
	@Autowired private PeriodePaieService periodePaieService;
	@Autowired private SocieteService societeService;
	// @Autowired private UtilisateurRoleService utilisateurRoleService;

	@GetMapping("/planningconges/init")
	public ResponseEntity<Object> viewAccountType(Principal principal) throws IOException {
		logger.info(">>>>> Planning Conges init");
		return ResponseEntity.ok().build();
	}

	@PostMapping("/listplanningcongejson")
	public ResponseEntity<PlanningCongeDTO> getPlanningCongeListJSON(@RequestBody PaginationRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "dateDepart");
		PlanningCongeDTO planningCongeDTO = (req.getSearch() == null) ? planningCongeService.loadPlanningConge(pageRequest) : planningCongeService.loadPlanningConge(pageRequest, req.getSearch());
		return ResponseEntity.ok(planningCongeDTO);
	}

	@PostMapping("/choisirplanningconge")
	public ResponseEntity<PlanningConge> selectPlanningConge(@RequestBody IdRequest req) {
		return ResponseEntity.ok(planningCongeService.findPlanningConge(req.getId()));
	}

	@PostMapping("/enregistrerplanningconge")
	public ResponseEntity<PlanningCongeDTO> savePlanningConge(@RequestBody PlanningCongeRequest dto) throws Exception {
		return ResponseEntity.ok(planningCongeService.save(dto.getId(), DateManager.stringToDate(dto.getDateDepartString(), "dd/MM/yyyy")));
	}

}

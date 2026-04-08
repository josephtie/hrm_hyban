package com.nectux.mizan.hyban.rh.absences.web;

import java.io.IOException;
import java.security.Principal;

// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.utils.DateManager;
import com.nectux.mizan.hyban.paie.entity.TempEffectif;
import com.nectux.mizan.hyban.paie.repository.TempEffectifRepository;
import com.nectux.mizan.hyban.paie.service.TempEffectifService;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.personnel.repository.PersonnelRepository;
import com.nectux.mizan.hyban.rh.absences.dto.AbsencesPersonnelDTO;
import com.nectux.mizan.hyban.rh.absences.service.AbsencesPersonnelService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nectux.mizan.hyban.common.dto.AbsencesPersonnelRequest;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;

@RestController
@RequestMapping("/api/absence")
public class AbsencesPersonnelController {
	
	private static final Logger logger = LogManager.getLogger(AbsencesPersonnelController.class);
	
	@Autowired private AbsencesPersonnelService absencesPersonnelService;
	// @Autowired private UtilisateurService utilisateurService;
	@Autowired private TempEffectifService  tempEffectifService;
	@Autowired private TempEffectifRepository  tempEffectifRepository;
	@Autowired private PeriodePaieService periodePaieService;
	@Autowired private PersonnelRepository personnelRepository;
	
	
	@RequestMapping("/absencepersonnel")
	public String viewAbsencePersonnel(org.springframework.ui.ModelMap modelMap, Principal principal) throws IOException {
		logger.info(">>>>> Sanction Personnel");

		// modelMap.addAttribute("user", utilisateurService.findByUsername(principal.getName()));
		// Utilisateur utilisateur=utilisateurService.findByUsername(principal.getName());
		// System.out.println("utilisateur    " +utilisateur.toString());

		// modelMap.addAttribute("profil", utilisateur.getUtilisateurRoles().stream()
		//		.map(utilisateurRole -> utilisateurRole.getRole().getName().name())
		//		.findFirst().orElse(""));
		modelMap.addAttribute("icon", "iconfa-user");
		modelMap.addAttribute("littleTitle", "Absence");
		modelMap.addAttribute("bigTitle", "Personnel");
		
		return "absencepersonnel";
	}
	
	@PostMapping(value = "/enregistrerabsencepersonnel")
	public ResponseEntity<AbsencesPersonnelDTO> saveAbsencesPersonnel(@RequestBody AbsencesPersonnelRequest req) throws Exception {
		logger.info(">>> ENREGISTRER SANCTION PERSONNEL");
		AbsencesPersonnelDTO absencesPersonnelDTO = absencesPersonnelService.save(req.getId(), req.getIdPersonnel(), req.getIdAbsence(), req.getDateDebut(), req.getDateFin(), req.getHeursabsence(), req.getJoursabsence(), req.getObservation(), req.getStatut(), req.getSanctionsalaire());
//		PeriodePaie myperiodePaie=periodePaieService.findPeriodeactive();
//		if(absencesPersonnelDTO.getRow().getSanctionsalaire()==4){
//			TempEffectif tpeffop= new TempEffectif();
//			tpeffop=tempEffectifRepository.findByPersonnelAndPeriodePaie(absencesPersonnelDTO.getRow().getPersonnel(), myperiodePaie);
//			if(tpeffop==null){
//				// create new
//				tpeffop.setHeurspresence(173.33-req.getHeursabsence());
//				tpeffop.setJourspresence(30-req.getJoursabsence());
//				tpeffop.setPersonnel(absencesPersonnelDTO.getRow().getPersonnel());
//				tpeffop.setPeriodePaie(myperiodePaie);
//				tpeffop=tempEffectifService.save(tpeffop);
//			}else{
//				tpeffop.setDatedesaisie(DateManager.stringToDate(req.getDateDebut(), "dd/MM/yyyy"));
//				tpeffop.setHeurspresence(tpeffop.getHeurspresence()-req.getHeursabsence());
//				tpeffop.setJourspresence(tpeffop.getJourspresence()-req.getJoursabsence());
//				tpeffop.setPersonnel(absencesPersonnelDTO.getRow().getPersonnel());
//				tpeffop.setPeriodePaie(myperiodePaie);
//				tpeffop=tempEffectifService.save(tpeffop);
//			}
//		}
		return ResponseEntity.ok(absencesPersonnelDTO);
	}

	@PostMapping(value = "/trouverabsencepersonnel")
	public ResponseEntity<AbsencesPersonnelDTO> findAbsencesPersonnel(@RequestBody IdRequest req) {
		logger.info(">>> TROUVER SANCTION PERSONNEL");
		return ResponseEntity.ok(absencesPersonnelService.findAbsencesPersonnel(req.getId()));
	}

	@PostMapping(value = "/listerabsencepersonnels")
	public ResponseEntity<AbsencesPersonnelDTO> findAbsencesPersonnels() {
		logger.info(">>> LISTE SANCTION PERSONNELS");
		return ResponseEntity.ok(absencesPersonnelService.findAbsencesPersonnels());
	}

	@PostMapping(value = "/listerabsencepersonnelsparpersonnel")
	public ResponseEntity<AbsencesPersonnelDTO> findAbsencePersonnelsByPersonnel(@RequestBody IdRequest req) {
		logger.info(">>> LISTE SANCTION PERSONNEL PAR PERSONNEL");
		return ResponseEntity.ok(absencesPersonnelService.findAbsencesPersonnelsByPersonnel(req.getId()));
	}

	@PostMapping(value = "/listerabsencepersonnelsparabsence")
	public ResponseEntity<AbsencesPersonnelDTO> findAbsencePersonnelsByAbsence(@RequestBody IdRequest req) {
		logger.info(">>> LISTE SANCTION PERSONNELS PAR PERSONNEL");
		return ResponseEntity.ok(absencesPersonnelService.findAbsencesPersonnelsByAbsence(req.getId()));
	}

	@PostMapping(value = "/supprimerabsencepersonnel")
	public ResponseEntity<AbsencesPersonnelDTO> deleteAbsencesPersonnel(@RequestBody IdRequest req) {
		logger.info(">>> SUPPRIMER SANCTION PERSONNEL");
		return ResponseEntity.ok(absencesPersonnelService.delete(req.getId()));
	}

	@PostMapping(value = "/paginerabsencePersonnels")
	public ResponseEntity<AbsencesPersonnelDTO> getUserListabsenceJSON(@RequestBody PaginationRequest req, Principal principal) {
		logger.info(">>> LISTE SANCTION PERSONNELS AVEC PAGINATION");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		AbsencesPersonnelDTO absencesPersonnelDTO = (req.getSearch() == null || req.getSearch().isEmpty()) ? absencesPersonnelService.loadAbsencesPersonnels(pageRequest) : absencesPersonnelService.loadAbsencesPersonnels(pageRequest, req.getSearch(), req.getSearch(), req.getSearch(), req.getSearch());
		return ResponseEntity.ok(absencesPersonnelDTO);
	}

}

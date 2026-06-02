package com.nectux.mizan.hyban.personnel.web;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
import com.nectux.mizan.hyban.paie.entity.BulletinPaie;
import com.nectux.mizan.hyban.paie.repository.BulletinPaieRepository;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.personnel.dto.OrganisationServiceDto;
import com.nectux.mizan.hyban.personnel.dto.OrganisationServiceResponse;
import com.nectux.mizan.hyban.personnel.dto.ServiceDTO;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.personnel.entity.Personnel;
import com.nectux.mizan.hyban.personnel.entity.Service;
import com.nectux.mizan.hyban.personnel.repository.ContratPersonnelRepository;
import com.nectux.mizan.hyban.personnel.repository.PersonnelRepository;

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

import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.personnel.service.ServiceService;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.ServiceRequest;

@RestController
@RequestMapping("/api/personnels")
public class ServiceController {

	private static final Logger logger = LogManager.getLogger(ServiceController.class);
	@Autowired private ServiceService serviceService;
	@Autowired private PersonnelRepository personnelRepository;
	@Autowired private ContratPersonnelRepository contratPersonnelRepository;
	@Autowired private BulletinPaieRepository bulletinPaieRepository;
	// @Autowired private UtilisateurService userService;
	@Autowired private PeriodePaieService periodePaieService;
	@Autowired private SocieteService societeService;
	// @Autowired private UtilisateurRoleService utilisateurRoleService;

	@GetMapping("/services/init")
	public ResponseEntity<Object> viewService(Principal principal) throws IOException {
		logger.info(">>>>> Services init");
		return ResponseEntity.ok().build();
	}

	@PostMapping("/listservicejson")
	public ResponseEntity<ServiceDTO> getServiceListJSON(@RequestBody PaginationRequest req, Principal principal) {
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / limit, limit, Direction.DESC, "libelle");
		ServiceDTO serviceDTO = (req.getSearch() == null) ? serviceService.loadService(pageRequest) : serviceService.loadService(pageRequest, req.getSearch());
		return ResponseEntity.ok(serviceDTO);
	}

	@PostMapping("/listdepartementbydirection")
	public ResponseEntity<List<Service>> getDepartementByDirection(@RequestBody IdRequest req, Principal principal) {
		return ResponseEntity.ok(serviceService.findByServiceParent(req.getId()));
	}

	@PostMapping("/listservicepartype")
	public ResponseEntity<List<Service>> listeServiceParType(@RequestBody IdRequest req) {
		return ResponseEntity.ok(serviceService.findByTypeServiceId(req.getId()));
	}

	@PostMapping("/enregisterservice")
	public ResponseEntity<ServiceDTO> saveService(@RequestBody ServiceRequest dto) {
		ServiceDTO res = serviceService.save(dto.getId(), dto.getLibelle(), dto.getIdDepartement(), dto.getIdDirection(), dto.getIdTypeService());
		return ResponseEntity.ok(res);
	}

	@PostMapping("/supprimerservice")
	public ResponseEntity<ServiceDTO> deleteService(@RequestBody IdRequest req) {
		ServiceDTO serviceDTO = new ServiceDTO();
		serviceDTO.setResult(serviceService.delete(req.getId()));
		return ResponseEntity.ok(serviceDTO);
	}

	// Endpoint pour OrganisationServicesView
	@PostMapping("/organisation/list")
	public ResponseEntity<OrganisationServiceResponse> getOrganisationServices(@RequestBody PaginationRequest req, Principal principal) {
		try {
			// Récupérer tous les services avec pagination
			Integer offset = req.getOffset() == null ? 0 : req.getOffset();
			Integer limit = req.getLimit() == null ? 10 : req.getLimit();
			PageRequest pageRequest = PageRequest.of(offset, limit, Direction.DESC, "libelle");
			
			ServiceDTO serviceDTO = (req.getSearch() == null) ? 
				serviceService.loadService(pageRequest) : 
				serviceService.loadService(pageRequest, req.getSearch());
			
			// Récupérer tous les services pour construire les relations hiérarchiques
			List<Service> allServices = serviceService.findServices();
			
			// Créer une map pour un accès rapide par ID
			Map<Long, Service> serviceMap = allServices.stream()
				.collect(java.util.stream.Collectors.toMap(Service::getId, service -> service));
			
			// Transformer en OrganisationServiceDto pour la vue
			List<OrganisationServiceDto> organisationServices = serviceDTO.getRows().stream()
				.map(service -> {
					Service currentService = (Service) service;
					OrganisationServiceDto dto = new OrganisationServiceDto();
					dto.setId(currentService.getId());
					dto.setLibelle(currentService.getLibelle());
					dto.setActive(true);
					dto.setCreatedAt(currentService.getCreatedAt() != null ? 
						currentService.getCreatedAt().toString() : "2024-01-01");
					
					// Déterminer le type en fonction de la hiérarchie
					Service parent = currentService.getServiceParent();
					if (parent == null) {
						// Service racine = Direction
						dto.setType("DIRECTION");
						dto.setIdDirection(null);
						dto.setIdDepartement(null);
					} else {
						Service grandParent = parent.getServiceParent();
						if (grandParent == null) {
							// Niveau 2 = Département
							dto.setType("DEPARTEMENT");
							dto.setIdDirection(parent.getId());
							dto.setIdDepartement(null);
						} else {
							// Niveau 3 = Service
							dto.setType("SERVICE");
							dto.setIdDirection(grandParent.getId());
							dto.setIdDepartement(parent.getId());
						}
					}
					
					return dto;
				})
				.collect(java.util.stream.Collectors.toList());
			
			OrganisationServiceResponse response = new OrganisationServiceResponse(
				organisationServices, 
				(int) serviceDTO.getTotal(), 
				"success"
			);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des services d'organisation", e);
			OrganisationServiceResponse response = new OrganisationServiceResponse();
			response.setRows(java.util.Collections.emptyList());
			response.setTotal(0);
			response.setResult("error");
			response.setMessage(e.getMessage());
			return ResponseEntity.status(500).body(response);
		}
	}

	@GetMapping("/effectifs-par-direction")
	public ResponseEntity<List<Map<String, Object>>> getEffectifsParDirection() {
		try {
			List<Service> allServices = serviceService.findServices();
			List<String> directionsOfficielles = java.util.Arrays.asList(
				"RESSOURCES HUMAINES",
				"PROJET",
				"MECANIQUE",
				"LOGISTIQUE",
				"LABO",
				"TOPOGRAPHIQUE"
			);
			Map<Long, Map<String, Object>> effectifs = new java.util.LinkedHashMap<>();

			for (Service service : allServices) {
				if (isDirection(service) && isDirectionOfficielle(service, directionsOfficielles)) {
					Map<String, Object> row = new java.util.HashMap<>();
					row.put("id", service.getId());
					row.put("libelle", formatDirectionLibelle(service.getLibelle()));
					row.put("effectif", 0);
					effectifs.put(service.getId(), row);
				}
			}

			List<Personnel> personnels = personnelRepository.findByRetraitEffectFalse();
			for (Personnel personnel : personnels) {
				if (!Boolean.TRUE.equals(personnel.getStatut())) {
					continue;
				}

				Service direction = getDirection(personnel.getService());
				if (direction == null) {
					continue;
				}
				if (!isDirectionOfficielle(direction, directionsOfficielles)) {
					continue;
				}

				Map<String, Object> row = effectifs.get(direction.getId());
				if (row == null) {
					continue;
				}
				row.put("effectif", ((Number) row.get("effectif")).intValue() + 1);
			}

			return ResponseEntity.ok(new java.util.ArrayList<>(effectifs.values()));
		} catch (Exception e) {
			logger.error("Erreur lors du calcul des effectifs par direction", e);
			return ResponseEntity.status(500).body(java.util.Collections.emptyList());
		}
	}

	@GetMapping("/dashboard/activites-recentes")
	public ResponseEntity<List<Map<String, Object>>> getActivitesRecentes() {
		try {
			List<Map<String, Object>> activites = new java.util.ArrayList<>();
			PageRequest pageRequest = PageRequest.of(0, 5, Direction.DESC, "createdAt");

			for (Personnel personnel : personnelRepository.findAll(pageRequest)) {
				if (personnel.getCreatedAt() == null) {
					continue;
				}
				Map<String, Object> activity = new java.util.HashMap<>();
				activity.put("id", "personnel-" + personnel.getId());
				activity.put("type", "create");
				activity.put("title", "Nouvel employé ajouté: " + getNomComplet(personnel));
				activity.put("createdAt", personnel.getCreatedAt());
				activites.add(activity);
			}

			for (ContratPersonnel contrat : contratPersonnelRepository.findAll(pageRequest)) {
				if (contrat.getCreatedAt() == null) {
					continue;
				}
				Map<String, Object> activity = new java.util.HashMap<>();
				activity.put("id", "contrat-" + contrat.getId());
				activity.put("type", "edit");
				activity.put("title", "Contrat enregistré: " + getNomComplet(contrat.getPersonnel()));
				activity.put("createdAt", contrat.getCreatedAt());
				activites.add(activity);
			}

			for (BulletinPaie bulletin : bulletinPaieRepository.findAll(pageRequest)) {
				if (bulletin.getCreatedAt() == null) {
					continue;
				}
				Map<String, Object> activity = new java.util.HashMap<>();
				activity.put("id", "bulletin-" + bulletin.getId());
				activity.put("type", "payroll");
				activity.put("title", "Bulletin généré: " + getNomCompletFromBulletin(bulletin));
				activity.put("createdAt", bulletin.getCreatedAt());
				activites.add(activity);
			}

			activites.sort((a, b) -> ((java.time.LocalDateTime) b.get("createdAt")).compareTo((java.time.LocalDateTime) a.get("createdAt")));
			return ResponseEntity.ok(activites.stream().limit(5).collect(java.util.stream.Collectors.toList()));
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération des activités récentes", e);
			return ResponseEntity.status(500).body(java.util.Collections.emptyList());
		}
	}

	private Service getDirection(Service service) {
		Service current = service;
		while (current != null && !isDirection(current)) {
			current = current.getServiceParent();
		}
		return current;
	}

	private boolean isDirection(Service service) {
		return service != null
			&& service.getTypeService() != null
			&& service.getTypeService().getLibelle() != null
			&& "DIRECTION".equalsIgnoreCase(service.getTypeService().getLibelle().trim());
	}

	private boolean isDirectionOfficielle(Service service, List<String> directionsOfficielles) {
		return directionsOfficielles.contains(normalizeDirectionLibelle(service.getLibelle()));
	}

	private String formatDirectionLibelle(String libelle) {
		if (libelle == null) {
			return "Non renseigné";
		}
		String formatted = libelle
			.replaceFirst("(?i)^\\s*DIRECTION\\s+", "")
			.replace("-", "")
			.trim()
			.replaceAll("\\s+", " ");
		return formatted.isEmpty() ? libelle : formatted;
	}

	private String normalizeDirectionLibelle(String libelle) {
		if (libelle == null) {
			return "";
		}
		return java.text.Normalizer.normalize(formatDirectionLibelle(libelle), java.text.Normalizer.Form.NFD)
			.replaceAll("\\p{M}", "")
			.trim()
			.replaceAll("\\s+", " ")
			.toUpperCase();
	}

	private String getNomComplet(Personnel personnel) {
		if (personnel == null) {
			return "Non renseigné";
		}
		String nom = personnel.getNom() == null ? "" : personnel.getNom();
		String prenom = personnel.getPrenom() == null ? "" : personnel.getPrenom();
		String nomComplet = (nom + " " + prenom).trim();
		return nomComplet.isEmpty() ? "Non renseigné" : nomComplet;
	}

	private String getNomCompletFromBulletin(BulletinPaie bulletin) {
		if (bulletin == null || bulletin.getContratPersonnel() == null) {
			return "Non renseigné";
		}
		return getNomComplet(bulletin.getContratPersonnel().getPersonnel());
	}
}

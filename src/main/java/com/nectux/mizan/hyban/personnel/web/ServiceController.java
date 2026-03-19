package com.nectux.mizan.hyban.personnel.web;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.PeriodePaieService;
import com.nectux.mizan.hyban.personnel.dto.OrganisationServiceDto;
import com.nectux.mizan.hyban.personnel.dto.OrganisationServiceResponse;
import com.nectux.mizan.hyban.personnel.dto.ServiceDTO;
import com.nectux.mizan.hyban.personnel.entity.Service;

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
}

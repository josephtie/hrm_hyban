package com.nectux.mizan.hyban.parametrages.web;

import java.io.IOException;
import java.security.Principal;

import com.nectux.mizan.hyban.parametrages.dto.TypeDocumentDTO;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.TypeDocumentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.common.dto.TypeDocumentRequest;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;

@RestController
@RequestMapping("/api/parametrages/doc")
public class TypeDocumentController {

	private static final Logger logger = LogManager.getLogger(TypeDocumentController.class);

	@Autowired private TypeDocumentService typeDocumentService;
	// @Autowired private UtilisateurService utilisateurService;
	 
	@RequestMapping("/typedocument")
	public String viewAccountType(org.springframework.ui.ModelMap modelMap, Principal principal) throws IOException {
		logger.info(">>>>> Types Documents");

		// modelMap.addAttribute("user", utilisateurService.findByUsername(principal.getName()));
		// Utilisateur utilisateur=utilisateurService.findByUsername(principal.getName());
		// System.out.println("utilisateur    " +utilisateur.toString());
		// modelMap.addAttribute("profil", utilisateur.getUtilisateurRoles().stream()
		//		.map(utilisateurRole -> utilisateurRole.getRole().getName().name())
		//		.findFirst().orElse(""));
		modelMap.addAttribute("icon", "iconfa-wrench");
		modelMap.addAttribute("littleTitle", "Parametrages");
		modelMap.addAttribute("bigTitle", "Types Documents");
		
		return "typedocument";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/enregistrertypedocument")
	public TypeDocumentDTO saveTypeDocument(@RequestBody TypeDocumentRequest req) {
		logger.info(">>> ENREGISTRER TYPE DOCUMENT");
		return typeDocumentService.save(req.getId(), req.getLibelle());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/supprimertypedocument")
	public TypeDocumentDTO deleteTypeDocument(@RequestBody IdRequest req) {
		logger.info(">>> SUPPRIMER TYPE DOCUMENT");
		return typeDocumentService.delete(req.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/trouvertypedocument")
	public TypeDocumentDTO findTypeDocument(@RequestBody IdRequest req) {
		logger.info(">>> TROUVER TYPE DOCUMENT");
		return typeDocumentService.findTypeDocument(req.getId());
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listertypesdocuments")
	public TypeDocumentDTO findTypesDocuments() {
		logger.info(">>> LISTE TYPES DOCUMENTS");
		return typeDocumentService.findTypesDocumments();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/paginertypesdocuments")
	public TypeDocumentDTO getUserListJSON(@RequestBody PaginationRequest req, Principal principal) {
		logger.info(">>> LISTE TYPES DOCUMENTS AVEC PAGINATION");
		Integer offset = req.getOffset() == null ? 0 : req.getOffset();
		Integer limit = req.getLimit() == null ? 10 : req.getLimit();
		PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
		TypeDocumentDTO typeDocumentDTO = (req.getSearch() == null || req.getSearch().isEmpty()) ? typeDocumentService.loadTypesDocuments(pageRequest) : typeDocumentService.loadTypesDocuments(pageRequest, req.getSearch());
		return typeDocumentDTO;
	}

}

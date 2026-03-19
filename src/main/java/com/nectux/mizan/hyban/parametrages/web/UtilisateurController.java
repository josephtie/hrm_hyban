package com.nectux.mizan.hyban.parametrages.web;

import java.io.IOException;
import java.security.Principal;

import com.nectux.mizan.hyban.parametrages.dto.UserDto;
// import com.nectux.mizan.hyban.parametrages.dto.UtilisateurRoleDTO;
// import com.nectux.mizan.hyban.parametrages.dto.UtilisateurDTO;
import com.nectux.mizan.hyban.parametrages.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.UtilisateurRequest;
import com.nectux.mizan.hyban.common.dto.EmailRequest;
import com.nectux.mizan.hyban.common.dto.ChangePasswordRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.parametrages.dto.UserWithRolesDto;

@Controller
@RequestMapping("/api/parametrages/user")
public class UtilisateurController {
	
	private static final Logger logger = LogManager.getLogger(UtilisateurController.class);
	
	// @Autowired private UtilisateurService utilisateurService;
	// @Autowired private UtilisateurRoleService utilisateurRoleService;
	@Autowired private SocieteService societeService;
	@RequestMapping("/utilisateur")
    public String viewAccountType(org.springframework.ui.ModelMap modelMap, Principal principal) throws IOException {
		logger.info(">>>>> Utilisateurs");
		
		modelMap.addAttribute("activeSetting", "active");
		modelMap.addAttribute("blockSetting", "block");
		modelMap.addAttribute("activeUser", "active");
		// modelMap.addAttribute("user", utilisateurService.findByUsername(principal.getName()));
		// Utilisateur utilisateur=utilisateurService.findByUsername(principal.getName());
		// System.out.println("utilisateur    " +utilisateur.toString());
		// modelMap.addAttribute("profil", utilisateur.getUtilisateurRoles().stream()
		// 			.map(utilisateurRole -> utilisateurRole.getRole().getName().name())
		// 			.findFirst().orElse(""));
		modelMap.addAttribute("icon", "fa fa-users");
		modelMap.addAttribute("littleTitle", "Parametrages");
		modelMap.addAttribute("bigTitle", "Utilisateurs");
		  Societe mysociete=null;
		  java.util.List<Societe> malist=societeService.findtsmois();
		  if(malist.size()>0)
			{	mysociete=malist.get(0);			
			modelMap.addAttribute("urllogo",mysociete.getUrlLogo());}
		return "utilisateurs";
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/listutilisateurjson")
		public Object getUserListJSON(@RequestBody PaginationRequest req, Principal principal) {
			Integer offset = req.getOffset() == null ? 0 : req.getOffset();
			Integer limit = req.getLimit() == null ? 10 : req.getLimit();
			PageRequest pageRequest = PageRequest.of(offset / 10, limit, Sort.Direction.DESC, "id");
			// UtilisateurRoleDTO utilisateurRoleDTO = (req.getSearch() == null || req.getSearch().isEmpty()) ? utilisateurService.loadUtilisateur(pageRequest) : utilisateurService.loadUtilisateur(pageRequest, req.getSearch());
			// return utilisateurRoleDTO;
			return null;
		}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/enregistrerutilisateur")
		public Object saveUser(@RequestBody UtilisateurRequest req) {
		// UserDto utilisateur=new UserDto();
		// utilisateur.setPassword("1234567L");
		// utilisateur.setEmail(req.getEmail());
		// utilisateur.setNomComplet(req.getNomComplet());
		// utilisateur.setUsername(req.getDateNaissance());
		// if(req.getIdRole() == 1){utilisateur.setRoleName(RoleName.ADMIN);}
		// if(req.getIdRole() == 2){utilisateur.setRoleName(RoleName.DAF);}
		// if(req.getIdRole() == 3){utilisateur.setRoleName(RoleName.RH);}
		// if(req.getIdRole() == 4){utilisateur.setRoleName(RoleName.PTGE);}
		// return utilisateurService.save(req.getId(), req.getIdRole(), req.getNomComplet(), req.getDateNaissance(), req.getTelephone(), req.getAdresse(), req.getEmail(), "123456M");
		return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/motdepasseoublie")
		public Object forgotPassword(@RequestBody EmailRequest req) {
			// return utilisateurService.forgetPassword(req.getEmail());
			return null;
		}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/choisirutilisateur")
	public UtilisateurRole selectUserRole(@RequestBody IdRequest req) {
		// UtilisateurRole utilisateurRole = utilisateurRoleService.findUtilisateurRole(req.getId());
		// return utilisateurRole;
		return null;
	}
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/cherchutilisateur")
	public Utilisateur cherchutilisateur(@RequestBody IdRequest req) {
		// Utilisateur utilisateurRole = utilisateurService.findUtilisateur(req.getId());
		// return utilisateurRole;
		return null;
	}
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/chagermotdepasse")
		public Object changePassword(@RequestBody ChangePasswordRequest req, Principal principal) {
			// Utilisateur utilisateur = utilisateurService.findUtilisateur(req.getId());
			// return utilisateurService.changePassword(Long.valueOf(utilisateur.getId()), req.getAncien(), req.getNouveau());
			return null;
		}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/supprimerutilisateur")
		public Object deleteUser(@RequestBody IdRequest req) {
			// UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
			// utilisateurDTO.setResult(utilisateurService.delete(req.getId()));
			// return utilisateurDTO;
			return null;
		}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/changerstatut")
		public Object changeStatus(@RequestBody IdRequest req) {
			// UtilisateurDTO utilisateurDTO = utilisateurService.changeStstus(req.getId());
			// return utilisateurDTO;
			return null;
		}

}

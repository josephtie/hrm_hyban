package com.nectux.mizan.hyban.parametrages.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Iterator;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import com.nectux.mizan.hyban.common.dto.IdRequest;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.SocieteRequest;
import com.nectux.mizan.hyban.parametrages.entity.Societe;
// import com.nectux.mizan.hyban.parametrages.entity.Utilisateur;
import com.nectux.mizan.hyban.parametrages.service.BanqueService;
import com.nectux.mizan.hyban.parametrages.service.MoisService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurRoleService;
import com.nectux.mizan.hyban.parametrages.dto.MoisDTO;
import com.nectux.mizan.hyban.parametrages.dto.SocieteDTO;
import com.nectux.mizan.hyban.parametrages.entity.Mois;
import com.nectux.mizan.hyban.parametrages.repository.SocieteRepository;
import com.nectux.mizan.hyban.parametrages.service.SocieteService;
// import com.nectux.mizan.hyban.parametrages.service.UtilisateurService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.nectux.mizan.hyban.common.dto.PaginationRequest;
import com.nectux.mizan.hyban.common.dto.SocieteRequest;
import com.nectux.mizan.hyban.common.dto.IdRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Controller
@RequestMapping("/api/parametrages/societe")
public class SocieteController {

	
private static final Logger logger = LogManager.getLogger(SocieteController.class);
	
	// @Autowired private UtilisateurService userService;
	@Autowired private SocieteService societeService;
	@Autowired private SocieteRepository societeRepository;
	@Autowired private MoisService moisService;
	 @Autowired private BanqueService banqueService;
	 // @Autowired private UtilisateurRoleService utilisateurRoleService;
	 
	 public SocieteDTO excelAttribdto =null;
		public static int maxFileSize 		= 1024 * 1024 * 2; // 40MB 
		public static String imageType = new String(".jpg");
	@RequestMapping("/societe")
    public String viewAccountType(ModelMap modelMap, Principal principal) throws IOException {
		logger.info(">>>>> Utilisateurs");
		
		modelMap.addAttribute("activeSetting", "active");
		modelMap.addAttribute("blockSetting", "block");
		modelMap.addAttribute("activeSociety", "active");
		// modelMap.addAttribute("user", userService.findByUsername(principal.getName()));
		// Utilisateur utilisateur=userService.findByUsername(principal.getName());
		// System.out.println("utilisateur    " +utilisateur.toString());
		// modelMap.addAttribute("profil", utilisateur.getUtilisateurRoles().stream()
		// .map(utilisateurRole -> utilisateurRole.getRole().getName().name()) 
		// .findFirst().orElse(""));
		modelMap.addAttribute("icon", "fa fa-html5");
		modelMap.addAttribute("littleTitle", "Parametrages");
		modelMap.addAttribute("bigTitle", "Societe");
		  modelMap.addAttribute("listeBanques", banqueService.getBanques());
	
		  Societe mysociete=null;
		  java.util.List<Societe> malist=societeService.findtsmois();
		  if(malist.size()>0)
			{	mysociete=malist.get(0);			
			modelMap.addAttribute("urllogo",mysociete.getUrlLogo());}
		return "societes";
	}
	
	@RequestMapping("/societe1")
    public String viewAccountTyped(ModelMap modelMap, Principal principal) throws IOException {
		logger.info(">>>>> Utilisateurs");
		
		modelMap.addAttribute("activeSetting", "active");
		modelMap.addAttribute("blockSetting", "block");
		modelMap.addAttribute("activeSociety", "active");
		// modelMap.addAttribute("user", userService.findByEmail(principal.getName()));
		modelMap.addAttribute("icon", "iconfa-wrench");
		modelMap.addAttribute("littleTitle", "Parametrages");
		modelMap.addAttribute("bigTitle", "RH PAIE - CGECI");
		  modelMap.addAttribute("listeBanques", banqueService.getBanques());
		  Societe mysociete=null;
		  java.util.List<Societe> malist=societeService.findtsmois();
			mysociete=malist.get(0);			
			modelMap.addAttribute("urllogo",mysociete.getUrlLogo());
		    System.out.println("PERIODE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + mysociete.getUrlLogo());
		return "societesp";
	}
	
	//afficher toutes les periodes
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/societejson")
	public @ResponseBody SocieteDTO getsocieteju(@RequestBody PaginationRequest req) {
        Integer offset = req.getOffset() == null ? 0 : req.getOffset();
        Integer limit = req.getLimit() == null ? 10 : req.getLimit();
        String search = req.getSearch();
        PageRequest pageRequest = PageRequest.of(offset / 10, limit, Direction.DESC, "id");
        SocieteDTO societDTO = (search == null || search.isEmpty()) ? societeService.loadSociete(pageRequest) : societeService.loadSociete(pageRequest, search);
        return societDTO;
    }
	//*********/
	//Afficher tous les mois
	//*******/
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/societlistmois", method = RequestMethod.GET)
	public @ResponseBody MoisDTO listMoisyy(Principal principal) {
		
		//Utilisateur currentUser = userService.findByEmail(principal.getName());
		MoisDTO moisDTO = new MoisDTO();
		java.util.List<Mois> malist=moisService.findtsmois();
		moisDTO.setRows(malist);
		moisDTO.setTotal(malist.size());
		return moisDTO;
	}
	
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/enregistrersociete")
	public @ResponseBody SocieteDTO saveSociete(@RequestBody SocieteRequest req) {
        return societeService.save(req.getIdp(), req.getRaisonsoc(), req.getSigle(), req.getActivitepp(), req.getAdress(), req.getFormjuri(), req.getTelephone(), req.getBp(), req.getCommune(), req.getQuartier(), req.getRue(), req.getLot(), req.getSectpartiell(), req.getCentreImpot(), req.getCodeEts(), req.getCodeActivite(), req.getCodeEmployeur(), req.getCpteContrib(), req.getNomcomptable(), req.getTelcomptable(), req.getAdrcomptable(), req.getTxprest(), req.getTxacctr(), req.getTxretraite(), req.getTxgratif(), req.getGratification());
    }

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/choisirsociete")
	public @ResponseBody Societe selectSociete(@RequestBody IdRequest req) {
        Societe utilisateurRole = societeRepository.findById(req.getId()).orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + req.getId()));
        return utilisateurRole;
    }

	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping(value = "/deletesociete")
	public @ResponseBody SocieteDTO deleteSociete(@RequestBody IdRequest req, Principal principal) {
        SocieteDTO userDTO = new SocieteDTO();
        userDTO.setResult(societeService.delete(req.getId()));
        return userDTO;
    }

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/saveattrib", method = RequestMethod.POST)
	@ResponseBody
	public  SocieteDTO uploadFile(MultipartHttpServletRequest uploadfile, HttpServletRequest request)  {
	
		//User currentUser1 = userService.findUser(idUser);+ File.separator + "soxieteLog"
		try {
			Societe mysociete=null;
		String uploadPath =  request.getSession().getServletContext().getClass().getResource("")+ File.separator + "\\static\\logo\\";

		System.out.println(">>> CHEMIN UPLOAD >>>" + uploadPath);
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
	
		Iterator<String> itr =  uploadfile.getFileNames();
		 
	     MultipartFile mpf = uploadfile.getFile(itr.next());
	     //System.out.println(mpf.getOriginalFilename() +" uploaded!");
	     
	     String filename = mpf.getOriginalFilename();
	     String directory = uploadPath;
	     String filepath = Paths.get(directory, filename).toString();
	 	java.util.List<Societe> malist=societeService.findtsmois();
		mysociete=malist.get(0);
		mysociete.setUrlLogo("/static/logo/"+filename);
		societeService.save(mysociete);
	     // Save the file locally
	     BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
	     
			stream.write(mpf.getBytes());
			stream.close();
		
	     
	     System.out.println("PERIODE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + filename);
	   
	 
			} catch (IOException e) {
				e.printStackTrace();
			}
		return excelAttribdto;
	    
		
	/*	Contactattrib	Event=new Contactattrib();
		Event.setDatDeb(datDeb);Event.setDatFin(datFin);
		Event.setUser(currentUser);
		Event.setOpcommercial(opcommercialeRepository.findOne(Idopcommercial));*/
		
		
	}
	
	/*
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/saveuserc", method = RequestMethod.POST)
	public @ResponseBody PeriodePaieDTO saveUserc(@RequestParam(value="id", required=false) Long id,@RequestParam(value="name", required=true) String name,  
											@RequestParam(value="dateDeb", required=true) String dateDeb,	@RequestParam(value="dateFin", required=false) String dateFin,	 Principal principal) {
		
		User currentUser = userService.findByEmail(principal.getName());
		
		return opcommercialeService.save(id,name, dateDeb, dateFin, currentUser);
	}
	/*	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/loaduser", method = RequestMethod.POST)
	public @ResponseBody User loadUser(@RequestParam(value="id", required=true) Long id) {
		return userService.findUser(id);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/loadusers", method = RequestMethod.POST)
	public @ResponseBody List<User> loadUsers() {
		return userService.findUsers();
	}
	

*/
}

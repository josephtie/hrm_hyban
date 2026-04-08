package com.nectux.mizan.hyban.rh.carriere.service.impl;

import com.nectux.mizan.hyban.rh.carriere.dto.SiteWorkDTO;
import com.nectux.mizan.hyban.rh.carriere.entity.Site;
import com.nectux.mizan.hyban.rh.carriere.repository.SiteWorkRepository;
import com.nectux.mizan.hyban.rh.carriere.service.SiteService;
import com.nectux.mizan.hyban.utils.Erreur;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("siteService")
public class SiteWorkServiceImpl implements SiteService {
	
	@Autowired private SiteWorkRepository SiteWorkRepository;
	
	private StringBuilder sb;
	private Erreur erreur;
	private List<Erreur> listErreur;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SiteWorkDTO save(Long id, String libelle, String description) {
		// TODO Auto-generated method stub
		
		SiteWorkDTO SiteWorkDTO = new SiteWorkDTO();
		Site Site;
		erreur = new Erreur();
		listErreur = new ArrayList<Erreur>();
		
		try{
			if(id == null){
				Site = new Site();
				//Site.setDateCreation(new Date());
			} else {
				Site = SiteWorkRepository.findById(id) .orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + id));
				//Site.setDateModification(new Date());
			}
			Site.setLibelle(libelle);
		//	Site.setDescription(description);
			
			if(id == null){
				if(SiteWorkRepository.findByLibelle(Site.getLibelle()) != null){
					sb = new StringBuilder();
					erreur.setCode(10);
					erreur.setDescription("contrainte de doublon non respecte");
					sb.append("ce Site existe deja");
					erreur.setMessage(sb.toString());
					listErreur.add(erreur);
				}
			} else {
				if(SiteWorkRepository.findByIdNotAndLibelle(id, Site.getLibelle()) != null){
					sb = new StringBuilder();
					erreur.setCode(10);
					erreur.setDescription("contrainte de doublon non respecte");
					sb.append("ce Site existe deja");
					erreur.setMessage(sb.toString());
					listErreur.add(erreur);
				}
			}
			
			if(Site.getLibelle() == null || Site.getLibelle().trim().equals("")){
				sb = new StringBuilder();
				erreur.setCode(10);
				erreur.setDescription("contrainte d'integrite non null non respectee");
				sb.append("le champ libelle est obligatoire");
				erreur.setMessage(sb.toString());
				listErreur.add(erreur);
			} 
			
			if(listErreur.isEmpty()){
				Site = SiteWorkRepository.save(Site);
				sb = new StringBuilder();
				sb.append(Site.getLibelle()).append(" enregistre avec succes");
				SiteWorkDTO.setResult(true);
				SiteWorkDTO.setStatus(true);
				SiteWorkDTO.setRow(Site);
				SiteWorkDTO.setRows(null);
				SiteWorkDTO.setMessage(sb.toString());
				SiteWorkDTO.setTotal(0);
				SiteWorkDTO.setErrors(listErreur);
			} else {
				SiteWorkDTO.setResult(false);
				SiteWorkDTO.setStatus(false);
				SiteWorkDTO.setRow(null);
				SiteWorkDTO.setRows(null);
				SiteWorkDTO.setMessage("voir liste erreur");
				SiteWorkDTO.setTotal(0);
				SiteWorkDTO.setErrors(listErreur);
			}
			
		} catch(Exception ex){
			erreur.setCode(20);
			erreur.setDescription("exception capturee");
			listErreur.add(erreur);
			ex.printStackTrace();
			SiteWorkDTO.setResult(false);
			SiteWorkDTO.setStatus(false);
			SiteWorkDTO.setRow(null);
			SiteWorkDTO.setRows(null);
			SiteWorkDTO.setMessage(ex.getMessage());
			SiteWorkDTO.setTotal(0);
			SiteWorkDTO.setErrors(listErreur);
		}
		return SiteWorkDTO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SiteWorkDTO delete(Long id) {
		// TODO Auto-generated method stub
		
		SiteWorkDTO SiteWorkDTO = new SiteWorkDTO();
		Site Site;
		erreur = new Erreur();
		listErreur = new ArrayList<Erreur>();
		
		try{
			Site = SiteWorkRepository.findById(id) .orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + id));
			if(Site == null){
				sb = new StringBuilder();
				erreur.setCode(12);
				erreur.setDescription("Site inexistant");
				sb.append("ce Site est inexistant dans le systeme");
				erreur.setMessage(sb.toString());
				listErreur.add(erreur);
				
				SiteWorkDTO.setResult(false);
				SiteWorkDTO.setStatus(false);
				SiteWorkDTO.setRow(null);
				SiteWorkDTO.setRows(null);
				SiteWorkDTO.setMessage("voir liste erreur");
				SiteWorkDTO.setTotal(0);
				SiteWorkDTO.setErrors(listErreur);
			} 
			
			if(listErreur.isEmpty()) {
				SiteWorkRepository.delete(Site);
				sb = new StringBuilder();
				sb.append(Site.getLibelle()).append(" supprime avec succes");
				SiteWorkDTO.setResult(true);
				SiteWorkDTO.setStatus(true);
				SiteWorkDTO.setRow(Site);
				SiteWorkDTO.setRows(null);
				SiteWorkDTO.setMessage(sb.toString());
				SiteWorkDTO.setTotal(0);
				SiteWorkDTO.setErrors(listErreur);
			}
		} catch(Exception ex){
			erreur.setCode(20);
			erreur.setDescription("exception capturee");
			listErreur.add(erreur);
			ex.printStackTrace();
			SiteWorkDTO.setResult(false);
			SiteWorkDTO.setStatus(false);
			SiteWorkDTO.setRow(null);
			SiteWorkDTO.setRows(null);
			SiteWorkDTO.setMessage(ex.getMessage());
			SiteWorkDTO.setTotal(0);
			SiteWorkDTO.setErrors(listErreur);
		}
		return SiteWorkDTO;
	}

	@Override
	public SiteWorkDTO findSite(Long id) {
		// TODO Auto-generated method stub
		
		SiteWorkDTO SiteWorkDTO = new SiteWorkDTO();
		Site Site;
		erreur = new Erreur();
		listErreur = new ArrayList<Erreur>();
		
		try{
			Site = SiteWorkRepository.findById(id) .orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + id));
			if(Site == null){
				SiteWorkDTO.setResult(true);
				SiteWorkDTO.setStatus(true);
				SiteWorkDTO.setRow(Site);
				SiteWorkDTO.setRows(null);
				SiteWorkDTO.setMessage("Site inexistant dans le systeme");
				SiteWorkDTO.setTotal(1);
				SiteWorkDTO.setErrors(listErreur);
			} else {
				SiteWorkDTO.setResult(true);
				SiteWorkDTO.setStatus(true);
				SiteWorkDTO.setRow(Site);
				SiteWorkDTO.setRows(null);
				SiteWorkDTO.setMessage("Site trouve avec succes");
				SiteWorkDTO.setTotal(1);
				SiteWorkDTO.setErrors(listErreur);
			}
		} catch(Exception ex){
			erreur.setCode(20);
			erreur.setDescription("exception capturee");
			listErreur.add(erreur);
			ex.printStackTrace();
			SiteWorkDTO.setResult(false);
			SiteWorkDTO.setStatus(false);
			SiteWorkDTO.setRow(null);
			SiteWorkDTO.setRows(null);
			SiteWorkDTO.setMessage(ex.getMessage());
			SiteWorkDTO.setTotal(0);
			SiteWorkDTO.setErrors(listErreur);
		}
		return SiteWorkDTO;
	}

	@Override
	public SiteWorkDTO findSites() {
		// TODO Auto-generated method stub
		
		SiteWorkDTO SiteWorkDTO = new SiteWorkDTO();
		List<Site> listSite = new ArrayList<Site>();
		
		try{
			listSite = SiteWorkRepository.findAll();
			if(listSite == null){
				SiteWorkDTO.setResult(true);
				SiteWorkDTO.setStatus(true);
				SiteWorkDTO.setRow(null);
				SiteWorkDTO.setRows(new ArrayList<Site>());
				SiteWorkDTO.setMessage("aucun Site trouve");
				SiteWorkDTO.setTotal(0);
				SiteWorkDTO.setErrors(listErreur);
			} else {
				int i = listSite.size();
				sb = new StringBuilder();
				sb.append(i).append(" Site(s) trouve(s) avec succes");
				SiteWorkDTO.setResult(true);
				SiteWorkDTO.setStatus(true);
				SiteWorkDTO.setRow(null);
				SiteWorkDTO.setRows(listSite);
				SiteWorkDTO.setMessage(sb.toString());
				SiteWorkDTO.setTotal(i);
				SiteWorkDTO.setErrors(listErreur);
			}
		} catch (Exception ex){
			erreur.setCode(20);
			erreur.setDescription("exception capturee");
			listErreur.add(erreur);
			ex.printStackTrace();
			
			SiteWorkDTO.setResult(false);
			SiteWorkDTO.setStatus(false);
			SiteWorkDTO.setRow(null);
			SiteWorkDTO.setRows(listSite);
			SiteWorkDTO.setMessage(ex.getMessage());
			SiteWorkDTO.setTotal(0);
			SiteWorkDTO.setErrors(listErreur);
		}
		return SiteWorkDTO;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return (int) SiteWorkRepository.count();
	}

	@Override
	public SiteWorkDTO loadSites(Pageable pageable) {
		// TODO Auto-generated method stub
		SiteWorkDTO SiteWorkDTO = new SiteWorkDTO();
		Page<Site> page = SiteWorkRepository.findAll(pageable);
		SiteWorkDTO.setResult(true);
		SiteWorkDTO.setStatus(true);
		SiteWorkDTO.setRows(page.getContent());
		SiteWorkDTO.setTotal(page.getTotalElements());
		return SiteWorkDTO;
	}

	@Override
	public SiteWorkDTO loadSites(Pageable pageable, String libelle, String description) {
		// TODO Auto-generated method stub
		SiteWorkDTO SiteWorkDTO = new SiteWorkDTO();
		Page<Site> page = SiteWorkRepository.findByLibelleContaining(pageable, libelle);
		SiteWorkDTO.setResult(true);
		SiteWorkDTO.setStatus(true);
		SiteWorkDTO.setRows(page.getContent());
		SiteWorkDTO.setTotal(page.getTotalElements());
		return SiteWorkDTO;
	}

    @Override
    public List<Site> getSites() {
        return SiteWorkRepository.findAll();
    }

}

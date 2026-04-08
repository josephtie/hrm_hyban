package com.nectux.mizan.hyban.rh.carriere.service;

import com.nectux.mizan.hyban.rh.carriere.dto.SiteWorkDTO;
import com.nectux.mizan.hyban.rh.carriere.entity.Site;
import com.nectux.mizan.hyban.rh.carriere.entity.Site;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SiteService {
	
	public SiteWorkDTO save(Long id, String libelle, String description);
	
	public SiteWorkDTO delete(Long id);
	
	public SiteWorkDTO findSite(Long id);
	
	public SiteWorkDTO findSites();
	

	
	public int count();
	
	public SiteWorkDTO loadSites(Pageable pageable);
	
	public SiteWorkDTO loadSites(Pageable pageable, String libelle, String description);

    List<Site> getSites();
}

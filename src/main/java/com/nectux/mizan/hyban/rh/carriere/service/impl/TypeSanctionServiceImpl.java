package com.nectux.mizan.hyban.rh.carriere.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nectux.mizan.hyban.rh.carriere.service.TypeSanctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nectux.mizan.hyban.rh.carriere.dto.TypeSanctionDTO;
import com.nectux.mizan.hyban.rh.carriere.entity.TypeSanction;
import com.nectux.mizan.hyban.rh.carriere.repository.TypeSanctionRepository;
import com.nectux.mizan.hyban.utils.Erreur;

import jakarta.persistence.EntityNotFoundException;

@Transactional
@Service("typeSanctionService")
public class TypeSanctionServiceImpl implements TypeSanctionService {
	
	@Autowired private TypeSanctionRepository typeSanctionRepository;
	
	private StringBuilder sb;
	private Erreur erreur;
	private List<Erreur> listErreur;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public TypeSanctionDTO save(Long id, String libelle, String description) {
		TypeSanctionDTO typeSanctionDTO = new TypeSanctionDTO();
		TypeSanction typeSanction;
		listErreur = new ArrayList<Erreur>();
		
		try{
			// Validation des champs obligatoires
			if(libelle == null || libelle.trim().isEmpty()){
				sb = new StringBuilder();
				erreur = new Erreur();
				erreur.setCode(10);
				erreur.setDescription("contrainte d'integrite non null non respectee");
				sb.append("le champ libelle est obligatoire");
				erreur.setMessage(sb.toString());
				listErreur.add(erreur);
			}
			
			// Si pas d'erreur de validation, continuer le traitement
			if(listErreur.isEmpty()){
				if(id == null || id == 0){
					// Création d'un nouveau type de sanction
					typeSanction = new TypeSanction();
					typeSanction.setDateCreation(new Date());
					
					// Vérifier si le libellé existe déjà pour la création
					if(typeSanctionRepository.findByLibelle(libelle.trim()) != null){
						sb = new StringBuilder();
						erreur = new Erreur();
						erreur.setCode(10);
						erreur.setDescription("contrainte de doublon non respecte");
						sb.append("ce type de sanction existe deja");
						erreur.setMessage(sb.toString());
						listErreur.add(erreur);
					}
				} else {
					// Mise à jour d'un type de sanction existant
					typeSanction = typeSanctionRepository.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("TypeSanction not found for id " + id));
					typeSanction.setDateModification(new Date());
					
					// Vérifier si le libellé existe déjà pour un autre enregistrement
					if(typeSanctionRepository.findByIdNotAndLibelle(id, libelle.trim()) != null){
						sb = new StringBuilder();
						erreur = new Erreur();
						erreur.setCode(10);
						erreur.setDescription("contrainte de doublon non respecte");
						sb.append("ce type de sanction existe deja");
						erreur.setMessage(sb.toString());
						listErreur.add(erreur);
					}
				}
				
				// Si toujours pas d'erreur, procéder à la sauvegarde
				if(listErreur.isEmpty()){
					typeSanction.setLibelle(libelle.trim());
					typeSanction.setDescription(description != null ? description.trim() : "");
					
					typeSanction = typeSanctionRepository.save(typeSanction);
					
					sb = new StringBuilder();
					String action = (id == null || id == 0) ? "créé" : "mis à jour";
					sb.append(typeSanction.getLibelle()).append(" ").append(action).append(" avec succès");
					
					typeSanctionDTO.setResult(true);
					typeSanctionDTO.setStatus(true);
					typeSanctionDTO.setRow(typeSanction);
					typeSanctionDTO.setRows(null);
					typeSanctionDTO.setMessage(sb.toString());
					typeSanctionDTO.setTotal(0);
					typeSanctionDTO.setErrors(listErreur);
				} else {
					// Erreurs de doublon
					typeSanctionDTO.setResult(false);
					typeSanctionDTO.setStatus(false);
					typeSanctionDTO.setRow(null);
					typeSanctionDTO.setRows(null);
					typeSanctionDTO.setMessage("voir liste erreur");
					typeSanctionDTO.setTotal(0);
					typeSanctionDTO.setErrors(listErreur);
				}
			} else {
				// Erreurs de validation
				typeSanctionDTO.setResult(false);
				typeSanctionDTO.setStatus(false);
				typeSanctionDTO.setRow(null);
				typeSanctionDTO.setRows(null);
				typeSanctionDTO.setMessage("voir liste erreur");
				typeSanctionDTO.setTotal(0);
				typeSanctionDTO.setErrors(listErreur);
			}
			
		} catch(Exception ex){
			erreur = new Erreur();
			erreur.setCode(20);
			erreur.setDescription("exception capturee");
			erreur.setMessage("Une erreur technique est survenue: " + ex.getMessage());
			listErreur.add(erreur);
			ex.printStackTrace();
			
			typeSanctionDTO.setResult(false);
			typeSanctionDTO.setStatus(false);
			typeSanctionDTO.setRow(null);
			typeSanctionDTO.setRows(null);
			typeSanctionDTO.setMessage("erreur technique");
			typeSanctionDTO.setTotal(0);
			typeSanctionDTO.setErrors(listErreur);
		}
		return typeSanctionDTO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public TypeSanctionDTO delete(Long id) {
		// TODO Auto-generated method stub
		
		TypeSanctionDTO typeSanctionDTO = new TypeSanctionDTO();
		TypeSanction typeSanction;
		erreur = new Erreur();
		listErreur = new ArrayList<Erreur>();
		
		try{
			typeSanction = typeSanctionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + id));
			if(typeSanction == null){
				sb = new StringBuilder();
				erreur.setCode(12);
				erreur.setDescription("typeSanction inexistant");
				sb.append("ce typeSanction est inexistant dans le systeme");
				erreur.setMessage(sb.toString());
				listErreur.add(erreur);
				
				typeSanctionDTO.setResult(false);
				typeSanctionDTO.setStatus(false);
				typeSanctionDTO.setRow(null);
				typeSanctionDTO.setRows(null);
				typeSanctionDTO.setMessage("voir liste erreur");
				typeSanctionDTO.setTotal(0);
				typeSanctionDTO.setErrors(listErreur);
			} 
			
			if(listErreur.isEmpty()) {
				typeSanctionRepository.delete(typeSanction);
				sb = new StringBuilder();
				sb.append(typeSanction.getLibelle()).append(" supprime avec succes");
				typeSanctionDTO.setResult(true);
				typeSanctionDTO.setStatus(true);
				typeSanctionDTO.setRow(typeSanction);
				typeSanctionDTO.setRows(null);
				typeSanctionDTO.setMessage(sb.toString());
				typeSanctionDTO.setTotal(0);
				typeSanctionDTO.setErrors(listErreur);
			}
		} catch(Exception ex){
			erreur.setCode(20);
			erreur.setDescription("exception capturee");
			listErreur.add(erreur);
			ex.printStackTrace();
			typeSanctionDTO.setResult(false);
			typeSanctionDTO.setStatus(false);
			typeSanctionDTO.setRow(null);
			typeSanctionDTO.setRows(null);
			typeSanctionDTO.setMessage(ex.getMessage());
			typeSanctionDTO.setTotal(0);
			typeSanctionDTO.setErrors(listErreur);
		}
		return typeSanctionDTO;
	}

	@Override
	public TypeSanctionDTO findTypeSanction(Long id) {
		// TODO Auto-generated method stub
		
		TypeSanctionDTO typeSanctionDTO = new TypeSanctionDTO();
		TypeSanction typeSanction;
		erreur = new Erreur();
		listErreur = new ArrayList<Erreur>();
		
		try{
			typeSanction = typeSanctionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + id));
			if(typeSanction == null){
				typeSanctionDTO.setResult(true);
				typeSanctionDTO.setStatus(true);
				typeSanctionDTO.setRow(typeSanction);
				typeSanctionDTO.setRows(null);
				typeSanctionDTO.setMessage("typeSanction inexistant dans le systeme");
				typeSanctionDTO.setTotal(1);
				typeSanctionDTO.setErrors(listErreur);
			} else {
				typeSanctionDTO.setResult(true);
				typeSanctionDTO.setStatus(true);
				typeSanctionDTO.setRow(typeSanction);
				typeSanctionDTO.setRows(null);
				typeSanctionDTO.setMessage("typeSanction trouve avec succes");
				typeSanctionDTO.setTotal(1);
				typeSanctionDTO.setErrors(listErreur);
			}
		} catch(Exception ex){
			erreur.setCode(20);
			erreur.setDescription("exception capturee");
			listErreur.add(erreur);
			ex.printStackTrace();
			typeSanctionDTO.setResult(false);
			typeSanctionDTO.setStatus(false);
			typeSanctionDTO.setRow(null);
			typeSanctionDTO.setRows(null);
			typeSanctionDTO.setMessage(ex.getMessage());
			typeSanctionDTO.setTotal(0);
			typeSanctionDTO.setErrors(listErreur);
		}
		return typeSanctionDTO;
	}

	@Override
	public TypeSanctionDTO findTypeSanctions() {
		// TODO Auto-generated method stub
		
		TypeSanctionDTO typeSanctionDTO = new TypeSanctionDTO();
		List<TypeSanction> listTypeSanction = new ArrayList<TypeSanction>();
		
		try{
			listTypeSanction = typeSanctionRepository.findAll();
			if(listTypeSanction == null){
				typeSanctionDTO.setResult(true);
				typeSanctionDTO.setStatus(true);
				typeSanctionDTO.setRow(null);
				typeSanctionDTO.setRows(new ArrayList<TypeSanction>());
				typeSanctionDTO.setMessage("aucun typeSanction trouve");
				typeSanctionDTO.setTotal(0);
				typeSanctionDTO.setErrors(listErreur);
			} else {
				int i = listTypeSanction.size();
				sb = new StringBuilder();
				sb.append(i).append(" typeSanction(s) trouve(s) avec succes");
				typeSanctionDTO.setResult(true);
				typeSanctionDTO.setStatus(true);
				typeSanctionDTO.setRow(null);
				typeSanctionDTO.setRows(listTypeSanction);
				typeSanctionDTO.setMessage(sb.toString());
				typeSanctionDTO.setTotal(i);
				typeSanctionDTO.setErrors(listErreur);
			}
		} catch (Exception ex){
			erreur.setCode(20);
			erreur.setDescription("exception capturee");
			listErreur.add(erreur);
			ex.printStackTrace();
			
			typeSanctionDTO.setResult(false);
			typeSanctionDTO.setStatus(false);
			typeSanctionDTO.setRow(null);
			typeSanctionDTO.setRows(listTypeSanction);
			typeSanctionDTO.setMessage(ex.getMessage());
			typeSanctionDTO.setTotal(0);
			typeSanctionDTO.setErrors(listErreur);
		}
		return typeSanctionDTO;
	}

        @Override
        public List<TypeSanction> findTypesSanction(){
            return typeSanctionRepository.findAll();
        }
        
	@Override
	public int count() {
		// TODO Auto-generated method stub
		return (int) typeSanctionRepository.count();
	}

	@Override
	public TypeSanctionDTO loadTypeSanctions(Pageable pageable) {
		// TODO Auto-generated method stub
		TypeSanctionDTO typeSanctionDTO = new TypeSanctionDTO();
		Page<TypeSanction> page = typeSanctionRepository.findAll(pageable);
		typeSanctionDTO.setResult(true);
		typeSanctionDTO.setStatus(true);
		typeSanctionDTO.setRows(page.getContent());
		typeSanctionDTO.setTotal(page.getTotalElements());
		return typeSanctionDTO;
	}

	@Override
	public TypeSanctionDTO loadTypeSanctions(Pageable pageable, String libelle, String description) {
		// TODO Auto-generated method stub
		TypeSanctionDTO typeSanctionDTO = new TypeSanctionDTO();
		Page<TypeSanction> page = typeSanctionRepository.findByLibelleContainingOrDescriptionContaining(pageable, libelle, description);
		typeSanctionDTO.setResult(true);
		typeSanctionDTO.setStatus(true);
		typeSanctionDTO.setRows(page.getContent());
		typeSanctionDTO.setTotal(page.getTotalElements());
		return typeSanctionDTO;
	}

}

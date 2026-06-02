package com.nectux.mizan.hyban.paie.repository;

import com.nectux.mizan.hyban.paie.entity.Echelonnement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EchelonnementRepository extends CrudRepository<Echelonnement, Long> {
	
	
	
	public java.util.List<Echelonnement> findAll();
	
	public Page<Echelonnement> findAll(Pageable pageable);
	
	public java.util.List<Echelonnement> findByPretPersonnelId(Long idptret);
	
	public java.util.List<Echelonnement> findByPretPersonnelPretId(Long idptret);
	
	public java.util.List<Echelonnement> findByPretPersonnelPersonnelIdAndPeriodePaieIdAndPayeTrue(Long idpers,Long idperiode);
	
	public java.util.List<Echelonnement> findByPretPersonnelPersonnelIdAndPeriodePaieId(Long idpers,Long idperiode);
	
	public Page<Echelonnement> findByMontantLike(Pageable pageable,String search);
	
	//public Page<Echelonnement> findByPretPersonnelPersonnelNomLike(String search);

    // Nouvelles méthodes de recherche pour les filtres avancés
    public Page<Echelonnement> findByPretPersonnelPersonnelMatriculeIgnoreCaseContaining(Pageable pageable, String matricule);
    
    public Page<Echelonnement> findByPretPersonnelPersonnelNomIgnoreCaseContaining(Pageable pageable, String nom);
    
    public Page<Echelonnement> findByPretPersonnelPersonnelPrenomIgnoreCaseContaining(Pageable pageable, String prenom);
    
    public Page<Echelonnement> findByPaye(Pageable pageable, Boolean paye);
    
    public Page<Echelonnement> findByPeriodePaieId(Pageable pageable, Long periodePaieId);
    
    // Méthodes combinées pour la recherche multi-critères
    public Page<Echelonnement> findByPretPersonnelPersonnelMatriculeIgnoreCaseContainingAndPayeAndPeriodePaieId(
        Pageable pageable, String matricule, Boolean paye, Long periodePaieId);
    
    public Page<Echelonnement> findByPretPersonnelPersonnelNomIgnoreCaseContainingAndPayeAndPeriodePaieId(
        Pageable pageable, String nom, Boolean paye, Long periodePaieId);
    
    public Page<Echelonnement> findByPretPersonnelPersonnelPrenomIgnoreCaseContainingAndPayeAndPeriodePaieId(
        Pageable pageable, String prenom, Boolean paye, Long periodePaieId);
    
    // Méthodes combinées avec période seulement
    public Page<Echelonnement> findByPretPersonnelPersonnelMatriculeIgnoreCaseContainingAndPeriodePaieId(
        Pageable pageable, String matricule, Long periodePaieId);
    
    public Page<Echelonnement> findByPretPersonnelPersonnelNomIgnoreCaseContainingAndPeriodePaieId(
        Pageable pageable, String nom, Long periodePaieId);
    
    public Page<Echelonnement> findByPretPersonnelPersonnelPrenomIgnoreCaseContainingAndPeriodePaieId(
        Pageable pageable, String prenom, Long periodePaieId);
    
    // Méthode générique pour recherche par nom/prénom combiné
    public Page<Echelonnement> findByPretPersonnelPersonnelNomCompletIgnoreCaseContaining(
        Pageable pageable, String nomComplet);
    
    // Méthodes pour retourner List<Echelonnement> (pour l'export)
    public List<Echelonnement> findByPretPersonnelPersonnelMatriculeIgnoreCaseContaining(String matricule);
    
    public List<Echelonnement> findByPretPersonnelPersonnelNomIgnoreCaseContaining(String nom);
    
    public List<Echelonnement> findByPretPersonnelPersonnelPrenomIgnoreCaseContaining(String prenom);
    
    public List<Echelonnement> findByPaye(Boolean paye);

    List<Echelonnement> findByPeriodePaieId(Long idPeriode);
}

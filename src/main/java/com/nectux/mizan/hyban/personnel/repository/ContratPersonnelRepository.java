package com.nectux.mizan.hyban.personnel.repository;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.nectux.mizan.hyban.parametrages.entity.TypeContrat;
import com.nectux.mizan.hyban.personnel.entity.Categorie;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.personnel.entity.Fonction;
import com.nectux.mizan.hyban.personnel.entity.Personnel;

import com.nectux.mizan.hyban.rh.absences.entity.Absences;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface ContratPersonnelRepository extends JpaRepository<ContratPersonnel, Long>, JpaSpecificationExecutor<ContratPersonnel> {
	
	public List<ContratPersonnel> findAll();
	
	public ContratPersonnel findByPersonnelId(Long idPers);
    List<ContratPersonnel> findLastByStatutTrue();
	//public List<ContratPersonnel> findByPersonnelId(Long idPers);

    Optional<ContratPersonnel> findTopByPersonnelIdAndStatutTrueOrderByIdDesc(Long personnelId);
	public ContratPersonnel findByPersonnelMatricule(String mat);
	
	public ContratPersonnel findByPersonnelMatriculeAndStatut(String mat, Boolean val);
	
	public Page<ContratPersonnel> findByStatut(Boolean val, Pageable pageable);
	
	public Page<ContratPersonnel> findByStatutAndPersonnelMatriculeIgnoreCaseContainingOrPersonnelNomIgnoreCaseContaining(Pageable pageable,Boolean val, String matricule,String matricule1);

	public ContratPersonnel findByPersonnelIdAndStatut(Long idlong, Boolean val);

	public List<ContratPersonnel> findByPersonnelIdInAndStatut(List<Long> idsPers, Boolean statut);
	public List<ContratPersonnel> findByStatut( Boolean val);

	public List<ContratPersonnel> findByPersonnel(Personnel personnel);

	@Query("SELECT cp FROM ContratPersonnel cp " +
		   "LEFT JOIN FETCH cp.typeContrat " +
		   "LEFT JOIN FETCH cp.categorie " +
		   "LEFT JOIN FETCH cp.fonction " +
		   "WHERE cp.personnel = :personnel")
	public List<ContratPersonnel> findByPersonnelWithRelations(@Param("personnel") Personnel personnel);
	
	public List<ContratPersonnel> findByTypeContrat(TypeContrat typeContrat);
	
	//public ContratPersonnel findByTypeContratIdAndStatutAndPersonnelId(TypeContrat typeContrat);
	
	public List<ContratPersonnel> findByCategorie(Categorie categorie);
	
	public List<ContratPersonnel> findByFonction(Fonction fonction);

	public Page<ContratPersonnel> findByPersonnel(Personnel personnel, Pageable pageable);

	public Page<ContratPersonnel> findByStatut(Pageable pageable, Boolean statut);
	
	public ContratPersonnel findByIdAndStatut(Long idlong, Boolean statut);
	public ContratPersonnel findTop1ByPersonnelIdAndStatutOrderByDateDebutDesc(Long idlong, Boolean statut);
	
//	public Page<ContratPersonnel> findByStatut(Pageable pageable, Boolean statut);
	
	public Page<ContratPersonnel> findByTypeContratIdAndDateFinBetween(Pageable pageable,Long Idctr, Date  ddep, Date dfin );
	
	
/*	public final static String findByTypeContratIddate = "select u from ContratPersonnel u" +            
            " where u.dateFin >= :dateDeb  and u.typeContrat.id= :idopcom  ";

	@Query(findByTypeContratIddate)
	public List<ContratPersonnel> RechContratIddateExpired(Pageable pageable,@Param("dateDeb") Date dateDeb,@Param("idopcom") Long idopcom);*/
	
	public Page<ContratPersonnel> findAll(Pageable pageable);

	public List<ContratPersonnel> findByStatutTrue(); 
	
/*	public final static String find_Bulletin_Personnel = "select p from ContratPersonnel p, Personnel u  " + 
            " where p.personnel.id = u.id " +
            " and p.statut= true  "+
            " and u.id = :idPers ";

	@Query(find_Bulletin_Personnel)
	public ContratPersonnel findByContratPersonnelactif(@Param("idPers") Long idPers);*/

	
	//public ContratPersonnel findByPersonnelIdAndStatut(Long idPers,Boolean etat);
	
	public List<ContratPersonnel> findByDateFinBetween(java.util.Date dateDebut, java.util.Date  dateFin);
	public List<ContratPersonnel> findByStatutTrueAndDepartFalseOrderByPersonnelNomAscPersonnelPrenomAsc();
	public Page<ContratPersonnel> findByStatutTrueAndDepartFalseOrderByPersonnelNomAscPersonnelPrenomAsc(Pageable pageable);

	public List<ContratPersonnel> findByStatutTrueAndDepartFalseAndPersonnelNomIgnoreCaseContaining(String search);

	Page<ContratPersonnel> findByStatutTrueAndDepartTrueAndSoldeCalculeFalseOrderByPersonnelNomAscPersonnelPrenomAsc(Pageable pageable);

	Page<ContratPersonnel> findByStatutTrueAndDepartTrueAndSoldeCalculeFalseAndPersonnelNomIgnoreCaseContainingOrderByPersonnelNomAscPersonnelPrenomAsc(Pageable pageable,String search);

    ContratPersonnel findFirstByPersonnelIdAndStatutTrueOrderByDateDebutDesc(Long id);

    //  List<ContratPersonnel> findByTypeContratIdAndStatutTrue(Long id);


    /**
     * @deprecated Cette requete dupliquait la definition d'un "contrat actif" deja portee
     *             par {@code ContratPersonnelSpecifications}, ce qui avait provoque une
     *             divergence de resultats avec {@code findAllfilter}. Composer plutot
     *             {@code actif().and(personnelNonRetire()).and(recherche(...))} et passer
     *             la Specification a {@link #findAll(org.springframework.data.jpa.domain.Specification, Pageable)}.
     *             Conservee pour compatibilite ascendante.
     */
    @Deprecated
    @Query("SELECT p FROM ContratPersonnel p " +
            "JOIN p.personnel cp "+
            "WHERE p.statut = true " +
            "AND p.depart = false " +
            "AND cp.retraitEffect = false " +
            "AND (LOWER(cp.nom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(cp.prenom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(cp.matricule) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ContratPersonnel> searchContrat(@Param("search") String search, Pageable pageable);

    List<ContratPersonnel> findByEtatContrat(com.nectux.mizan.hyban.personnel.enums.EtatContrat etatContrat);

    @Query("SELECT cp FROM ContratPersonnel cp " +
            "LEFT JOIN FETCH cp.personnel " +
            "LEFT JOIN FETCH cp.typeContrat " +
            "LEFT JOIN FETCH cp.categorie " +
            "LEFT JOIN FETCH cp.fonction " +
            "WHERE cp.dateFin IS NOT NULL " +
            "AND cp.statut = true " +
            "AND cp.dateFin BETWEEN :dateDebut AND :dateFin " +
            "ORDER BY cp.dateFin ASC")
    List<ContratPersonnel> findContratsEcheance(@Param("dateDebut") java.util.Date dateDebut, @Param("dateFin") java.util.Date dateFin);

    @Query("SELECT cp FROM ContratPersonnel cp " +
            "LEFT JOIN FETCH cp.personnel " +
            "LEFT JOIN FETCH cp.typeContrat " +
            "WHERE cp.dateFin IS NOT NULL " +
            "AND cp.statut = true " +
            "AND cp.dateFin < :dateReference " +
            "AND cp.typeContrat.id <> 1 " +
            "ORDER BY cp.dateFin ASC")
    List<ContratPersonnel> findContratsExpired(@Param("dateReference") java.util.Date dateReference);

    @Query("SELECT COUNT(cp) FROM ContratPersonnel cp WHERE cp.statut = true AND cp.depart = false")
    long countContratsActifs();

    @Query("SELECT COUNT(cp) FROM ContratPersonnel cp WHERE cp.dateFin IS NOT NULL AND cp.statut = true AND cp.dateFin < :dateReference AND cp.typeContrat.id <> 1")
    long countContratsExpired(@Param("dateReference") java.util.Date dateReference);

    @Query("SELECT COUNT(cp) FROM ContratPersonnel cp WHERE cp.dateFin IS NOT NULL AND cp.statut = true AND cp.dateFin BETWEEN :dateDebut AND :dateFin")
    long countContratsEcheance(@Param("dateDebut") java.util.Date dateDebut, @Param("dateFin") java.util.Date dateFin);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE CGECI_RHPAIE_CONTRAT_PERSONNEL SET depart = :depart, statut = :statut, etat_contrat = :etatContrat, date_fin = :dateFin, date_mod = :dateMod, observ_ctrat = :observCtrat, solde_calcule = :soldeCalcule, motif_cloture = :motifCloture, operation_contrat = :operationContrat WHERE id = :id", nativeQuery = true)
    void updateContractEnd(@Param("id") Long id, @Param("depart") Boolean depart, @Param("statut") Boolean statut, @Param("etatContrat") String etatContrat, @Param("dateFin") java.util.Date dateFin, @Param("dateMod") java.util.Date dateMod, @Param("observCtrat") String observCtrat, @Param("soldeCalcule") Boolean soldeCalcule, @Param("motifCloture") String motifCloture, @Param("operationContrat") String operationContrat);


}


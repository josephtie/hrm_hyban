package com.nectux.mizan.hyban.personnel.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.nectux.mizan.hyban.personnel.entity.*;
import com.nectux.mizan.hyban.personnel.repository.*;
import jakarta.persistence.criteria.Predicate;

import com.nectux.mizan.hyban.paie.repository.PrimePersonnelRepository;
import com.nectux.mizan.hyban.parametrages.entity.TypeContrat;
import com.nectux.mizan.hyban.parametrages.repository.TypeContratRepository;
import com.nectux.mizan.hyban.utils.DifferenceDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import com.nectux.mizan.hyban.paie.entity.LivreDePaie;
import com.nectux.mizan.hyban.paie.entity.PrimePersonnel;

import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.personnel.dto.ContratPersonnelDTO;
import com.nectux.mizan.hyban.personnel.enums.EtatContrat;
import com.nectux.mizan.hyban.personnel.enums.MotifClotureContrat;
import com.nectux.mizan.hyban.personnel.enums.TypeOperationContrat;
import com.nectux.mizan.hyban.personnel.service.ContratPersonnelService;
import com.nectux.mizan.hyban.utils.DateManager;
import com.nectux.mizan.hyban.utils.Utils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Transactional
@Service("contratPersonnelService")
public class ContratPersonnelServiceImpl implements ContratPersonnelService {
	
	private static final Logger logger = LogManager.getLogger(ContratPersonnelServiceImpl.class);
	
	@Autowired FonctionRepository fonctionRepository;
	@Autowired CategorieRepository categorieRepository;
	@Autowired PersonnelRepository personnelRepository;
	@Autowired
	ContratDateFinHistoriqueRepository  contratDateFinHistoriqueRepository;
	@Autowired
    TypeContratRepository typeContratRepository;
	@Autowired ContratPersonnelRepository contratPersonnelRepository;
	@Autowired private PrimePersonnelRepository primePersonnelRepository;
	@Autowired private ContratHistoryRepository contratHistoryRepository;
	@Autowired private JdbcTemplate jdbcTemplate;
	@PersistenceContext private EntityManager entityManager;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ContratPersonnel save(ContratPersonnel contratPersonnel) {
		// TODO Auto-generated method stub
		return contratPersonnelRepository.save(contratPersonnel);
	}

	@Override
	public ContratPersonnelDTO save(Long id, Long idPersonnel, Long idCategorie, Long idFonction, Long idTypeContrat,
									String dateDebut, String dateFin, Double netAPayer, Double indemniteLogement, int ancienete, boolean statut,Double sursalaire,Double indemnitetransport,Double indemniterespons,Double indemniterepresent) {
		return save(id, idPersonnel, idCategorie, idFonction, idTypeContrat, dateDebut, dateFin, netAPayer, indemniteLogement, ancienete, statut, sursalaire, indemnitetransport, indemniterespons, indemniterepresent, null);
	}

	@Override
	public ContratPersonnelDTO save(Long id, Long idPersonnel, Long idCategorie, Long idFonction, Long idTypeContrat,
									String dateDebut, String dateFin, Double netAPayer, Double indemniteLogement, int ancienete, boolean statut,Double sursalaire,Double indemnitetransport,Double indemniterespons,Double indemniterepresent, TypeOperationContrat typeOperation) {
		// TODO Auto-generated method stub
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		try{
			ContratPersonnel contratPersonnel = new ContratPersonnel();
			if(id != null)
				contratPersonnel = contratPersonnelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ContratPersonnel not found for id " + id));

            contratPersonnel.setFonction(fonctionRepository.findById(idFonction).orElseThrow(() -> new EntityNotFoundException("Fonction not found for id " + idFonction)));
			contratPersonnel.setCategorie(categorieRepository.findById(idCategorie).orElseThrow(() -> new EntityNotFoundException("Categorie not found for id " + idCategorie)));
			contratPersonnel.setPersonnel(personnelRepository.findById(idPersonnel).orElseThrow(() -> new EntityNotFoundException("Personnel not found for id " + idPersonnel)));
			contratPersonnel.setTypeContrat(typeContratRepository.findById(idTypeContrat).orElseThrow(() -> new EntityNotFoundException("Type contrat not found for id " + idTypeContrat)));
			
			contratPersonnel.setDateDebut(Utils.stringToDate(dateDebut, "dd/MM/yyyy"));
				if(contratPersonnel.getTypeContrat().getId()==1L)
				   contratPersonnel.setDateFin(null);
				else
					contratPersonnel.setDateFin(Utils.stringToDate(dateFin, "dd/MM/yyyy"));

			contratPersonnel.setNetAPayer(BigDecimal.valueOf(netAPayer));
			contratPersonnel.setIndemniteLogement(BigDecimal.valueOf(indemniteLogement));
			contratPersonnel.setIndemniteRepresent(BigDecimal.valueOf(indemniterepresent));
			contratPersonnel.setIndemniteTransport(BigDecimal.valueOf(indemnitetransport));
			//contratPersonnel.setIndemniteResp(indemniterespons);
			contratPersonnel.setSursalaire(BigDecimal.valueOf(sursalaire));
			contratPersonnel.setAncienneteInitial(ancienete);
			//contratPersonnel.setStatut(statut);
			contratPersonnel.setStatut(true);
			contratPersonnel.setDepart(false);
			contratPersonnel.setEtatContrat(EtatContrat.ACTIF);
			//contratPersonnel.setOperationContrat(typeOperation.);

			// Déterminer le type d'opération pour l'historique
			TypeOperationContrat histType = (typeOperation != null) ? typeOperation : TypeOperationContrat.MODIFICATION;
			contratPersonnel.setOperationContrat(histType);
			String histLabel = (id != null) ? histType.getLibelle() + " du contrat" : "Création du contrat";
			contratPersonnel = contratPersonnelRepository.save(contratPersonnel);
			

			contratHistoryRepository.save(new ContratHistory(contratPersonnel.getId(), histType, histLabel, null));
			
			if(contratPersonnel.getTypeContrat().getId()==4L){
				contratPersonnel.getPersonnel().setStage(true);
				contratPersonnel.getPersonnel().setFonctionnaire(false);
				contratPersonnel.getPersonnel().setConsultant(false);

			}

			if(contratPersonnel.getTypeContrat().getId()==5L){
				contratPersonnel.getPersonnel().setConsultant(true);
				contratPersonnel.getPersonnel().setFonctionnaire(false);
				contratPersonnel.getPersonnel().setStage(false);
			}

			if(contratPersonnel.getTypeContrat().getId()==6L){
				contratPersonnel.getPersonnel().setConsultant(false);
				contratPersonnel.getPersonnel().setFonctionnaire(true);
				contratPersonnel.getPersonnel().setStage(false);
			}
			 personnelRepository.save(contratPersonnel.getPersonnel());
			contratPersonnelDTO.setRow(contratPersonnel);
			contratPersonnelDTO.setResult("success");
			logger.info(new StringBuilder().append(">>>>> ").append(contratPersonnel.toString()).append(" ENREGISTRE AVEC SUCCES").toString());
		} catch(Exception ex){
			contratPersonnelDTO.setResult("failed");
			logger.error(ex.getMessage());
			logger.error(new StringBuilder().append(">>>>>  ERREUR SUR ENREGISTREMENT CONTRAT PERSONNEL").toString());
			ex.getStackTrace();
			System.out.println("#######################################################################");
			System.out.println(ex.getMessage());
		}
		return contratPersonnelDTO;
	}

	@Override
	public Boolean delete(Long id) {
		// TODO Auto-generated method stub
		ContratPersonnel contratPersonnel = contratPersonnelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + id));
		if(contratPersonnel == null)
			return false;
		contratPersonnelRepository.delete(contratPersonnel);
		return true;
	}

	@Override
	public ContratPersonnel findContratPersonnel(Long id) {
		// TODO Auto-generated method stub
		return contratPersonnelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + id));
	}

	@Override
	public List<ContratPersonnel> findByPersonnel(Personnel personnel) {
		// TODO Auto-generated method stub
		return contratPersonnelRepository.findByPersonnel(personnel);
	}

	@Override
	public List<ContratPersonnel> findByPersonnelWithRelations(Personnel personnel) {
		return contratPersonnelRepository.findByPersonnelWithRelations(personnel);
	}

	@Override
	public List<ContratPersonnel> findByTypeContrat(TypeContrat typeContrat) {
		// TODO Auto-generated method stub
		return contratPersonnelRepository.findByTypeContrat(typeContrat);
	}

	@Override
	public List<ContratPersonnel> findByCategorie(Categorie categorie) {
		// TODO Auto-generated method stub
		return contratPersonnelRepository.findByCategorie(categorie);
	}

	@Override
	public List<ContratPersonnel> findByFonction(Fonction fonction) {
		// TODO Auto-generated method stub
		return contratPersonnelRepository.findByFonction(fonction);
	}

	@Override
	public List<ContratPersonnel> findContratPersonnels() {
		// TODO Auto-generated method stub
		return contratPersonnelRepository.findAll();
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return (int) contratPersonnelRepository.count();
	}

	@Override
	public ContratPersonnel findByPersonnelContratActif(Long idPers) {
		// TODO Auto-generated method stub
		return contratPersonnelRepository.findByPersonnelId(idPers);
	}

	@Override
	public List<ContratPersonnel> findExpireContract() throws Exception {
		// TODO Auto-generated method stub
		Date dateExpire = DateManager.addingDate(30);
		return contratPersonnelRepository.findByDateFinBetween(new Date(), dateExpire);
	}

	@Override
	public List<ContratPersonnel> findExpireContract(int delay) throws Exception {
		// TODO Auto-generated method stub
		Date dateExpire = DateManager.addingDate(delay);
		return contratPersonnelRepository.findByDateFinBetween(new Date(), dateExpire);
	}

	@Override
	public ContratPersonnelDTO findContratPersonnelk(Long id) {
		// TODO Auto-generated method stub
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		contratPersonnelDTO.setRow(contratPersonnelRepository.findByPersonnelIdAndStatut(id, true));
		contratPersonnelDTO.setResult(true);
		contratPersonnelDTO.setStatus(true);
		return contratPersonnelDTO;
	}
	
	@Override
	public ContratPersonnelDTO loadContratByPersonnel(Personnel personnel, Pageable pageable) {
		// TODO Auto-generated method stub
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		Page<ContratPersonnel> page = contratPersonnelRepository.findByPersonnel(personnel, pageable);
		contratPersonnelDTO.setRows(page.getContent());
		contratPersonnelDTO.setTotal(page.getTotalElements());
		logger.info(new StringBuilder().append(">>>>> CONTRATS PERSONNELS CHARGES AVEC SUCCES").toString());
		return contratPersonnelDTO;
	}

	@Override
	public ContratPersonnelDTO findAllfilter(Map<String, String> filters, Pageable pageable) {
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		
		// Construire une requête dynamique avec les filtres
		Specification<ContratPersonnel> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			// Filtre de recherche textuelle (sur nom, prénom, matricule)
			String search = filters.get("search");
			if (search != null && !search.trim().isEmpty()) {
				String searchPattern = "%" + search.toLowerCase() + "%";
				Predicate searchPredicate = criteriaBuilder.or(
					criteriaBuilder.like(criteriaBuilder.lower(root.get("personnel").get("nom")), searchPattern),
					criteriaBuilder.like(criteriaBuilder.lower(root.get("personnel").get("prenom")), searchPattern),
					criteriaBuilder.like(criteriaBuilder.lower(root.get("personnel").get("matricule")), searchPattern)
				);
				predicates.add(searchPredicate);
			}
			
			// Filtre par statut
			String statut = filters.get("statut");
			if (statut != null && !statut.trim().isEmpty()) {
				logger.info(">>>>> FILTRE STATUT REÇU: " + statut);
				if ("active".equals(statut)) {
					// Contrats actifs : statut = true ET depart = false (aligné avec loadContratActif)
					logger.info(">>>>> FILTRE ACTIF - statut=true AND depart=false");
					predicates.add(criteriaBuilder.and(
						criteriaBuilder.equal(root.get("statut"), true),
						criteriaBuilder.equal(root.get("depart"), false)
					));
				} else if ("inactive".equals(statut)) {
					// Contrats inactifs : statut = false uniquement
					logger.info(">>>>> FILTRE INACTIF - Contrats avec statut = false");
					predicates.add(criteriaBuilder.equal(root.get("statut"), false));
				}
			}
			
			// Filtre par type de contrat
			String typeContrat = filters.get("typeContrat");
			if (typeContrat != null && !typeContrat.trim().isEmpty()) {
				if (typeContrat.contains(",")) {
					java.util.List<String> typeList = java.util.Arrays.stream(typeContrat.split(","))
						.map(String::trim)
						.filter(s -> !s.isEmpty())
						.collect(java.util.stream.Collectors.toList());
					jakarta.persistence.criteria.CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("typeContrat").get("libelle"));
					for (String type : typeList) {
						inClause.value(type);
					}
					predicates.add(inClause);
				} else {
					predicates.add(criteriaBuilder.equal(root.get("typeContrat").get("libelle"), typeContrat));
				}
			}
			
			// Filtre par salaire catégoriel
			String salaireFilter = filters.get("salaireRange");
			if (salaireFilter != null && !salaireFilter.trim().isEmpty()) {
				switch (salaireFilter) {
					case "low":
						predicates.add(criteriaBuilder.lessThan(root.get("categorie").get("salaireDeBase"), 100000));
						break;
					case "medium":
						predicates.add(criteriaBuilder.between(root.get("categorie").get("salaireDeBase"), 100000, 200000));
						break;
					case "high":
						predicates.add(criteriaBuilder.between(root.get("categorie").get("salaireDeBase"), 200000, 350000));
						break;
					case "veryhigh":
						predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("categorie").get("salaireDeBase"), 350000));
						break;
				}
			}
			
			// Filtre pour les contrats expirants
			String expires = filters.get("expires");
			if (expires != null && expires.equals("true")) {
				LocalDate today = LocalDate.now();
				java.sql.Timestamp sqlToday = java.sql.Timestamp.valueOf(today.atStartOfDay());
				predicates.add(criteriaBuilder.lessThan(root.get("dateFin"), sqlToday));
			}
			
			// Filtre pour les contrats actifs
			String active = filters.get("active");
			if (active != null && active.equals("true")) {
				LocalDate today = LocalDate.now();
				java.sql.Timestamp sqlToday = java.sql.Timestamp.valueOf(today.atStartOfDay());
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateFin"), sqlToday));
			}
			
			// Filtre pour les contrats qui expirent à une date spécifique
			String expireDate = filters.get("expireDate");
			if (expireDate != null && !expireDate.trim().isEmpty()) {
				try {
					// Convertir la chaîne en LocalDate puis en Timestamp pour la comparaison
					LocalDate localExpireDate = LocalDate.parse(expireDate);
					java.sql.Timestamp sqlExpireDate = java.sql.Timestamp.valueOf(localExpireDate.atStartOfDay());
					predicates.add(criteriaBuilder.equal(root.get("dateFin"), sqlExpireDate));
					logger.info(">>>>> FILTRE EXPIRE DATE: " + expireDate + " (converti en Timestamp)");
				} catch (Exception e) {
					logger.error(">>>>> ERREUR CONVERSION DATE: " + expireDate, e);
				}
			}
			
			// Filtre pour les contrats qui expirent au plus tard à une date donnée (dateFin <= date)
			String expireDateMax = filters.get("expireDateMax");
			if (expireDateMax != null && !expireDateMax.trim().isEmpty()) {
				try {
					LocalDate localExpireDateMax = LocalDate.parse(expireDateMax);
					java.sql.Timestamp sqlExpireDateMax = java.sql.Timestamp.valueOf(localExpireDateMax.atTime(23, 59, 59));
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateFin"), sqlExpireDateMax));
					logger.info(">>>>> FILTRE EXPIRE DATE MAX: " + expireDateMax + " (converti en Timestamp)");
				} catch (Exception e) {
					logger.error(">>>>> ERREUR CONVERSION DATE MAX: " + expireDateMax, e);
				}
			}
			
			// Filtre pour les contrats qui expirent dans une période
			String expirePeriodStart = filters.get("expirePeriodStart");
			String expirePeriodEnd = filters.get("expirePeriodEnd");
			if (expirePeriodStart != null && !expirePeriodStart.trim().isEmpty() && 
				expirePeriodEnd != null && !expirePeriodEnd.trim().isEmpty()) {
				try {
					// Convertir les chaînes en LocalDate puis en Timestamp pour la comparaison
					LocalDate localStartDate = LocalDate.parse(expirePeriodStart);
					LocalDate localEndDate = LocalDate.parse(expirePeriodEnd);
					java.sql.Timestamp sqlStartDate = java.sql.Timestamp.valueOf(localStartDate.atStartOfDay());
					java.sql.Timestamp sqlEndDate = java.sql.Timestamp.valueOf(localEndDate.atStartOfDay());
					predicates.add(criteriaBuilder.between(root.get("dateFin"), sqlStartDate, sqlEndDate));
					logger.info(">>>>> FILTRE PERIODE: " + expirePeriodStart + " à " + expirePeriodEnd + " (converti en Timestamp)");
				} catch (Exception e) {
					logger.error(">>>>> ERREUR CONVERSION PERIODE: " + expirePeriodStart + " - " + expirePeriodEnd, e);
				}
			}
			
			// Filtre par état contractuel (carec)
			String carec = filters.get("carec");
			if (carec != null) {
				Boolean carecValue = Boolean.parseBoolean(carec);
				logger.info(">>>>> FILTRE CAREC: " + carecValue);
				predicates.add(criteriaBuilder.equal(root.get("personnel").get("carec"), carecValue));
			}
			
			// Log de débogage pour diagnostiquer
			logger.info(">>>>> FILTRES APPLIQUES: " + filters.toString());
			logger.info(">>>>> NOMBRE DE PREDICATS: " + predicates.size());
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
		
		Page<ContratPersonnel> page = contratPersonnelRepository.findAll(specification, pageable);
		logger.info(">>>>> NOMBRE DE CONTRATS TROUVES: " + page.getTotalElements());
		if (page.getContent().isEmpty()) {
			logger.info(">>>>> AUCUN CONTRAT TROUVE - VÉRIFICATION DES DONNÉES");
		} else {
			logger.info(">>>>> CONTENU DES CONTRATS: " + page.getContent().stream()
				.map(c -> "ID:" + c.getId() + ", Personnel:" + c.getPersonnel().getNom() + " " + c.getPersonnel().getPrenom() + ", CAREC:" + c.getPersonnel().getCarec())
				.collect(java.util.stream.Collectors.joining("; ")));
		}
		
		contratPersonnelDTO.setResult(true);
		contratPersonnelDTO.setStatus(true);
		contratPersonnelDTO.setRows(page.getContent());
		contratPersonnelDTO.setTotal(page.getTotalElements());
		return contratPersonnelDTO;
	}

	@Override
	public ContratPersonnelDTO loadContratByPersonnel(Personnel personnel, Pageable pageable, String search) {
		// TODO Auto-generated method stub
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		Page<ContratPersonnel> page = contratPersonnelRepository.findByPersonnel(personnel, pageable);
		contratPersonnelDTO.setRows(page.getContent());
		contratPersonnelDTO.setTotal(page.getTotalElements());
		logger.info(new StringBuilder().append(">>>>> CONTRATS PERSONNELS CHARGES AVEC SUCCES").toString());
		return contratPersonnelDTO;
	}

	@Override
	public ContratPersonnelDTO loadContratPersonnelActif(Pageable pageable) {
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		Page<ContratPersonnel> page = contratPersonnelRepository.findByStatutTrueAndDepartFalseOrderByPersonnelNomAscPersonnelPrenomAsc( pageable);
		contratPersonnelDTO.setRows(page.getContent());
		contratPersonnelDTO.setTotal(page.getTotalElements());
		logger.info(new StringBuilder().append(">>>>> CONTRATS PERSONNELS CHARGES AVEC SUCCES").toString());
		return contratPersonnelDTO;
	}
	
	/*@Override
	public ContratPersonnelDTO loadContratExpieredumois(Pageable pageable,Long IdTypctr, Date  dfin ) {
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		Page<ContratPersonnel> page = contratPersonnelRepository.findByStatut(true, pageable);
		contratPersonnelDTO.setRows(page.getContent());
		contratPersonnelDTO.setTotal(page.getTotalElements());
		logger.info(new StringBuilder().append(">>>>> CONTRATS PERSONNELS CHARGES AVEC SUCCES").toString());
		return contratPersonnelDTO;
	}*/

	@Override
	public ContratPersonnelDTO loadContratPersonnelActif(Pageable pageable,	String search,	String search1) {
		// TODO Auto-generated method stub
		/*ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		Page<ContratPersonnel> page = contratPersonnelRepository.findByStatutAndPersonnelMatriculeIgnoreCaseContainingOrPersonnelNomIgnoreCaseContaining(pageable,true,search,search );
		contratPersonnelDTO.setRows(page.getContent());
		contratPersonnelDTO.setTotal(page.getTotalElements());
		logger.info(new StringBuilder().append(">>>>> CONTRATS PERSONNELS CHARGES AVEC SUCCES").toString());
		return contratPersonnelDTO;*/
		
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		List<ContratPersonnel> myList2=new ArrayList<ContratPersonnel>();
		List<ContratPersonnel> myList=contratPersonnelRepository.findByStatutTrueAndDepartFalseAndPersonnelNomIgnoreCaseContaining(search);
		for(ContratPersonnel contratpersonnel1 : myList){
			if(contratpersonnel1.getPersonnel().getRetraitEffect()==false && contratpersonnel1.getDepart()==false && contratpersonnel1.getStatut()==true)
				myList2.add(contratpersonnel1);
			else{}
		}
		int start =(int) pageable.getOffset();
		int end = (start + (int) pageable.getPageSize()) > myList2.size() ? myList2.size() : (start + pageable.getPageSize());
		Page<ContratPersonnel>	pageImpianto=new PageImpl<ContratPersonnel>(myList2.subList(start, end), pageable,myList2.size());
		//Page<ContratPersonnel> pageImpianto = new PageImpl<ContratPersonnel>(myList);
		contratPersonnelDTO.setRows(pageImpianto.getContent());
		contratPersonnelDTO.setTotal(pageImpianto.getTotalElements());
		logger.info(new StringBuilder().append(">>>>> CONTRATS PERSONNELS CHARGES AVEC SUCCES").toString());
		return contratPersonnelDTO;
	}

	@Override
	public List<ContratPersonnel> rechercherBytypeContrat(TypeContrat annee) {
		return null;
	}



	@Override
	public ContratPersonnelDTO loadContratDepart(Pageable pageable) {
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		try {
			Page<ContratPersonnel> page = contratPersonnelRepository.findByStatutTrueAndDepartTrueAndSoldeCalculeFalseOrderByPersonnelNomAscPersonnelPrenomAsc(pageable);
			contratPersonnelDTO.setRows(page.getContent());
			contratPersonnelDTO.setTotal(page.getTotalElements());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return contratPersonnelDTO;
	}

	@Override
	public ContratPersonnelDTO loadContratDepart(Pageable pageable, String search) {
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		try {
			Page<ContratPersonnel> page = contratPersonnelRepository.findByStatutTrueAndDepartTrueAndSoldeCalculeFalseAndPersonnelNomIgnoreCaseContainingOrderByPersonnelNomAscPersonnelPrenomAsc(pageable,search);
			contratPersonnelDTO.setRows(page.getContent());
			contratPersonnelDTO.setTotal(page.getTotalElements());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return contratPersonnelDTO;
	}


    @Override
    public ContratPersonnelDTO loadContratActif(Pageable pageable) {
        ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
        try {
            Page<ContratPersonnel> page = contratPersonnelRepository.findByStatutTrueAndDepartFalseOrderByPersonnelNomAscPersonnelPrenomAsc(pageable);
            contratPersonnelDTO.setRows(page.getContent());
            contratPersonnelDTO.setTotal(page.getTotalElements());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return contratPersonnelDTO;
    }

	@Override
	public ContratPersonnelDTO loadContratActif(Pageable pageable, String search) {
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		try {
			Page<ContratPersonnel> page = contratPersonnelRepository.searchContrat(search, pageable);
			contratPersonnelDTO.setRows(page.getContent());
			contratPersonnelDTO.setTotal(page.getTotalElements());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return contratPersonnelDTO;
	}



	//@Override
	//public ContratPersonnelDTO endContract(Long id, String dateFin, String dateMod, Boolean depart, String ObservCtrat, MotifClotureContrat motifCloture) {
	//	return endContract(id, dateFin, dateMod, depart, ObservCtrat, motifCloture, null,null);
	//}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ContratPersonnelDTO endContractNew(
			Long id,
			String dateFin,
			String dateMod,
			Boolean depart,
			String observCtrat,
			MotifClotureContrat motifCloture,
			TypeOperationContrat typeOperation) {

		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();

		try {

			// ============================================================
			// 1. VALIDATION DES PARAMETRES
			// ============================================================

			if (id == null) {
				throw new IllegalArgumentException("L'identifiant du contrat est obligatoire.");
			}

			if (dateFin == null || dateFin.trim().isEmpty()) {
				throw new IllegalArgumentException("Date de fin du contrat invalide.");
			}

			// ============================================================
			// 2. RECUPERATION DU CONTRAT
			// ============================================================

			ContratPersonnel contratPersonnel =
					contratPersonnelRepository.findById(id)
							.orElseThrow(() ->
									new EntityNotFoundException(
											"Contrat introuvable pour l'id : " + id));

			// ============================================================
			// 3. MISE A JOUR DES INFORMATIONS GENERALES
			// ============================================================

			contratPersonnel.setDateFin(
					DateManager.stringToDate(dateFin, "dd/MM/yyyy")
			);

			if (dateMod != null && !dateMod.trim().isEmpty()) {
				contratPersonnel.setDateMod(
						DateManager.stringToDate(dateMod, "dd/MM/yyyy")
				);
			}

			contratPersonnel.setObservCtrat(observCtrat);

			// Le solde doit être recalculé après une modification/clôture
			contratPersonnel.setSoldeCalcule(Boolean.FALSE);

			// ============================================================
			// 4. MEMORISER L'OPERATION
			// ============================================================

			if (typeOperation != null) {
				contratPersonnel.setOperationContrat(typeOperation);
			} else {
				// Si aucune opération n'est fournie,
				// on considère l'opération comme une clôture.
				contratPersonnel.setOperationContrat(
						TypeOperationContrat.CLOTURE
				);
			}

			// ============================================================
			// 5. MEMORISER LE MOTIF DE CLOTURE
			// ============================================================

			if (motifCloture != null) {
				contratPersonnel.setMotifCloture(motifCloture);
			}

			// ============================================================
			// 6. DETERMINATION DU DEPART
			// ============================================================
			//
			// TRUE  = départ définitif du salarié
			// FALSE = le salarié reste dans l'entreprise
			//
			// On utilise Boolean.TRUE.equals() pour éviter les problèmes
			// lorsque depart == null.
			// ============================================================

			boolean departDefinitif = Boolean.TRUE.equals(depart);

			contratPersonnel.setDepart(departDefinitif);

			// ============================================================
			// 7. DEPART DEFINITIF
			// ============================================================

			if (departDefinitif) {

				contratPersonnel.setDepart(Boolean.TRUE);

				// Le contrat est terminé
				contratPersonnel.setStatut(Boolean.TRUE);
				contratPersonnel.setEtatContrat(EtatContrat.TERMINE);

				// Le salarié quitte définitivement l'entreprise
				if (contratPersonnel.getPersonnel() != null) {

					contratPersonnel.getPersonnel()
							.setRetraitEffect(Boolean.FALSE);

					contratPersonnel.getPersonnel()
							.setStatut(Boolean.TRUE);
				}

			}

			// ============================================================
			// 8. PAS DE DEPART DEFINITIF
			// ============================================================

			else {

				// Très important :
				// le salarié ne quitte pas définitivement l'entreprise.
				contratPersonnel.setDepart(Boolean.FALSE);

				if (typeOperation == null) {

					contratPersonnel.setStatut(Boolean.FALSE);
					contratPersonnel.setEtatContrat(
							EtatContrat.INACTIF
					);

				} else {

					switch (typeOperation) {

						// ------------------------------------------------
						// SUSPENSION
						// ------------------------------------------------

						case SUSPENSION:

							contratPersonnel.setDepart(Boolean.FALSE);
							contratPersonnel.setStatut(Boolean.FALSE);
							contratPersonnel.setEtatContrat(
									EtatContrat.SUSPENDU
							);

							break;

						// ------------------------------------------------
						// CLOTURE
						// ------------------------------------------------

						case CLOTURE:

							contratPersonnel.setDepart(Boolean.FALSE);
							contratPersonnel.setStatut(Boolean.FALSE);
							contratPersonnel.setEtatContrat(
									EtatContrat.TERMINE
							);

							break;

						// ------------------------------------------------
						// RESILIATION
						// ------------------------------------------------

						case RESILIATION:

							contratPersonnel.setDepart(Boolean.FALSE);
							contratPersonnel.setStatut(Boolean.FALSE);
							contratPersonnel.setEtatContrat(
									EtatContrat.RESILIE
							);

							break;

						// ------------------------------------------------
						// MODIFICATION
						// ------------------------------------------------

						case MODIFICATION:

							contratPersonnel.setDepart(Boolean.FALSE);
							contratPersonnel.setStatut(Boolean.FALSE);
							contratPersonnel.setEtatContrat(
									EtatContrat.INACTIF
							);

							break;

						// ------------------------------------------------
						// RENOUVELLEMENT
						// ------------------------------------------------

						case RENOUVELLEMENT:

							contratPersonnel.setDepart(Boolean.FALSE);
							contratPersonnel.setStatut(Boolean.FALSE);
							contratPersonnel.setEtatContrat(
									EtatContrat.INACTIF
							);

							break;

						// ------------------------------------------------
						// AVENANT
						// ------------------------------------------------

						case AVENANT:

							contratPersonnel.setDepart(Boolean.FALSE);
							contratPersonnel.setStatut(Boolean.FALSE);
							contratPersonnel.setEtatContrat(
									EtatContrat.INACTIF
							);

							break;

						// ------------------------------------------------
						// PAR DEFAUT
						// ------------------------------------------------

						default:

							contratPersonnel.setDepart(Boolean.FALSE);
							contratPersonnel.setStatut(Boolean.FALSE);
							contratPersonnel.setEtatContrat(
									EtatContrat.INACTIF
							);

							break;
					}
				}
			}

			// ============================================================
			// 9. LOG AVANT ENREGISTREMENT
			// ============================================================

			logger.info(
					">>> AVANT SAVE CONTRAT : id={}, depart={}, statut={}, " +
							"etatContrat={}, operationContrat={}, motifCloture={}",
					contratPersonnel.getId(),
					contratPersonnel.getDepart(),
					contratPersonnel.getStatut(),
					contratPersonnel.getEtatContrat(),
					contratPersonnel.getOperationContrat(),
					contratPersonnel.getMotifCloture()
			);

			// ============================================================
			// 10. ENREGISTREMENT DU CONTRAT
			// ============================================================

			contratPersonnelRepository.saveAndFlush(contratPersonnel);

			logger.info(
					">>> APRES SAVE/FLUSH CONTRAT : id={}, depart={}, statut={}, etat={}",
					contratPersonnel.getId(),
					contratPersonnel.getDepart(),
					contratPersonnel.getStatut(),
					contratPersonnel.getEtatContrat()
			);

			// ============================================================
			// 11. VERIFICATION DIRECTE EN BASE
			// ============================================================

			Boolean dbDepart = jdbcTemplate.queryForObject(
					"SELECT depart " +
							"FROM cgeci_rhpaie_contrat_personnel " +
							"WHERE id = ?",
					Boolean.class,
					contratPersonnel.getId()
			);

			logger.info(
					">>> DB READ-BACK : id={}, depart={}",
					contratPersonnel.getId(),
					dbDepart
			);

			// ============================================================
			// 12. ENREGISTREMENT DU PERSONNEL
			// ============================================================

//			if (contratPersonnel.getPersonnel() != null) {
//
//				personnelRepository.saveAndFlush(
//						contratPersonnel.getPersonnel()
//				);
//			}

			// ============================================================
			// 13. NOUVELLE VERIFICATION DB
			// ============================================================

			dbDepart = jdbcTemplate.queryForObject(
					"SELECT depart " +
							"FROM cgeci_rhpaie_contrat_personnel " +
							"WHERE id = ?",
					Boolean.class,
					contratPersonnel.getId()
			);

			logger.info(
					">>> DB READ-BACK APRES PERSONNEL : id={}, depart={}",
					contratPersonnel.getId(),
					dbDepart
			);

			// ============================================================
			// 14. HISTORIQUE
			// ============================================================

//			TypeOperationContrat histType =
//					typeOperation != null
//							? typeOperation
//							: TypeOperationContrat.CLOTURE;
//
//			String description =
//					histType.getLibelle()
//							+ " du contrat"
//							+ (
//							motifCloture != null
//									? " - Motif : "
//									+ motifCloture.getLibelle()
//									: ""
//					);
//
//			ContratHistory historique =
//					new ContratHistory(
//							contratPersonnel.getId(),
//							histType,
//							description,
//							null
//					);
//
//			contratHistoryRepository.save(historique);

			// ============================================================
			// 15. REPONSE
			// ============================================================

			contratPersonnelDTO.setRow(contratPersonnel);
			contratPersonnelDTO.setResult("success");
			contratPersonnelDTO.setStatus(Boolean.TRUE);

			logger.info(
					">>> endContractNew TERMINEE AVEC SUCCES : id={}",
					contratPersonnel.getId()
			);

		} catch (Exception ex) {

			logger.error(
					">>> ERREUR endContractNew : {}",
					ex.getMessage(),
					ex
			);

			contratPersonnelDTO.setResult("failed");
			contratPersonnelDTO.setStatus(Boolean.FALSE);
		}

		logger.info(
				">>> RETURN endContractNew : result={}",
				contratPersonnelDTO.getResult()
		);

		return contratPersonnelDTO;
	}

	@Override
	public ContratPersonnelDTO suspendreContrat(Long id, String observations) {
		ContratPersonnelDTO dto = new ContratPersonnelDTO();
		try {
			ContratPersonnel contrat = contratPersonnelRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("Contrat not found for id " + id));
			contrat.setEtatContrat(EtatContrat.SUSPENDU);
			contrat.setStatut(Boolean.FALSE);
			contrat.setObservCtrat(observations);
			contrat = contratPersonnelRepository.save(contrat);
			contratHistoryRepository.save(new ContratHistory(id, TypeOperationContrat.SUSPENSION, "Suspension du contrat" + (observations != null ? " - " + observations : ""), null));
			dto.setRow(contrat);
			dto.setResult("success");
			logger.info(">>>>> Contrat " + id + " suspendu");
		} catch (Exception ex) {
			dto.setResult("failed");
			logger.error(ex.getMessage());
		}
		return dto;
	}

	@Override
	public ContratPersonnelDTO resilierContrat(Long id, String dateFin, MotifClotureContrat motif, String observations) {
		ContratPersonnelDTO dto = new ContratPersonnelDTO();
		try {
			ContratPersonnel contrat = contratPersonnelRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("Contrat not found for id " + id));
			contrat.setDateFin(DateManager.stringToDate(dateFin, "dd/MM/yyyy"));
			contrat.setStatut(false);
			contrat.setEtatContrat(EtatContrat.RESILIE);
			contrat.setMotifCloture(motif);
			contrat.setObservCtrat(observations);
			contrat = contratPersonnelRepository.save(contrat);
			contratHistoryRepository.save(new ContratHistory(id, TypeOperationContrat.RESILIATION, "Résiliation du contrat - Motif: " + motif.getLibelle() + (observations != null ? " - " + observations : ""), null));
			dto.setRow(contrat);
			dto.setResult("success");
			logger.info(">>>>> Contrat " + id + " résilié");
		} catch (Exception ex) {
			dto.setResult("failed");
			logger.error(ex.getMessage());
		}
		return dto;
	}

	@Override
	public ContratPersonnelDTO reprendreContrat(Long id) {
		ContratPersonnelDTO dto = new ContratPersonnelDTO();
		try {
			ContratPersonnel contrat = contratPersonnelRepository.findById(id)
					.orElseThrow(() -> new EntityNotFoundException("Contrat not found for id " + id));
			contrat.setEtatContrat(EtatContrat.ACTIF);
			contrat.setStatut(true);
			contrat = contratPersonnelRepository.save(contrat);
			contratHistoryRepository.save(new ContratHistory(id, TypeOperationContrat.REPRISE, "Reprise du contrat", null));
			dto.setRow(contrat);
			dto.setResult("success");
			logger.info(">>>>> Contrat " + id + " repris");
		} catch (Exception ex) {
			dto.setResult("failed");
			logger.error(ex.getMessage());
		}
		return dto;
	}

	@Override
	public List<ContratPersonnel> findContratsByEtat(EtatContrat etatContrat) {
		return contratPersonnelRepository.findByEtatContrat(etatContrat);
	}

/*	@Override
	public ContratPersonnelDTO renouvelerContrat(Long idAncienContrat, String nouvelleDateDebut, String nouvelleDateFin, String observations) {
		ContratPersonnelDTO dto = new ContratPersonnelDTO();
		try {
			ContratPersonnel ancien = contratPersonnelRepository.findById(idAncienContrat)
					.orElseThrow(() -> new EntityNotFoundException("Contrat not found for id " + idAncienContrat));

			ancien.setStatut(false);
			ancien.setEtatContrat(EtatContrat.TERMINE);
			ancien.setMotifCloture(MotifClotureContrat.FIN_CDD);
			ancien.setObservCtrat("Renouvelé - " + (observations != null ? observations : ""));
			contratPersonnelRepository.save(ancien);

			ContratPersonnel nouveau = new ContratPersonnel();
			nouveau.setPersonnel(ancien.getPersonnel());
			nouveau.setCategorie(ancien.getCategorie());
			nouveau.setFonction(ancien.getFonction());
			nouveau.setTypeContrat(ancien.getTypeContrat());
			nouveau.setDateDebut(DateManager.stringToDate(nouvelleDateDebut, "dd/MM/yyyy"));
			if (nouvelleDateFin != null && !nouvelleDateFin.isEmpty()) {
				nouveau.setDateFin(DateManager.stringToDate(nouvelleDateFin, "dd/MM/yyyy"));
			}
			nouveau.setNetAPayer(ancien.getNetAPayer());
			nouveau.setIndemniteLogement(ancien.getIndemniteLogement());
			nouveau.setIndemniteRepresent(ancien.getIndemniteRepresent());
			nouveau.setIndemniteTransport(ancien.getIndemniteTransport());
			nouveau.setSursalaire(ancien.getSursalaire());
			nouveau.setAncienneteInitial(ancien.getAncienneteInitial());
			nouveau.setStatut(true);
			nouveau.setDepart(false);
			nouveau.setEtatContrat(EtatContrat.ACTIF);
			nouveau.setObservCtrat("Renouvellement du contrat #" + idAncienContrat);
			nouveau = contratPersonnelRepository.save(nouveau);
			contratHistoryRepository.save(new ContratHistory(idAncienContrat, TypeOperationContrat.RENOUVELLEMENT, "Renouvellement vers contrat #" + nouveau.getId(), null));
			contratHistoryRepository.save(new ContratHistory(nouveau.getId(), TypeOperationContrat.CREATION, "Création par renouvellement du contrat #" + idAncienContrat, null));

			dto.setRow(nouveau);
			dto.setResult("success");
			logger.info(">>>>> Contrat {} renouvelé en nouveau contrat {}", idAncienContrat, nouveau.getId());
		} catch (Exception ex) {
			dto.setResult("failed");
			logger.error(ex.getMessage());
		}
		return dto;
	}*/


	@Override
	public ContratPersonnelDTO renouvelerContrat(Long idAncienContrat, String nouvelleDateDebut, String nouvelleDateFin, String observations,String username) {
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();

		try {

			// 1. Vérification de la nouvelle date
			if (nouvelleDateFin == null || nouvelleDateFin.trim().isEmpty()) {
				throw new Exception("La nouvelle date de fin est obligatoire.");
			}

			// 2. Recherche du contrat
			ContratPersonnel contratPersonnel = contratPersonnelRepository.findById(idAncienContrat)
					.orElseThrow(() ->
							new EntityNotFoundException(
									"Contrat non trouvé pour l'id " + idAncienContrat));

			// 3. Vérification de la date de début
			if (contratPersonnel.getDateDebut() == null) {
				throw new Exception(
						"La date de début du contrat est obligatoire.");
			}

			// 4. Conversion des dates
			LocalDate dateDebut = contratPersonnel.getDateDebut()
					.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate();

			LocalDate nouvelleDateFintrt = LocalDate.parse(
					nouvelleDateFin,
					DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			// 5. La date de fin ne peut pas être avant la date de début
			if (nouvelleDateFintrt.isBefore(dateDebut)) {
				throw new Exception(
						"La date de fin ne peut pas être antérieure à la date de début du contrat.");
			}

			// 6. Date maximale = date début + 2 ans
			LocalDate dateFinMax = dateDebut.plusYears(2);

			// 7. Contrôle de la durée maximale
			if (nouvelleDateFintrt.isAfter(dateFinMax)) {
				throw new Exception(
						"La durée du contrat ne peut pas dépasser 2 ans. "
								+ "La date de fin maximale autorisée est le "
								+ dateFinMax.format(
								DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
			}

			// 8. Ancienne date de fin
			java.util.Date ancienneDateFin = contratPersonnel.getDateFin();

			// 9. Conversion LocalDate -> Date
			java.util.Date nouvelleDateFinDate =
					java.util.Date.from(
							nouvelleDateFintrt
									.atStartOfDay(ZoneId.systemDefault())
									.toInstant());

			// 10. Historisation
			ContratDateFinHistorique historique =
					new ContratDateFinHistorique();

			historique.setContratPersonnel(contratPersonnel);
			historique.setAncienneDateFin(ancienneDateFin);
			historique.setNouvelleDateFin(nouvelleDateFinDate);
			historique.setMotif(observations);
			historique.setCreatedBy(username);

			contratDateFinHistoriqueRepository.save(historique);

			// 11. Modification du contrat
			contratPersonnel.setDateFin(nouvelleDateFinDate);

			contratPersonnel =
					contratPersonnelRepository.save(contratPersonnel);

			// 12. Réponse
			contratPersonnelDTO.setRow(contratPersonnel);
			contratPersonnelDTO.setResult("success");

			logger.info(
					">>>>> Date de fin du contrat {} modifiée par {} — "
							+ "ancienne: {} nouvelle: {}",
					contratPersonnel.getId(),
					username,
					ancienneDateFin,
					nouvelleDateFin);

		} catch (Exception ex) {

			contratPersonnelDTO.setResult("failed");
			contratPersonnelDTO.setMessage(ex.getMessage());

			logger.error(
					">>>>> ERREUR SUR MODIFICATION DATE DE FIN CONTRAT",
					ex);
		}

		return contratPersonnelDTO;
	}
	@Override
	public ContratPersonnelDTO creerAvenant(Long idContrat, String nouvelleDateFin, String observations) {
		ContratPersonnelDTO dto = new ContratPersonnelDTO();
		try {
			ContratPersonnel contrat = contratPersonnelRepository.findById(idContrat)
					.orElseThrow(() -> new EntityNotFoundException("Contrat not found for id " + idContrat));

			contrat.setDateFin(DateManager.stringToDate(nouvelleDateFin, "dd/MM/yyyy"));
			String obs = contrat.getObservCtrat();
			contrat.setObservCtrat((obs != null ? obs + " | " : "") + "Avenant: " + (observations != null ? observations : ""));
			contrat = contratPersonnelRepository.save(contrat);
			contratHistoryRepository.save(new ContratHistory(idContrat, TypeOperationContrat.AVENANT, "Avenant - nouvelle date de fin: " + nouvelleDateFin + (observations != null ? " - " + observations : ""), null));

			dto.setRow(contrat);
			dto.setResult("success");
			logger.info(">>>>> Avenant créé pour contrat {}", idContrat);
		} catch (Exception ex) {
			dto.setResult("failed");
			logger.error(ex.getMessage());
		}
		return dto;
	}

	@Override
	public ContratPersonnelDTO departDefinitif(Long contratId, String dateFinEffective) throws Exception {

		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		try{
		ContratPersonnel contrat = contratPersonnelRepository.findById(contratId)
				.orElseThrow(() -> new RuntimeException("Contrat non trouvé"));


		contrat.setStatut(false);
		contrat.setDepart(true);
		contrat.setSoldeCalcule(true);
		contrat.setEtatContrat(EtatContrat.TERMINE);
		contrat.getPersonnel().setStatut(false);
		contrat.getPersonnel().setRetraitEffect(true);
		contrat.setDateFin(Utils.stringToDate(dateFinEffective,"dd/MM/yyyy"));
		personnelRepository.save(contrat.getPersonnel());
			contrat=contratPersonnelRepository.save(contrat);
		contratPersonnelDTO.setRow(contrat);
		contratPersonnelDTO.setResult(true);
		contratPersonnelDTO.setResult("success");
		} catch(Exception ex){
			contratPersonnelDTO.setResult("failed");
			logger.error(ex.getMessage());
			logger.error(new StringBuilder().append(">>>>>  ERREUR SUR FIN CONTRAT PERSONNEL").toString());
			ex.getStackTrace();
		}
		return contratPersonnelDTO;

	}



	@Override
	public ContratPersonnelDTO updateContractSursalaire(Long id,Double sursalaire) {
		// TODO Auto-generated method stub
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		try{
			
			ContratPersonnel contratPersonnel = contratPersonnelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pret not found for id " + id));
			contratPersonnel.setSursalaire(BigDecimal.valueOf(sursalaire));
			//contratPersonnel.setStatut(false);
			contratPersonnel = contratPersonnelRepository.save(contratPersonnel);			
			contratPersonnelDTO.setRow(contratPersonnel);
			contratPersonnelDTO.setResult("success");
			logger.info(new StringBuilder().append(">>>>> ").append(contratPersonnel.toString()).append(" MAJ AVEC SUCCES").toString());
		} catch(Exception ex){
			contratPersonnelDTO.setResult("failed");
			logger.error(ex.getMessage());
			logger.error(new StringBuilder().append(">>>>>  ERREUR SUR FIN CONTRAT PERSONNEL").toString());
			ex.getStackTrace();
		}
		return contratPersonnelDTO;
	}	
	
	

//	@Override
//	public ContratPersonnelDTO loadContratExpieredumois(Long IdTypctr,String  ddeb,String  dfin) {
//		// TODO Auto-generated method stub
//		Date dateDeb;Date dateFin;
//		java.sql.Date dateDebE = null ;
//		java.sql.Date dateFinE = null ;
//		try {
//			dateDeb = DateManager.stringToDate(ddeb, "dd/MM/yyyy");
//			dateFin = DateManager.stringToDate(dfin, "dd/MM/yyyy");
//			dateDebE= new java.sql.Date(dateDeb.getTime());
//			dateFinE= new java.sql.Date(dateFin.getTime());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		//java.sql.Date dateFinE = new java.sql.Date(dateFin.getTime());
//		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
//		Page<ContratPersonnel> page = contratPersonnelRepository.findByTypeContratIdAndDateFinBetween(Pageable pageable, 2L, dateDebE, dateFinE);
//		contratPersonnelDTO.setRows(page.getContent());
//		contratPersonnelDTO.setTotal(page.getTotalElements());
//		logger.info(new StringBuilder().append(">>>>> CONTRATS PERSONNELS CHARGES AVEC SUCCES").toString());
//		return contratPersonnelDTO;
//	}

	public LivreDePaie calculbullFirst(ContratPersonnel ctratpersonnellz, PeriodePaie periodePaieActif){
		
		BigDecimal[]  ancienete=calculAnciennete(ctratpersonnellz.getCategorie().getSalaireDeBase(),ctratpersonnellz.getPersonnel().getDateArrivee());
        BigDecimal newancienete;
		if(ctratpersonnellz.getAncienneteInitial()!=0) {
			 newancienete=ancienete[1].add(BigDecimal.valueOf(ctratpersonnellz.getAncienneteInitial()));
		}else{
			newancienete=ancienete[1];
		}
        BigDecimal anc=newancienete;
        int op = (anc.compareTo(BigDecimal.valueOf(2)) < 0) ? 0
                : (anc.compareTo(BigDecimal.valueOf(25)) <= 0) ? anc.intValue()
                : 25;
		 List<PrimePersonnel> listIndemniteBrut=new ArrayList<PrimePersonnel>();
		 List<PrimePersonnel> listIndemniteNonBrut=new ArrayList<PrimePersonnel>();
		List<PrimePersonnel> listRetenueMutuelle=new ArrayList<PrimePersonnel>();
		List<PrimePersonnel> listRetenueSociale=new ArrayList<PrimePersonnel>();
		List<PrimePersonnel> listGainsNet=new ArrayList<PrimePersonnel>();
		 List<PrimePersonnel> listIndemnite  =new ArrayList<PrimePersonnel>();
		 listIndemnite =  primePersonnelRepository.findByContratPersonnelPersonnelIdAndPeriodePaieId(ctratpersonnellz.getPersonnel().getId(), periodePaieActif.getId());
			if(listIndemnite.size()>0){
				for(PrimePersonnel kprme:listIndemnite){
					 if(kprme.getPrime().getEtatImposition()==1)
					 {
						 listIndemniteBrut.add(kprme);
					 }
					 if(kprme.getPrime().getEtatImposition()==2)
					 {
						 listIndemniteNonBrut.add(kprme);
					 }
					 if(kprme.getPrime().getEtatImposition()==3)
					 {
						 if(kprme.getPrime().getMtExedent()!=null)
						 {listIndemniteNonBrut.add(kprme);
						 listIndemniteNonBrut.add(kprme);}
					 }
					if(kprme.getPrime().getEtatImposition()==4)
					{
						listRetenueMutuelle.add(kprme);
					}
					if(kprme.getPrime().getEtatImposition()==5)
					{
						listGainsNet.add(kprme);
					}
					if(kprme.getPrime().getEtatImposition()==6)
					{
						listRetenueSociale.add(kprme);
					}
				}
				
			} 
			LivreDePaie livrePaiecalpm = new LivreDePaie(ctratpersonnellz.getPersonnel().getMatricule(),ctratpersonnellz.getPersonnel().getNom()+" "+ctratpersonnellz.getPersonnel().getPrenom(), ctratpersonnellz.getPersonnel().getNombrePart(), op, ctratpersonnellz.getCategorie().getSalaireDeBase(),BigDecimal.valueOf(5000), ctratpersonnellz.getIndemniteLogement(),BigDecimal.valueOf(0), BigDecimal.valueOf(0),ctratpersonnellz,null,periodePaieActif,listIndemniteBrut,listIndemniteNonBrut,listRetenueMutuelle,listGainsNet,listRetenueSociale);
			try { 
			 int pi=0;
				while (livrePaiecalpm.getNetPayer()!=ctratpersonnellz.getNetAPayer() || pi==3) {		 				
					 BigDecimal nouvSursal = BigDecimal.ZERO;BigDecimal nouvDiff= BigDecimal.ZERO;BigDecimal nouvMontantBrutImp=BigDecimal.ZERO;
					nouvMontantBrutImp=ctratpersonnellz.getNetAPayer().multiply(livrePaiecalpm.getBrutImposable()).divide(livrePaiecalpm.getNetPayer());
					nouvDiff=nouvMontantBrutImp.subtract(livrePaiecalpm.getBrutImposable());
					nouvSursal=nouvDiff.add(livrePaiecalpm.getSursalaire());
					livrePaiecalpm = new LivreDePaie(ctratpersonnellz.getPersonnel().getMatricule(),ctratpersonnellz.getPersonnel().getNom()+" "+ctratpersonnellz.getPersonnel().getPrenom(), ctratpersonnellz.getPersonnel().getNombrePart(), op, ctratpersonnellz.getCategorie().getSalaireDeBase(),nouvSursal, ctratpersonnellz.getIndemniteLogement(), BigDecimal.valueOf(0), BigDecimal.valueOf(0),ctratpersonnellz,null,periodePaieActif,listIndemniteBrut,listIndemniteNonBrut,listRetenueMutuelle,listGainsNet,listRetenueSociale);
			//	 logger.info("*********************SECOND BULLETIN********************############## SECOND BULLETIN #############-----------"+livrePaiecal.toString());	
					pi=pi+1;
			  }
			
			 
			} catch (Exception e) {
				System.out.println("FINISH"+ e.getMessage());
			} 
			 return livrePaiecalpm;
		}

    public  BigDecimal[] calculAnciennete(BigDecimal salaireCategoriel, Date dateEntree){

        BigDecimal[] tab = new BigDecimal[5];

        BigDecimal anciennete = BigDecimal.valueOf(0) ;


        double age = DifferenceDate.valAge(new Date(), dateEntree);

        int partieEntiere = (int) age;
        int partieApresVirg = (int)((age - partieEntiere) * 12);


        if(age>=2)
            anciennete = salaireCategoriel.multiply(BigDecimal.valueOf(partieEntiere)).divide(BigDecimal.valueOf(100));

        tab[0] = anciennete;


        tab[1] = BigDecimal.valueOf(partieEntiere);
        tab[2] = BigDecimal.valueOf((partieApresVirg));



        return tab;
    }

    @Override
	public ContratPersonnelDTO loadContratExpieredumois(Pageable pageable, Long IdTypctr, String ddeb, String dfin) {
		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();
		try {
			Date dateDeb = DateManager.stringToDate(ddeb, "dd/MM/yyyy");
			Date dateFin = DateManager.stringToDate(dfin, "dd/MM/yyyy");
			java.sql.Date sqlDateDeb = new java.sql.Date(dateDeb.getTime());
			java.sql.Date sqlDateFin = new java.sql.Date(dateFin.getTime());
			
			Page<ContratPersonnel> page = contratPersonnelRepository.findByTypeContratIdAndDateFinBetween(pageable, IdTypctr, sqlDateDeb, sqlDateFin);
			contratPersonnelDTO.setRows(page.getContent());
			contratPersonnelDTO.setTotal(page.getTotalElements());
			logger.info(">>>>> CONTRATS EXPIRANTS DU MOIS CHARGES AVEC SUCCES");
		} catch (Exception ex) {
			logger.error(">>>>> ERREUR SUR CHARGEMENT CONTRATS EXPIRANTS: " + ex.getMessage());
			ex.printStackTrace();
		}
		return contratPersonnelDTO;
	}


	@Override
	@Transactional
	public ContratPersonnelDTO modifierDateFinContrat(
			Long id,
			String nouvelleDateFin,
			String motif,
			String username) {

		ContratPersonnelDTO contratPersonnelDTO = new ContratPersonnelDTO();

		try {

			// 1. Vérification de la nouvelle date
			if (nouvelleDateFin == null || nouvelleDateFin.trim().isEmpty()) {
				throw new Exception("La nouvelle date de fin est obligatoire.");
			}

			// 2. Recherche du contrat
			ContratPersonnel contratPersonnel = contratPersonnelRepository.findById(id)
					.orElseThrow(() ->
							new EntityNotFoundException(
									"Contrat non trouvé pour l'id " + id));

			// 3. Vérification de la date de début
			if (contratPersonnel.getDateDebut() == null) {
				throw new Exception(
						"La date de début du contrat est obligatoire.");
			}

			// 4. Conversion des dates
			LocalDate dateDebut = contratPersonnel.getDateDebut()
					.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDate();

			LocalDate nouvelleDateFintrt = LocalDate.parse(
					nouvelleDateFin,
					DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			// 5. La date de fin ne peut pas être avant la date de début
			if (nouvelleDateFintrt.isBefore(dateDebut)) {
				throw new Exception(
						"La date de fin ne peut pas être antérieure à la date de début du contrat.");
			}

			// 6. Date maximale = date début + 2 ans
			LocalDate dateFinMax = dateDebut.plusYears(2);

			// 7. Contrôle de la durée maximale
			if (nouvelleDateFintrt.isAfter(dateFinMax)) {
				throw new Exception(
						"La durée du contrat ne peut pas dépasser 2 ans. "
								+ "La date de fin maximale autorisée est le "
								+ dateFinMax.format(
								DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".");
			}

			// 8. Ancienne date de fin
			java.util.Date ancienneDateFin = contratPersonnel.getDateFin();

			// 9. Conversion LocalDate -> Date
			java.util.Date nouvelleDateFinDate =
					java.util.Date.from(
							nouvelleDateFintrt
									.atStartOfDay(ZoneId.systemDefault())
									.toInstant());

			// 10. Historisation
			ContratDateFinHistorique historique =
					new ContratDateFinHistorique();

			historique.setContratPersonnel(contratPersonnel);
			historique.setAncienneDateFin(ancienneDateFin);
			historique.setNouvelleDateFin(nouvelleDateFinDate);
			historique.setMotif(motif);
			historique.setCreatedBy(username);

			contratDateFinHistoriqueRepository.save(historique);

			// 11. Modification du contrat
			contratPersonnel.setDateFin(nouvelleDateFinDate);

			contratPersonnel =
					contratPersonnelRepository.save(contratPersonnel);

			// 12. Réponse
			contratPersonnelDTO.setRow(contratPersonnel);
			contratPersonnelDTO.setResult("success");

			logger.info(
					">>>>> Date de fin du contrat {} modifiée par {} — "
							+ "ancienne: {} nouvelle: {}",
					contratPersonnel.getId(),
					username,
					ancienneDateFin,
					nouvelleDateFin);

		} catch (Exception ex) {

			contratPersonnelDTO.setResult("failed");
			contratPersonnelDTO.setMessage(ex.getMessage());

			logger.error(
					">>>>> ERREUR SUR MODIFICATION DATE DE FIN CONTRAT",
					ex);
		}

		return contratPersonnelDTO;
	}


}

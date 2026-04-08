package com.nectux.mizan.hyban.paie.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.nectux.mizan.hyban.paie.dto.*;
import com.nectux.mizan.hyban.parametrages.entity.PeriodePaie;
import com.nectux.mizan.hyban.paie.entity.BulletinPaie;
import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;
import com.nectux.mizan.hyban.personnel.entity.Personnel;

import com.nectux.mizan.hyban.utils.PrintLs;
import org.springframework.data.domain.Pageable;

public interface BulletinPaieService {
	
	public BulletinPaie save(BulletinPaie bulletinPaie);

    //@SuppressWarnings({ "unused", "null" })
    LivreDePaieDTO  genererMois1(Pageable pageable, Long idPeriode);

    //	public LivreDePaieDTO   genererMois1(Pageable pageable,Long idPeriode);
	public LivreDePaieDTO   genererOptimiseMois1(Pageable pageable,Long idPeriode);
	public BulletinPaieDTO generateLivreDePaie(Pageable pageable);
	
//	public BulletinPaieDTO generateBulletinDePaie(List<LivreDePaie> monlivre);
//	public List<BulletinPaie>   genererMois1Personnel(Long idPeriode,Long idPersonnel);
	public List<BulletinPaie>   rechercherBulletinMois(PeriodePaie periodePaie);
	public List<BulletinPaie>   rechercherBulletinMoisCalculer(PeriodePaie periodePaie,boolean etat);
   // public LivreDePaieDTOV2 genererOptimiseMoisVersion2(Pageable pageable, Long idPeriode);
	public List<BulletinPaie>   rechercherBulletinAnneeCalculer(Long idanne,boolean etat);
	public List<BulletinPaie>   rechercherBulletinAnnuel(Long anneeId,Long Idpers);
	public List<BulletinPaie>   rechercherBulletinAnnuelglobal(Long anneeId);
	public List<BulletinPaie>   findBulletinByPeriodePaie(Long idPeriode);
	public BulletinPaieDTO   BulletinMoisCalculer(Pageable pageable,PeriodePaie periodePaie);
	public Boolean delete(Long id);
	public int count();
	public BulletinPaie findBulletinByPeriodePaieAndPersonnel(PeriodePaie periodePaie, Personnel personnel);
	public BulletinPaieDTO loadBulletinPaie(Pageable pageable,PeriodePaie maperiode);
	public BulletinPaieDTO loadBulletinPaiechearch(Pageable pageable,String valeurarechercher);
	//public BulletinPaieDTO loadBulletinPaie(Pageable pageable,PeriodePaie maperiode, String search);
	public BulletinPaieDTO loadBulletinPaie(Pageable pageable,PeriodePaie maperiode, String search);
    public BulletinSpecialeDTO BulletinMoisSpecialeCalculer(Pageable pageable, PeriodePaie periodePaie);
    public BulletinSpecialeDTO generateEmployeLivreDePaie(Pageable pageable);
    public LivreDePaieSpecialeDTO genererOptimiseEmployeeMois1(Pageable pageable, Long idPeriode);

    public BulletinSpecialeDTO loadBulletinSpeciale(Pageable pageable,PeriodePaie maperiode, String search);
    public BulletinSpecialeDTO loadBulletinSpeciale(Pageable pageable,PeriodePaie maperiode);
	//BulletinPaieDTO findAllfilter(Map<String,String> filters, Pageable pageable);

	public BigDecimal salaireMoyenMensuel(ContratPersonnel contratPersonnel);

	public BigDecimal indemniteMoyenMensuel(ContratPersonnel contratPersonnel);
	
	public List<BulletinPaie>   findBulletinByPeriodePaieContract(Long idPeriode);
	
	//public List<BulletinPaie> findAllBulletinByPeriodePaieForLivrePaieforBanque(Long idPeriodePaie) ;
	
	public BigDecimal[]  MasseSalarialdeLexo(PeriodePaie maperiode);
	public BigDecimal  MasseSalarialMois(PeriodePaie maperiode);
	public List<BulletinPaie> findAllBulletinByvirementforBanque(Long idPeriodePaie, Long idBanque);
	
	public BigDecimal MensuelCn(PeriodePaie maperiode);
	
	public BigDecimal MensuelIgr(PeriodePaie maperiode);
	
	public BigDecimal MensuelBrut(PeriodePaie maperiode);
	public BigDecimal[]  MensuelBaseCnpsSup(PeriodePaie maperiode);
	
	public BigDecimal[]  MensuelBaseCnpsSup70000(PeriodePaie maperiode);
	
	
	public BigDecimal MensuelBrutImposable(PeriodePaie maperiode);
	
	
	public BigDecimal MensuelIs(PeriodePaie maperiode);
	
	
	public BigDecimal MensuelJourtravail(PeriodePaie maperiode);
	
	public BigDecimal MensuelIgrPatron(PeriodePaie maperiode);
	
	public Integer Nbrebulletin(Long idcontratPersonnel);
	
	
	public BigDecimal MensuelCnAnne(Long anneeId);
	
	public BigDecimal MensuelIgrAnne(Long anneeId);
	
	public BigDecimal MensuelBrutAnne(Long anneeId);
	
	
	public BigDecimal MensuelIsAnne(Long anneeId);
	
	public BigDecimal MensuelIgrPatronAnne(Long anneeId);
	
	
	public BigDecimal MensuelBrutImpAnne(Long anneeId);

	public BigDecimal[] MensuelBaseCnpsInf(PeriodePaie periodePaie);

	public List<PrintLs> calculerMasseSalarialeParTypeContrat(PeriodePaie periode);
	public List<PrintLs> calculerMasseSalarialeParSite(PeriodePaie periode);
	public List<PrintLs> calculerEffectifParSiteAlaPaie(PeriodePaie periode);
	public BulletinPaieDTO getCurrentYearBulletins(Long personnelId,String annee) ;

	BulletinPaieDTO findbulletin(Long payrollId);

	BulletinPaieDTO loadBulletinPaieSearch(Pageable page, String criteria);
	BulletinPaieDTO finImprimbulletin(Long payrollId);


}

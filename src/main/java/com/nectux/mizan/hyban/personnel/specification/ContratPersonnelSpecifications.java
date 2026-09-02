package com.nectux.mizan.hyban.personnel.specification;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.nectux.mizan.hyban.personnel.entity.ContratPersonnel;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

/**
 * Fabriques de {@link Specification} pour {@link ContratPersonnel}.
 *
 * <p>Chaque methode exprime UN critere metier et un seul. Les appelants composent
 * ce dont ils ont besoin via {@link Specification#allOf}. Il n'y a volontairement
 * aucun socle impose : contrairement au module Personnel, une requete sur les
 * contrats peut legitimement viser des contrats vivants (onglet actifs) comme
 * des contrats morts (onglets clotures / expires). Imposer un
 * {@code statut = true} par defaut viderait ces derniers.</p>
 *
 * <p>Rappel sur les dates : {@code dateFin} est nullable et vaut NULL pour les CDI.
 * En SQL toute comparaison avec NULL renvoie NULL, donc un CDI est ecarte par tout
 * predicat de date. C'est le comportement voulu pour les criteres d'echeance (un CDI
 * n'arrive jamais a echeance) mais pas pour {@link #enCoursA(LocalDate)}, qui inclut
 * donc explicitement les dates nulles.</p>
 */
public final class ContratPersonnelSpecifications {

	private ContratPersonnelSpecifications() {
		// classe utilitaire
	}

	// ------------------------------------------------------------------
	// Cycle de vie du contrat
	// ------------------------------------------------------------------

	/** Contrat vivant : {@code statut = true AND depart = false}. */
	public static Specification<ContratPersonnel> actif() {
		return (root, query, cb) -> cb.and(
			cb.isTrue(root.get("statut")),
			cb.isFalse(root.get("depart"))
		);
	}

	/** Contrat cloture : {@code statut = false AND depart = true}. */
	public static Specification<ContratPersonnel> clos() {
		return (root, query, cb) -> cb.and(
			cb.isFalse(root.get("statut")),
			cb.isTrue(root.get("depart"))
		);
	}

	/** Contrat suspendu : {@code statut = false} (depart indifferents). */
	public static Specification<ContratPersonnel> suspendu() {
		return (root, query, cb) -> cb.isFalse(root.get("statut"));
	}

	/** Type de contrat different de CDI (inclut les contrats sans type). */
	public static Specification<ContratPersonnel> nonCDI() {
		return (root, query, cb) -> cb.or(
			cb.isNull(libelleTypeContrat(root, cb)),
			cb.notEqual(libelleTypeContrat(root, cb), "cdi")
		);
	}

	/** Date de fin dans le futur ou aujourd'hui (exclut les dateFin nulles). */
	public static Specification<ContratPersonnel> echeanceFuture(LocalDate date) {
		Timestamp borne = debutDeJournee(date);
		return (root, query, cb) -> cb.greaterThanOrEqualTo(dateFin(root), borne);
	}

	/** Exclut les contrats des agents sortis de l'effectif. */
	public static Specification<ContratPersonnel> personnelNonRetire() {
		return (root, query, cb) -> cb.isFalse(root.get("personnel").get("retraitEffect"));
	}

	// ------------------------------------------------------------------
	// Recherche textuelle
	// ------------------------------------------------------------------

	/**
	 * Recherche insensible a la casse sur nom, prenom et matricule de l'agent, ainsi que
	 * sur les libelles de type de contrat et de fonction.
	 *
	 * <p>Le perimetre couvre volontairement les memes champs que la recherche de l'IHM
	 * (cf. {@code applyCurrentContractsFilters}), sans quoi une recherche sur "CDI" ou sur
	 * un intitule de poste ne renverrait rien des lors que le filtrage est delegue au serveur.</p>
	 *
	 * <p>Les jointures sur {@code typeContrat} et {@code fonction} sont en LEFT : une jointure
	 * interne ecarterait les contrats dont l'association est nulle, alors qu'ils doivent
	 * rester trouvables par le nom de l'agent.</p>
	 */
	public static Specification<ContratPersonnel> recherche(String search) {
		return (root, query, cb) -> {
			String motif = "%" + search.toLowerCase() + "%";
			Path<Object> agent = root.get("personnel");
			Path<Object> type = root.join("typeContrat", JoinType.LEFT);
			Path<Object> fonction = root.join("fonction", JoinType.LEFT);
			return cb.or(
				cb.like(cb.lower(agent.get("nom")), motif),
				cb.like(cb.lower(agent.get("prenom")), motif),
				cb.like(cb.lower(agent.get("matricule")), motif),
				cb.like(cb.lower(type.get("libelle")), motif),
				cb.like(cb.lower(fonction.get("libelle")), motif)
			);
		};
	}

	// ------------------------------------------------------------------
	// Attributs du contrat
	// ------------------------------------------------------------------

	/**
	 * Restreint aux libelles de type de contrat fournis.
	 *
	 * <p>La comparaison est faite sur les valeurs mises en minuscules et debarrassees de
	 * leurs espaces : en base, la plupart des libelles portent un espace de fin
	 * ({@code 'CDI '}, {@code 'CDD '}, {@code 'Consultance '}...), si bien qu'une egalite
	 * stricte sur {@code "CDI"} ne remontait aucune ligne.</p>
	 */
	public static Specification<ContratPersonnel> typeContratParmi(Collection<String> libelles) {
		List<String> normalises = libelles.stream()
			.filter(java.util.Objects::nonNull)
			.map(ContratPersonnelSpecifications::normaliser)
			.collect(Collectors.toList());
		return (root, query, cb) -> libelleTypeContrat(root, cb).in(normalises);
	}

	/** Restreint a un libelle de type de contrat. Voir {@link #typeContratParmi} pour la normalisation. */
	public static Specification<ContratPersonnel> typeContratEgal(String libelle) {
		String normalise = normaliser(libelle);
		return (root, query, cb) -> cb.equal(libelleTypeContrat(root, cb), normalise);
	}

	private static Expression<String> libelleTypeContrat(Root<ContratPersonnel> root, CriteriaBuilder cb) {
		return cb.lower(cb.trim(root.get("typeContrat").get("libelle")));
	}

	private static String normaliser(String valeur) {
		return valeur.trim().toLowerCase();
	}

	/** Etat contractuel de l'agent (CAREC). */
	public static Specification<ContratPersonnel> carecEgal(boolean valeur) {
		return (root, query, cb) -> cb.equal(root.get("personnel").get("carec"), valeur);
	}

	/**
	 * Tranche de salaire categoriel. Renvoie {@code null} si la tranche est inconnue,
	 * ce qui neutralise le critere sans fausser la requete.
	 */
	public static Specification<ContratPersonnel> trancheSalaire(String tranche) {
		switch (tranche) {
			case "low":
				return salaireStrictementInferieurA(new BigDecimal("100000"));
			case "medium":
				return salaireEntre(new BigDecimal("100000"), new BigDecimal("200000"));
			case "high":
				return salaireEntre(new BigDecimal("200000"), new BigDecimal("350000"));
			case "veryhigh":
				return salaireAuMoins(new BigDecimal("350000"));
			default:
				return null;
		}
	}

	private static Specification<ContratPersonnel> salaireStrictementInferieurA(BigDecimal plafond) {
		return (root, query, cb) -> cb.lessThan(salaire(root), plafond);
	}

	private static Specification<ContratPersonnel> salaireEntre(BigDecimal min, BigDecimal max) {
		return (root, query, cb) -> cb.between(salaire(root), min, max);
	}

	private static Specification<ContratPersonnel> salaireAuMoins(BigDecimal plancher) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(salaire(root), plancher);
	}

	private static Path<BigDecimal> salaire(jakarta.persistence.criteria.Root<ContratPersonnel> root) {
		return root.get("categorie").get("salaireDeBase");
	}

	// ------------------------------------------------------------------
	// Criteres de date
	// ------------------------------------------------------------------

	/**
	 * Contrat encore en cours a la date donnee : {@code dateFin >= date OU dateFin IS NULL}.
	 * Les CDI (dateFin nulle) sont inclus car ils n'expirent jamais.
	 */
	public static Specification<ContratPersonnel> enCoursA(LocalDate date) {
		Timestamp borne = debutDeJournee(date);
		return (root, query, cb) -> cb.or(
			cb.greaterThanOrEqualTo(dateFin(root), borne),
			cb.isNull(dateFin(root))
		);
	}

	/**
	 * Contrat encore en vigueur a la date donnee, au sens metier retenu par l'IHM :
	 * {@code CDI OU dateFin IS NULL OU dateFin >= date}.
	 *
	 * <p>Un CDI n'expire jamais, meme si une {@code dateFin} a ete saisie par erreur ou
	 * pour une autre raison. Sans cette clause, les contrats a duree determinee dont la
	 * date de fin est depassee mais dont le statut n'a pas encore ete cloture
	 * remonteraient parmi les contrats en cours.</p>
	 */
	public static Specification<ContratPersonnel> enVigueurA(LocalDate date) {
		return enCoursA(date).or(typeContratEgal("CDI"));
	}

	/** Contrat echu avant la date donnee. Exclut les CDI (dateFin nulle). */
	public static Specification<ContratPersonnel> echuAvant(LocalDate date) {
		Timestamp borne = debutDeJournee(date);
		return (root, query, cb) -> cb.lessThan(dateFin(root), borne);
	}

	/** Contrat arrivant a echeance exactement a la date donnee. Exclut les CDI. */
	public static Specification<ContratPersonnel> echeanceLe(LocalDate date) {
		Timestamp borne = debutDeJournee(date);
		return (root, query, cb) -> cb.equal(dateFin(root), borne);
	}

	/** Contrat arrivant a echeance au plus tard a la date donnee (incluse). Exclut les CDI. */
	public static Specification<ContratPersonnel> echeanceAuPlusTard(LocalDate date) {
		Timestamp borne = finDeJournee(date);
		return (root, query, cb) -> cb.lessThanOrEqualTo(dateFin(root), borne);
	}

	/** Contrat arrivant a echeance dans la periode donnee. Exclut les CDI. */
	public static Specification<ContratPersonnel> echeanceEntre(LocalDate debut, LocalDate fin) {
		Timestamp borneDebut = debutDeJournee(debut);
		Timestamp borneFin = debutDeJournee(fin);
		return (root, query, cb) -> cb.between(dateFin(root), borneDebut, borneFin);
	}

	private static Path<Date> dateFin(jakarta.persistence.criteria.Root<ContratPersonnel> root) {
		return root.get("dateFin");
	}

	private static Timestamp debutDeJournee(LocalDate date) {
		return Timestamp.valueOf(date.atStartOfDay());
	}

	private static Timestamp finDeJournee(LocalDate date) {
		return Timestamp.valueOf(date.atTime(23, 59, 59));
	}
}

package com.nectux.mizan.hyban.personnel.entity;

import jakarta.persistence.*;

import com.nectux.mizan.hyban.parametrages.entity.Auditable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Entity
@Component("fonction")
@Scope("prototype")
@Table(name="CGECI_RHPAIE_FONCTION")
@SequenceGenerator(name="CGECI_RHPAIE_FONCTION_SEQUENCE", sequenceName="CGECI_RHPAIE_FONCTION_SEQ", initialValue=1, allocationSize=1)
public class Fonction extends Auditable {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CGECI_RHPAIE_FONCTION_SEQUENCE")
	@Column(unique=true, nullable=false)
	private Long id;
	
	@Column(unique=true, nullable=false)
	private String libelle;
	
	@Column(length=500)
	private String description;
	


	
	@Column(length=10)
	private String niveau;
	
	@Column(length=100)
	private String departement;
	
	@Column(length=20)
	private String statut;

	public Fonction() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public String getNiveau() {
		return niveau;
	}

	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}

	public String getDepartement() {
		return departement;
	}

	public void setDepartement(String departement) {
		this.departement = departement;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	@Override
	public String toString() {
		return "Fonction [id=" + id + ", libelle=" + libelle + ", description=" + description + 
			   ", niveau=" + niveau +
			   ", departement=" + departement + ", statut=" + statut + "]";
	}
}

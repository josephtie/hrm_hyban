package com.nectux.mizan.hyban.paie.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import jakarta.persistence.Transient;

import com.nectux.mizan.hyban.utils.Utils;


public class ImprimBulletinPaie implements Serializable {
	private static final long serialVersionUID = 1L;

	private String libelle;
	
	private String code;
	
	private BigDecimal gain;
	@Transient
	private String mtgain;
	
	private BigDecimal retenue;
	@Transient
	private String mtretenue;
	
	private BigDecimal base;
	@Transient
	private String mtbase;
	
	private BigDecimal taux;
	
	private BigDecimal gainPatron;
	@Transient
	private String mtgainPatron;
	
	private BigDecimal retenuePatron;
	@Transient
	private String mtretenuePatron;
	
	private BigDecimal tauxPatron;
	
	public ImprimBulletinPaie() {
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public BigDecimal getGain() {
		return gain;
	}

	public void setGain(BigDecimal gain) {
		this.gain = gain;
	}

	public BigDecimal getRetenue() {
		return retenue;
	}

	public void setRetenue(BigDecimal retenue) {
		this.retenue = retenue;
	}

	public BigDecimal getBase() {
		return base;
	}

	public void setBase(BigDecimal base) {
		this.base = base;
	}

	public BigDecimal getTaux() {
		return taux;
	}

	public void setTaux(BigDecimal taux) {
		this.taux = taux;
	}

	public BigDecimal getGainPatron() {
		return gainPatron;
	}

	public void setGainPatron(BigDecimal gainPatron) {
		this.gainPatron = gainPatron;
	}

	public BigDecimal getRetenuePatron() {
		return retenuePatron;
	}

	public void setRetenuePatron(BigDecimal retenuePatron) {
		this.retenuePatron = retenuePatron;
	}

	public BigDecimal getTauxPatron() {
		return tauxPatron;
	}

	public void setTauxPatron(BigDecimal tauxPatron) {
		this.tauxPatron = tauxPatron;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMtgain() {
		
		return mtgain= Utils.formattingAmount(gain);
	}

	public void setMtgain(String mtgain) {
		this.mtgain = mtgain;
	}

	public String getMtretenue() {
		
		return mtretenue= Utils.formattingAmount(retenue);
	}

	public void setMtretenue(String mtretenue) {
		this.mtretenue = mtretenue;
	}

	public String getMtbase() {
		
		return mtbase= Utils.formattingAmount(base);
	}

	public void setMtbase(String mtbase) {
		this.mtbase = mtbase;
	}

	public String getMtgainPatron() {
		
		return mtgainPatron= Utils.formattingAmount(gainPatron);
		
	}

	public void setMtgainPatron(String mtgainPatron) {
		this.mtgainPatron = mtgainPatron;
	}

	public String getMtretenuePatron() {
		
		return mtretenuePatron= Utils.formattingAmount(retenuePatron);
	}

	public void setMtretenuePatron(String mtretenuePatron) {
		this.mtretenuePatron = mtretenuePatron;
	}


	public ImprimBulletinPaie(String libelle, BigDecimal base, BigDecimal gain, BigDecimal taux) {
		this.libelle = libelle;
		this.base = base;
		this.gain = gain;
		this.taux = taux;
	}


}
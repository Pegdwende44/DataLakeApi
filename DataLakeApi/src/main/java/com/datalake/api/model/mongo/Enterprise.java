package com.datalake.api.model.mongo;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//Linking to the collection
@Document(collection="structured_data") 
public class Enterprise {
	
	@Id
	private String id;
	
	private String entreprise;
	
	private String cible;
	
	private String ville;
	
	private String codePostal;
	
	private String taille;
	
	private int effectif;
	
	private String tailleEffectif;
	
	private String codeNaf;
	
	private int scoreDigital;
	
	private int scoreServiciel;
	
	private int scoreRelationel;
	
	private float proportionServices;
	
	private int nombreServices;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(String entreprise) {
		this.entreprise = entreprise;
	}

	public String getCible() {
		return cible;
	}

	public void setCible(String cible) {
		this.cible = cible;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getTaille() {
		return taille;
	}

	public void setTaille(String taille) {
		this.taille = taille;
	}

	public int getEffectif() {
		return effectif;
	}

	public void setEffectif(int effectif) {
		this.effectif = effectif;
	}

	public String getTailleEffectif() {
		return tailleEffectif;
	}

	public void setTailleEffectif(String tailleEffectif) {
		this.tailleEffectif = tailleEffectif;
	}

	public String getCodeNaf() {
		return codeNaf;
	}

	public void setCodeNaf(String codeNaf) {
		this.codeNaf = codeNaf;
	}

	public int getScoreDigital() {
		return scoreDigital;
	}

	public void setScoreDigital(int scoreDigital) {
		this.scoreDigital = scoreDigital;
	}

	public int getScoreServiciel() {
		return scoreServiciel;
	}

	public void setScoreServiciel(int scoreServiciel) {
		this.scoreServiciel = scoreServiciel;
	}

	public int getScoreRelationel() {
		return scoreRelationel;
	}

	public void setScoreRelationel(int scoreRelationel) {
		this.scoreRelationel = scoreRelationel;
	}

	public float getProportionServices() {
		return proportionServices;
	}

	public void setProportionServices(float proportionServices) {
		this.proportionServices = proportionServices;
	}

	public int getNombreServices() {
		return nombreServices;
	}

	public void setNombreServices(int nombreServices) {
		this.nombreServices = nombreServices;
	}
	
	
	
}

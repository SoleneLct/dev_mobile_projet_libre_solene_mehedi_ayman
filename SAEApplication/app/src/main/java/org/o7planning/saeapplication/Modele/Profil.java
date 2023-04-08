package org.o7planning.saeapplication.Modele;

import java.io.Serializable;

public class Profil implements Serializable {

    private int id;
    private String nom;
    private String prenom;
    private String pseudo;
    private String mot_de_passe;

    public Profil(String pseudo ,String nom, String prenom, String mot_de_passe) {
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.mot_de_passe = mot_de_passe;
    }
    public Profil(int id,String pseudo ,String nom, String prenom, String mot_de_passe) {
        this.id = id;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.mot_de_passe = mot_de_passe;
    }
    public int getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getMot_de_passe() {
        return mot_de_passe;
    }
    public String getPrenom() {
        return prenom;
    }

    public String getPseudo() {
        return pseudo;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}

package org.o7planning.saeapplication.Modele;

public class Image {

    private int id;
    private String nom;

    public void setId_profils(int id_profils) {
        this.id_profils = id_profils;
    }
    private byte[] image;
    private int id_profils;
    private int id_table;

    public int getId_table() {
        return id_table;
    }

    public void setId_table(int id_table) {
        this.id_table = id_table;
    }

    public Image(int id, String nom,int id_table, int id_profils) {
        this.id = id;
        this.nom = nom;
        this.id_profils = id_profils;
        this.id_table = id_table;
    }
    public Image(int id, String nom, byte[] image, int id_table, int id_profil) {
        this.id = id;
        this.nom = nom;
        this.image = image;
        this.id_profils = id_profils;
        this.id_table = id_table;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public byte[] getImage() {
        return image;
    }

    public int getId_profils() {
        return id_profils;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}

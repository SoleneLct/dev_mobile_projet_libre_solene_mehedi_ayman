package org.o7planning.saeapplication.Modele;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Image {

    private int mId;
    private String mNom;
    private byte[] mImage;
    private int mIdProfils;
    private int mIdTable;

    public Image(String nom, byte[] image, int mIdTable, int mIdProfils) {
        this.mNom = nom;
        this.mImage = image;
        this.mIdProfils = mIdProfils;
        this.mIdTable = mIdTable;
    }
    public Image(int id, String nom, byte[] image, int mIdTable, int id_profil) {
        this.mId = id;
        this.mNom = nom;
        this.mImage = image;
        this.mIdProfils = id_profil;
        this.mIdTable = mIdTable;
    }

    public int getId() {
        return mId;
    }

    public String getNom() {
        return mNom;
    }

    public byte[] getImage() {
        return mImage;
    }
    public int getIdTable() {
        return mIdTable;
    }
    public int getIdProfils() {
        return mIdProfils;
    }

    public void setId(int id) {
        this.mId = id;
    }
    public void setNom(String nom) {
        this.mNom = nom;
    }
    public void setImage(byte[] image) {
        this.mImage = image;
    }
    public void setIdProfils(int idProfils) {
        this.mIdProfils = idProfils;
    }
    public void setIdTable(int idTable) {
        this.mIdTable = idTable;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


}

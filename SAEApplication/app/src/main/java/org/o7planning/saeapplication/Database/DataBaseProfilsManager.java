package org.o7planning.saeapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import org.o7planning.saeapplication.Modele.Profil;

import java.util.ArrayList;
import java.util.List;
public class DataBaseProfilsManager extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "ProjectManager";
    // Table name: Profils.
    public static final String TABLE_PROFILS = "Profils";
    public static final String COLUMN_PROFILS_ID ="Profils_Id";
    public static final String COLUMN_PROFILS_PSEUDO ="Profils_Pseudo";
    public static final String COLUMN_PROFILS_NOM ="Profils_Nom";
    public static final String COLUMN_PROFILS_MPD ="Profils_Mot_de_Passe";
    public static final String COLUMN_PROFILS_PRENOM ="Profils_Prenom";

    public DataBaseProfilsManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_PROFILS + "("
                + COLUMN_PROFILS_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PROFILS_PSEUDO + " TEXT,"
                + COLUMN_PROFILS_NOM + " TEXT,"
                + COLUMN_PROFILS_PRENOM + " TEXT,"
                + COLUMN_PROFILS_MPD + " TEXT"
                + ")";
        // Execute Script.
        db.execSQL(script);
        defaultUser();
    }
    public void defaultUser(){
        if(this.getProfilsByNameAndMdp("user","12345")==null){
            Profil user = new Profil("user","userName","userFirstName","12345");
            this.addProfils(user);
        }
        if(this.getProfilsByNameAndMdp("admin","admin12345")==null){
            Profil admin = new Profil("admin","admin1","admin","admin12345");
            this.addProfils(admin);
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILS);
        // Create tables again
        onCreate(db);
    }

    public void addProfils(Profil profil) {
        Log.i(TAG, "MyDatabaseHelper.addProfil ... " + profil.getNom());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILS_PSEUDO, profil.getPseudo());
        values.put(COLUMN_PROFILS_PRENOM, profil.getPrenom());
        values.put(COLUMN_PROFILS_NOM, profil.getNom());
        values.put(COLUMN_PROFILS_MPD, profil.getMot_de_passe());
        // Inserting Row
        db.insert(TABLE_PROFILS, null, values);
        // Closing database connection
        db.close();
    }
    public Profil getProfils(int id) {
        Log.i(TAG, "MyDatabaseHelper.getProfil ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Profil profil = null;
        Cursor cursor = db.query(TABLE_PROFILS,
                        new String[] { COLUMN_PROFILS_ID,
                        COLUMN_PROFILS_PSEUDO,
                        COLUMN_PROFILS_NOM,
                        COLUMN_PROFILS_PRENOM,
                        COLUMN_PROFILS_MPD},
                COLUMN_PROFILS_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()){
            profil = new Profil(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
        }
        // return profil
        return profil;
    }
    public Profil getProfilsByNameAndMdp(String pseudo ,String mdp) {
        Log.i(TAG, "MyDatabaseHelper.getProfilsByNameAndMdp ... " + pseudo+" and " +mdp);
        SQLiteDatabase db = this.getReadableDatabase();
        Profil profil = null;
        Cursor cursor = db.query(TABLE_PROFILS,
                new String[] { COLUMN_PROFILS_ID,
                        COLUMN_PROFILS_PSEUDO,
                        COLUMN_PROFILS_NOM,
                        COLUMN_PROFILS_PRENOM,
                        COLUMN_PROFILS_MPD},
                COLUMN_PROFILS_PSEUDO + "=? and " + COLUMN_PROFILS_MPD + "=?",
                new String[] { pseudo, mdp }, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) { // Vérification du curseur
            profil = new Profil(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
        }
        cursor.close();
        // return profil
        return profil;
    }
    public List<Profil> getAllProfils() {
        Log.i(TAG, "MyDatabaseHelper.getAllProfils ... " );
        List<Profil> list = new ArrayList<Profil>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PROFILS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Profil profil = profil = new Profil(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));;
                // Adding profil to list
                list.add(profil);
            } while (cursor.moveToNext());
        }
        // return profil list
        return list;
    }
    public int getProfilsCount() {
        Log.i(TAG, "MyDatabaseHelper.getProfilsCount ... " );
        String countQuery = "SELECT * FROM " + TABLE_PROFILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    public int updateProfil(Profil profil) {
        Log.i(TAG, "MyDatabaseHelper.updateProfil ... " + profil.getNom());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PROFILS_PSEUDO, profil.getPseudo());
        values.put(COLUMN_PROFILS_PRENOM, profil.getPrenom());
        values.put(COLUMN_PROFILS_NOM, profil.getNom());
        values.put(COLUMN_PROFILS_MPD, profil.getMot_de_passe());
        // updating row
        return db.update(TABLE_PROFILS, values, COLUMN_PROFILS_ID + " = ?",
                new String[]{String.valueOf(profil.getId())});
    }
    public void deleteProfil(Profil profil,Context context) {
        Log.i(TAG, "MyDatabaseHelper.deleteProfil ... " + profil.getNom() );
        new DataBaseImageManager(context).deleteAllImageByUserId(profil.getId());
        new DataBaseFolderImageManager(context).deleteAllFolderByUserId(profil.getId(),context);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILS, COLUMN_PROFILS_ID + " = ?",
                new String[] { String.valueOf(profil.getId()) });
        db.close();
    }
}
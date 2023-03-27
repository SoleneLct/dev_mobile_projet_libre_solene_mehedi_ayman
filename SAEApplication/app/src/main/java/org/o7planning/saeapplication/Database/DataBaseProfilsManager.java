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
    private static final String DATABASE_NAME = "Profils_Manager";
    // Table name: Profils.
    private static final String TABLE_PROFILS = "Profils";
    private static final String COLUMN_PROFILS_ID ="Profils_Id";
    private static final String COLUMN_PROFILS_NOM ="Profils_Nom";
    private static final String COLUMN_PROFILS_MPD ="Profils_Mot_de_Passe";
    private static final String COLUMN_PROFILS_PRENOM ="Profils_Prenom";

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
                + COLUMN_PROFILS_NOM + " TEXT,"
                + COLUMN_PROFILS_PRENOM + " TEXT,"
                + COLUMN_PROFILS_MPD + " TEXT"
                + ")";
        // Execute Script.
        db.execSQL(script);
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
        values.put(COLUMN_PROFILS_NOM, profil.getNom());
        values.put(COLUMN_PROFILS_PRENOM, profil.getPrenom());
        values.put(COLUMN_PROFILS_MPD, profil.getMot_de_passe());

        // Inserting Row
        db.insert(TABLE_PROFILS, null, values);
        // Closing database connection
        db.close();
    }
    public Profil getProfils(int id) {
        Log.i(TAG, "MyDatabaseHelper.getProfil ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROFILS,
                        new String[] { COLUMN_PROFILS_ID,
                        COLUMN_PROFILS_NOM,
                        COLUMN_PROFILS_PRENOM,
                        COLUMN_PROFILS_MPD},
                COLUMN_PROFILS_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Profil profil = new Profil(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return profil
        return profil;
    }
    public List<Profil> getAllProfils() {
        Log.i(TAG, "MyDatabaseHelper.getAllProfils ... " );
        List<Profil> noteList = new ArrayList<Profil>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PROFILS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Profil note = new Profil(Integer.parseInt(cursor.getString(0)));
                note.setPrenom(cursor.getString(1));
                note.setMot_de_passe(cursor.getString(2));
                // Adding note to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        // return note list
        return noteList;
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
    public int updateProfil(Profil note) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + note.getNom());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILS_NOM, note.getNom());
        values.put(COLUMN_PROFILS_PRENOM, note.getPrenom());
        values.put(COLUMN_PROFILS_MPD, note.getMot_de_passe());

        // updating row
        return db.update(TABLE_PROFILS, values, COLUMN_PROFILS_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }
    public void deleteNote(Profil note) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + note.getNom() );
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILS, COLUMN_PROFILS_ID + " = ?",
                new String[] { String.valueOf(note.getId()) });
        db.close();
    }
}
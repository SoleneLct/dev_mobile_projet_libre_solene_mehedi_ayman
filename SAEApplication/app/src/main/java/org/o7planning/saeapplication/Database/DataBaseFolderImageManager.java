package org.o7planning.saeapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.o7planning.saeapplication.Modele.Folder;

import java.util.ArrayList;
import java.util.List;

public class DataBaseFolderImageManager extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "ProjectManager";
    // Table name: FolderImages
    public static final String TABLE_FOLDER_IMAGE = "FolderImages";
    public static final String COLUMN_FOLDER_IMAGE_ID ="Folder_Id";
    public static final String COLUMN_FOLDER_IMAGE_NOM ="Folder_Nom";
    public static final String COLUMN_FOLDER_IMAGE_ID_PROFIL = "Folder_Profil_Id";

    public DataBaseFolderImageManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String userForeinKey= "FOREIGN KEY ("+COLUMN_FOLDER_IMAGE_ID_PROFIL+" ) REFERENCES "+DataBaseProfilsManager.TABLE_PROFILS+"("+DataBaseProfilsManager.COLUMN_PROFILS_ID+")";

        String script = "CREATE TABLE " + TABLE_FOLDER_IMAGE + "("
                + COLUMN_FOLDER_IMAGE_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FOLDER_IMAGE_NOM + " TEXT,"
                + COLUMN_FOLDER_IMAGE_ID_PROFIL + " INTEGER"
                + userForeinKey
                + ")";
        // Execute Script.
        db.execSQL(script);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDER_IMAGE);
        // Create tables again
        onCreate(db);
    }

    public void addFolderImage(Folder folderImage) {
        Log.i(TAG, "MyDatabaseHelper.addFolderImage ... " + folderImage.getTitle());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_IMAGE_NOM, folderImage.getTitle());
        values.put(COLUMN_FOLDER_IMAGE_ID_PROFIL, folderImage.getUserId());

        // Inserting Row
        db.insert(TABLE_FOLDER_IMAGE, null, values);
        // Closing database connection
        db.close();
    }

    public Folder getFolderImage(int id) {
        Log.i(TAG, "MyDatabaseHelper.getFolderImage ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOLDER_IMAGE,
                        new String[] {
                                COLUMN_FOLDER_IMAGE_ID,
                                COLUMN_FOLDER_IMAGE_NOM,
                                COLUMN_FOLDER_IMAGE_ID_PROFIL}, COLUMN_FOLDER_IMAGE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Folder folderImage = new Folder(
                Integer.parseInt(cursor.getString(0)),//id
                cursor.getString(1),//nom
                Integer.parseInt(cursor.getString(3))//id profils
        );

        // return folderImage
        return folderImage;
    }
    public List<Folder> getAllFolderImages() {
        Log.i(TAG, "MyDatabaseHelper.getAllFolderImages ... " );
        List<Folder> List = new ArrayList<Folder>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FOLDER_IMAGE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Folder folderImage = new Folder(
                        Integer.parseInt(cursor.getString(0)),//id
                        cursor.getString(1),//nom
                        Integer.parseInt(cursor.getString(3))//id profils
                );
                List.add(folderImage);
            } while (cursor.moveToNext());
        }
        // return folder list
        return List;
    }

    public Folder getFolderImage(int id,String name) {
        Log.i(TAG, "MyDatabaseHelper.getFolderImage ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOLDER_IMAGE,
                new String[] {
                        COLUMN_FOLDER_IMAGE_ID,
                        COLUMN_FOLDER_IMAGE_NOM,
                        COLUMN_FOLDER_IMAGE_ID_PROFIL
                },
                COLUMN_FOLDER_IMAGE_ID + "=? AND " + COLUMN_FOLDER_IMAGE_NOM + "=?",
                new String[] { String.valueOf(id), name },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Folder folderImage = new Folder(
                Integer.parseInt(cursor.getString(0)),//id
                cursor.getString(1),//nom
                Integer.parseInt(cursor.getString(3))//id profils
        );

        // return folderImage
        return folderImage;
    }

    public int getFolderImagesCount() {
        Log.i(TAG, "MyDatabaseHelper.getFolderImagesCount ... " );
        String countQuery = "SELECT * FROM " + TABLE_FOLDER_IMAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    public int getFolderImagesByUserCount(int userId) {
        Log.i(TAG, "MyDatabaseHelper.getFolderImagesByUserCount ... " + userId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOLDER_IMAGE,
                new String[] {
                        COLUMN_FOLDER_IMAGE_ID,
                        COLUMN_FOLDER_IMAGE_NOM,
                        COLUMN_FOLDER_IMAGE_ID_PROFIL}, COLUMN_FOLDER_IMAGE_ID_PROFIL + "=?",
                new String[] { String.valueOf(userId) }, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateImage(Folder folderImage) {
        Log.i(TAG, "MyDatabaseHelper.updateImage ... " + folderImage.getTitle());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_IMAGE_NOM, folderImage.getTitle());
        values.put(COLUMN_FOLDER_IMAGE_ID_PROFIL, folderImage.getUserId());
        // updating row
        return db.update(TABLE_FOLDER_IMAGE, values, COLUMN_FOLDER_IMAGE_ID + " = ?",
                new String[]{String.valueOf(folderImage.getId())});
    }
    public void deleteImage(Folder folderImage) {
        Log.i(TAG, "MyDatabaseHelper.deleteImage ... " + folderImage.getTitle() );
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOLDER_IMAGE, COLUMN_FOLDER_IMAGE_ID + " = ?",
                new String[] { String.valueOf(folderImage.getId()) });
        db.close();
    }
}
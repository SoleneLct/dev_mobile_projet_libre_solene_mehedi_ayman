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
        Folder folderImage = null;
        if (cursor != null && cursor.moveToFirst()){
            folderImage = new Folder(
                    Integer.parseInt(cursor.getString(0)),//id
                    cursor.getString(1),//nom
                    Integer.parseInt(cursor.getString(2))//id profils
            );
        }
        // return folderImage
        return folderImage;
    }
    public Folder getFolderImage(int userId,String name) {
        Log.i(TAG, "MyDatabaseHelper.getFolderImage ... " + userId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOLDER_IMAGE,
                new String[] {
                        COLUMN_FOLDER_IMAGE_ID,
                        COLUMN_FOLDER_IMAGE_NOM,
                        COLUMN_FOLDER_IMAGE_ID_PROFIL
                },
                COLUMN_FOLDER_IMAGE_ID_PROFIL + "=? AND " + COLUMN_FOLDER_IMAGE_NOM + "=?",
                new String[] { String.valueOf(userId), name },
                null, null, null, null);
        Folder folderImage = null;
        if (cursor != null && cursor.moveToFirst()){
            folderImage = new Folder(
                    Integer.parseInt(cursor.getString(0)),//id
                    cursor.getString(1),//nom
                    Integer.parseInt(cursor.getString(2))//id profils
            );
        }
        // return folderImage
        return folderImage;
    }

    public List<Folder> getAllFolderImagesByUserId() {
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
                        Integer.parseInt(cursor.getString(2))//id profils
                );
                List.add(folderImage);
            } while (cursor.moveToNext());
        }
        // return folder list
        return List;
    }
    public List<Folder> getAllFolderImagesByUserId(int userId) {
        Log.i(TAG, "MyDatabaseHelper.getAllFolderImages ... " );
        List<Folder> List = new ArrayList<Folder>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FOLDER_IMAGE+" WHERE "+COLUMN_FOLDER_IMAGE_ID_PROFIL+"="+userId+";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Folder folderImage = new Folder(
                        Integer.parseInt(cursor.getString(0)),//id
                        cursor.getString(1),//nom
                        Integer.parseInt(cursor.getString(2))//id profils
                );
                List.add(folderImage);
            } while (cursor.moveToNext());
        }
        // return folder list
        return List;
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

    public int updateFolder(Folder folderImage) {
        Log.i(TAG, "MyDatabaseHelper.updateImage ... " + folderImage.getTitle());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_IMAGE_NOM, folderImage.getTitle());
        values.put(COLUMN_FOLDER_IMAGE_ID_PROFIL, folderImage.getUserId());
        // updating row
        return db.update(TABLE_FOLDER_IMAGE, values, COLUMN_FOLDER_IMAGE_ID + " = ?",
                new String[]{String.valueOf(folderImage.getId())});
    }
    public void deleteFolder(Folder folderImage,Context context) {
        Log.i(TAG, "MyDatabaseHelper.deleteImage ... " + folderImage.getTitle() );
        SQLiteDatabase db = this.getWritableDatabase();
        new DataBaseImageManager(context).deleteAllImageByUserIdAndFolderId(folderImage.getUserId(),folderImage.getId());
        db.delete(TABLE_FOLDER_IMAGE, COLUMN_FOLDER_IMAGE_ID + " = ?",
                new String[] { String.valueOf(folderImage.getId()) });
        db.close();
    }
    public void deleteAllFolderByUserId(int userId,Context context) {
        Log.i(TAG, "MyDatabaseHelper.deleteAllImage ... " + userId );
        List<Folder> list = getAllFolderImagesByUserId(userId);
        if(list!=null && !list.isEmpty()){
            for (Folder i:list) {
                if(i!=null) deleteFolder(i,context);
            }
        }
    }

}
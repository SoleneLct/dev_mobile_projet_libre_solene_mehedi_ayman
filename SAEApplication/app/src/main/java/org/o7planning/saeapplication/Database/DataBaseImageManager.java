package org.o7planning.saeapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.o7planning.saeapplication.Modele.Image;

import java.util.ArrayList;
import java.util.List;

public class DataBaseImageManager extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ProjectManager";

    // Table name: Image
    private static final String TABLE_IMAGES = "Images";
    private static final String COLUMN_IMAGES_ID ="Images_Id";
    private static final String COLUMN_IMAGES_NOM ="Images_Nom";
    private static final String COLUMN_IMAGES_BLOB = "Images_Blob";

    private static final String COLUMN_IMAGES_TABLE ="Images_Table ";
    private static final String COLUMN_IMAGES_ID_PROFIL = "Images_Profil_Id";

    public DataBaseImageManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String folderforeinKey= "FOREIGN KEY ("+COLUMN_IMAGES_TABLE+" ) REFERENCES "+DataBaseFolderImageManager.TABLE_FOLDER_IMAGE+"("+DataBaseFolderImageManager.COLUMN_FOLDER_IMAGE_ID+")";
        String userForeinKey= "FOREIGN KEY ("+COLUMN_IMAGES_ID_PROFIL+" ) REFERENCES "+DataBaseProfilsManager.TABLE_PROFILS+"("+DataBaseProfilsManager.COLUMN_PROFILS_ID+")";
        String script = "CREATE TABLE " + TABLE_IMAGES + "("
                + COLUMN_IMAGES_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_IMAGES_NOM + " TEXT,"
                + COLUMN_IMAGES_BLOB + "BINARY,"
                + COLUMN_IMAGES_TABLE + "INTEGER,"
                + COLUMN_IMAGES_ID_PROFIL + " INTEGER"
                + userForeinKey
                + folderforeinKey
                + ")";
        // Execute Script.
        db.execSQL(script);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        // Create tables again
        onCreate(db);
    }

    public void addImage(Image image) {
        Log.i(TAG, "MyDatabaseHelper.addImage ... " + image.getNom());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGES_TABLE, image.getIdTable());
        values.put(COLUMN_IMAGES_BLOB, image.getImage());
        values.put(COLUMN_IMAGES_NOM, image.getNom());
        values.put(COLUMN_IMAGES_ID_PROFIL, image.getIdProfils());
        // Inserting Row
        db.insert(TABLE_IMAGES, null, values);
        // Closing database connection
        db.close();
    }
    public Image getImage(int id) {
        Log.i(TAG, "MyDatabaseHelper.getImage ... " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_IMAGES,
                        new String[] {
                                COLUMN_IMAGES_ID,
                                COLUMN_IMAGES_NOM,
                                COLUMN_IMAGES_BLOB,
                                COLUMN_IMAGES_TABLE,
                                COLUMN_IMAGES_ID_PROFIL},
                COLUMN_IMAGES_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        Image image = null;
        if (cursor != null && cursor.moveToFirst()){
            image = new Image(
                    Integer.parseInt(cursor.getString(0)),//id
                    cursor.getString(1),//nom
                    cursor.getBlob(2),//Image
                    Integer.parseInt(cursor.getString(3)),//id table
                    Integer.parseInt(cursor.getString(4))//id profils
            );
        }
        // return Image
        return image;
    }
    public List<Image> getAllImagesByUserId() {
        Log.i(TAG, "MyDatabaseHelper.getAllImages ... " );
        List<Image> list = new ArrayList<Image>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_IMAGES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Image image = new Image(
                        Integer.parseInt(cursor.getString(0)),//id
                        cursor.getString(1),//nom
                        cursor.getBlob(2),//Image
                        Integer.parseInt(cursor.getString(3)),//id table
                        Integer.parseInt(cursor.getString(4))//id profils
                );
                // Adding image to list
                list.add(image);
            } while (cursor.moveToNext());
        }
        // return image list
        return list;
    }
    public List<Image> getAllImagesByUserId(int userId) {
        Log.i(TAG, "MyDatabaseHelper.getAllImagesByUserId ... "+userId );
        List<Image> List = new ArrayList<Image>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_IMAGES+" WHERE "+COLUMN_IMAGES_ID_PROFIL+"="+userId+";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Image image = new Image(
                        Integer.parseInt(cursor.getString(0)),//id
                        cursor.getString(1),//nom
                        cursor.getBlob(2),//Image
                        Integer.parseInt(cursor.getString(3)),//id table
                        Integer.parseInt(cursor.getString(4))//id profils
                );
                List.add(image);
            } while (cursor.moveToNext());
        }
        // return folder list
        return List;
    }
    public int getImagesCount() {
        Log.i(TAG, "MyDatabaseHelper.getImagesCount ... " );
        String countQuery = "SELECT * FROM " + TABLE_IMAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    public int updateImage(Image image) {
        Log.i(TAG, "MyDatabaseHelper.updateImage ... " + image.getNom());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGES_TABLE, image.getIdTable());
        values.put(COLUMN_IMAGES_NOM, image.getNom());
        values.put(COLUMN_IMAGES_BLOB, image.getImage());
        values.put(COLUMN_IMAGES_ID_PROFIL, image.getIdProfils());
        // updating row
        return db.update(TABLE_IMAGES, values, COLUMN_IMAGES_ID + " = ?",
                new String[]{String.valueOf(image.getId())});
    }
    public void deleteImage(Image image) {
        Log.i(TAG, "MyDatabaseHelper.deleteImage ... " + image.getNom() );
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGES, COLUMN_IMAGES_ID + " = ?",
                new String[] { String.valueOf(image.getId()) });
        db.close();
    }
    public void deleteAllImageByUserId(int userId) {
        Log.i(TAG, "MyDatabaseHelper.deleteAllImage ... " + userId );
        List<Image> list = getAllImagesByUserId(userId);
        if(list!=null && !list.isEmpty()){
            for (Image i:list) {
                if(i!=null) deleteImage(i);
            }
        }
    }
    public void deleteAllImageByUserIdAndFolderId(int userId, int folderId) {
        Log.i(TAG, "MyDatabaseHelper.deleteAllImage ... " + userId );
        List<Image> list = getAllImagesByUserId(userId);
        if(list!=null && !list.isEmpty()){
            for (Image i:list) {
                if(i!=null && i.getIdTable()==folderId) deleteImage(i);
            }
        }
    }

}
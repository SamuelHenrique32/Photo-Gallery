package info.codestart.trabalho01.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.List;
import info.codestart.trabalho01.model.Photo;

public class PhotoDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "photos.db";
    private static final int DATABASE_VERSION = 1 ;
    public static final String TABLE_NAME = "Photos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PHOTO_TITLE = "title";
    public static final String COLUMN_PHOTO_DESCRIPTION = "description";
    public static final String COLUMN_PHOTO_IMAGE_URL = "image";


    public PhotoDB(Context currentContext) {
        super(currentContext, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PHOTO_TITLE + " TEXT NOT NULL, " +
                COLUMN_PHOTO_DESCRIPTION + " TEXT NOT NULL, " +
                COLUMN_PHOTO_IMAGE_URL + " BLOB NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(database);
    }


    public void saveNewPhoto(Photo photo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHOTO_TITLE, photo.getTitle());
        values.put(COLUMN_PHOTO_DESCRIPTION, photo.getDescription());
        values.put(COLUMN_PHOTO_IMAGE_URL, photo.getImageUrl());

        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public List<Photo> photoList(String filterToApply) {
        String dbQuery;

        if(filterToApply.equals("")){
            dbQuery = "SELECT  * FROM " + TABLE_NAME;
        }else{
            dbQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filterToApply;
        }

        List<Photo> photoLinkedList = new LinkedList<>();

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(dbQuery, null);

        Photo photo;

        if (cursor.moveToFirst()) {
            do {
                photo = new Photo();

                photo.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                photo.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_TITLE)));
                photo.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_DESCRIPTION)));
                photo.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_IMAGE_URL)));
                photoLinkedList.add(photo);
            } while (cursor.moveToNext());
        }

        return photoLinkedList;
    }

    public Photo getPhoto(long photoId){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ photoId;

        Cursor cursor = db.rawQuery(query, null);

        Photo receivedPhoto = new Photo();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedPhoto.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_TITLE)));
            receivedPhoto.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_DESCRIPTION)));
            receivedPhoto.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_IMAGE_URL)));
        }

        return receivedPhoto;
    }

    public void deletePhotoRegister(long photoId, Context currentContext) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+photoId+"'");

        Toast.makeText(currentContext, "Apagado com Sucesso", Toast.LENGTH_SHORT).show();

    }

    public void updatePhotoRegister(long photoId, Context currentContext, Photo updatedPhoto) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE  "+TABLE_NAME+" SET title ='"+ updatedPhoto.getTitle() + "', description ='" + updatedPhoto.getDescription()+ "', image ='"+ updatedPhoto.getImageUrl() + "'  WHERE _id='" + photoId + "'");

        Toast.makeText(currentContext, "Atualizado com Sucesso", Toast.LENGTH_SHORT).show();
    }
}
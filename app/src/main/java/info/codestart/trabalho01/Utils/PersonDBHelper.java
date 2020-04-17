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

/**
 * Created by Ronsoft on 9/16/2017.
 */

public class PersonDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "people.db";
    private static final int DATABASE_VERSION = 3 ;
    public static final String TABLE_NAME = "People";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PERSON_NAME = "name";
    public static final String COLUMN_PERSON_AGE = "age";
    public static final String COLUMN_PERSON_OCCUPATION = "occupation";
    public static final String COLUMN_PERSON_IMAGE = "image";


    public PersonDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PERSON_NAME + " TEXT NOT NULL, " +
                COLUMN_PERSON_AGE + " NUMBER NOT NULL, " +
                COLUMN_PERSON_OCCUPATION + " TEXT NOT NULL, " +
                COLUMN_PERSON_IMAGE + " BLOB NOT NULL);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
    /**create record**/
    public void saveNewPerson(Photo photo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSON_NAME, photo.getName());
        values.put(COLUMN_PERSON_AGE, photo.getAge());
        values.put(COLUMN_PERSON_OCCUPATION, photo.getOccupation());
        values.put(COLUMN_PERSON_IMAGE, photo.getImage());

        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    /**Query records, give options to filter results**/
    public List<Photo> peopleList(String filter) {
        String query;
        if(filter.equals("")){
            //regular query
            query = "SELECT  * FROM " + TABLE_NAME;
        }else{
            //filter results by filter option provided
            query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY "+ filter;
        }

        List<Photo> photoLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Photo photo;

        if (cursor.moveToFirst()) {
            do {
                photo = new Photo();

                photo.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                photo.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
                photo.setAge(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AGE)));
                photo.setOccupation(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_OCCUPATION)));
                photo.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
                photoLinkedList.add(photo);
            } while (cursor.moveToNext());
        }


        return photoLinkedList;
    }

    /**Query only 1 record**/
    public Photo getPerson(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        Photo receivedPhoto = new Photo();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedPhoto.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_NAME)));
            receivedPhoto.setAge(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_AGE)));
            receivedPhoto.setOccupation(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_OCCUPATION)));
            receivedPhoto.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IMAGE)));
        }



        return receivedPhoto;


    }


    /**delete record**/
    public void deletePersonRecord(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id='"+id+"'");
        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }

    /**update record**/
    public void updatePersonRecord(long personId, Context context, Photo updatedperson) {
        SQLiteDatabase db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+TABLE_NAME+" SET name ='"+ updatedperson.getName() + "', age ='" + updatedperson.getAge()+ "', occupation ='"+ updatedperson.getOccupation() + "', image ='"+ updatedperson.getImage() + "'  WHERE _id='" + personId + "'");
        Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();


    }




}

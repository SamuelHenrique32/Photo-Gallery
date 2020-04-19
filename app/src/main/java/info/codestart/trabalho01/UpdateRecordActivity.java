package info.codestart.trabalho01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import info.codestart.trabalho01.R;
import info.codestart.trabalho01.Utils.PhotoDB;
import info.codestart.trabalho01.model.Photo;

public class UpdateRecordActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mAgeEditText;
    private EditText mOccupationEditText;
    private EditText mImageEditText;
    private Button mUpdateBtn;

    private PhotoDB dbHelper;
    private long receivedPersonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);

        //init
        mNameEditText = (EditText)findViewById(R.id.photoTitleUpdate);
        mAgeEditText = (EditText)findViewById(R.id.photoDescriptionUpdate);
        mUpdateBtn = (Button)findViewById(R.id.updateUserButton);

        dbHelper = new PhotoDB(this);

        try {
            //get intent to get person id
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /***populate user data before update***/
        Photo queriedPhoto = dbHelper.getPerson(receivedPersonId);
        //set field to this user data
        mNameEditText.setText(queriedPhoto.getTitle());
        mAgeEditText.setText(queriedPhoto.getDescription());
//        mOccupationEditText.setText(queriedPhoto.getAge());
//        mImageEditText.setText(queriedPhoto.getImage());



        //listen to add button click to update
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save person method
                updatePerson();
            }
        });





    }

    private void updatePerson(){
        String name = mNameEditText.getText().toString().trim();
        String age = mAgeEditText.getText().toString().trim();
//        String occupation = mOccupationEditText.getText().toString().trim();
//        String image = mImageEditText.getText().toString().trim();


        if(name.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
        }

        if(age.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an age", Toast.LENGTH_SHORT).show();
        }

        /*if(occupation.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an occupation", Toast.LENGTH_SHORT).show();
        }*/

        /*if(image.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an image link", Toast.LENGTH_SHORT).show();
        }*/

        //create updated person
        Photo updatedPhoto = new Photo(name, age, "image_url");

        //call dbhelper update
        dbHelper.updatePersonRecord(receivedPersonId, this, updatedPhoto);

        //finally redirect back home
        // NOTE you can implement an sqlite callback then redirect on success delete
        goBackHome();

    }

    private void goBackHome(){
        startActivity(new Intent(this, MainActivity.class));
    }
}

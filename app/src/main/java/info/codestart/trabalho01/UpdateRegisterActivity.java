package info.codestart.trabalho01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import info.codestart.trabalho01.Utils.PhotoDB;
import info.codestart.trabalho01.model.Photo;

public class UpdateRegisterActivity extends AppCompatActivity {

    private EditText titleEditT;
    private EditText descriptionEditT;
    private Button updateButton;

    private PhotoDB photoDB;

    private long photoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_record);

        titleEditT = (EditText)findViewById(R.id.photoTitleUpdate);

        descriptionEditT = (EditText)findViewById(R.id.photoDescriptionUpdate);

        updateButton = (Button)findViewById(R.id.updatePhotoButton);

        photoDB = new PhotoDB(this);

        try {
            photoID = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Photo photo = photoDB.getPhoto(photoID);

        titleEditT.setText(photo.getTitle());

        descriptionEditT.setText(photo.getDescription());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePhoto();
            }
        });
    }

    private void updatePhoto(){
        String title = titleEditT.getText().toString().trim();

        String description = descriptionEditT.getText().toString().trim();

        if(title.isEmpty()){
            Toast.makeText(this, "O título não pode ser vazio", Toast.LENGTH_SHORT).show();
        }

        if(description.isEmpty()){
            Toast.makeText(this, "A descrição não pode ser vazia", Toast.LENGTH_SHORT).show();
        }

        /*if(image.isEmpty()){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }*/

        Photo updatedPhoto = new Photo(title, description, "image_url");

        photoDB.updatePhotoRegister(photoID, this, updatedPhoto);

        goToHome();

    }

    private void goToHome() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
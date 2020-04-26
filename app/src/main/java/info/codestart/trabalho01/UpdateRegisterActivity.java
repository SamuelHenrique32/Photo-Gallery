package info.codestart.trabalho01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import info.codestart.trabalho01.Utils.PhotoDB;
import info.codestart.trabalho01.model.Photo;

import static info.codestart.trabalho01.R.*;

public class UpdateRegisterActivity extends AppCompatActivity {

    private EditText titleEditT;
    private EditText descriptionEditT;
    private Button updateButton;
    public ImageView photoImageUrlView;

    private PhotoDB photoDB;
    private Photo photo;

    private long photoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.activity_update_register);

        titleEditT = (EditText)findViewById(id.photoTitleUpdate);

        descriptionEditT = (EditText)findViewById(id.photoDescriptionUpdate);

        updateButton = (Button)findViewById(id.updatePhotoButton);

        photoImageUrlView = (ImageView) findViewById(id.image);

        photoDB = new PhotoDB(this);

        try {
            photoID = getIntent().getLongExtra("PHOTO_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        photo = photoDB.getPhoto(photoID);

        titleEditT.setText(photo.getTitle());

        descriptionEditT.setText(photo.getDescription());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePhoto();
            }
        });

        ImageView imageView = (ImageView) findViewById(id.image);

        Picasso.with(this).load("file://" + photo.getImageUrl()).rotate(90).into(imageView);
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

        Photo updatedPhoto = new Photo(title, description, photo.getImageUrl());

        photoDB.updatePhotoRegister(photoID, this, updatedPhoto);

        goToHome();

    }

    private void goToHome() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
package info.codestart.trabalho01;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import info.codestart.trabalho01.Utils.PhotoDB;
import info.codestart.trabalho01.model.Photo;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static info.codestart.trabalho01.R.*;

public class UpdateRegisterActivity extends AppCompatActivity {

    private TextView titleViewT;
    private AlertDialog titleAlertT;
    private EditText titleEditT;

    private TextView descriptionViewT;
    private AlertDialog descriptionAlertT;
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

        updateButton = (Button)findViewById(id.updatePhotoButton);

        photoImageUrlView = (ImageView) findViewById(id.image);

        photoDB = new PhotoDB(this);

        try {
            photoID = getIntent().getLongExtra("PHOTO_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        photo = photoDB.getPhoto(photoID);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePhoto();
            }
        });

        ImageView imageView = (ImageView) findViewById(id.image);

        Picasso.with(this).load("file://" + photo.getImageUrl()).rotate(90).into(imageView);

        // Title --------------------------------------------------------------------------------------------------

        titleViewT = (TextView)findViewById(id.title);

        if(photo.getTitle().length() == 0) {
            titleViewT.setText("URL: " + photo.getImageUrl());

            Toast.makeText(this, "O título não foi definido, mostrando URL", Toast.LENGTH_LONG).show();
        } else {
            titleViewT.setText("Título: " + photo.getTitle());
        }

        titleAlertT = new AlertDialog.Builder(this).create();
        titleEditT = new EditText(this);
        titleAlertT.setTitle("Editar Título ");
        titleAlertT.setView(titleEditT);
        titleAlertT.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                titleViewT.setText("Título: " + titleEditT.getText());
            }
        });

        titleViewT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(photo.getTitle().length() == 0) {
                    titleEditT.setText(photo.getTitle());
                } else {
                    titleEditT.setText(titleViewT.getText().subSequence(8,titleViewT.getText().length()));
                }

                titleAlertT.show();
            }
        });
        // Title --------------------------------------------------------------------------------------------------

        // Description --------------------------------------------------------------------------------------------------
        descriptionViewT = (TextView)findViewById(id.description);

        if(photo.getDescription().length() == 0) {
            descriptionViewT.setText("Descrição: não há descrição" + photo.getDescription());
        } else{
            descriptionViewT.setText("Descrição: " + photo.getDescription());
        }

        descriptionAlertT = new AlertDialog.Builder(this).create();
        descriptionEditT = new EditText(this);
        descriptionAlertT.setTitle("Editar Descrição ");
        descriptionAlertT.setView(descriptionEditT);

        descriptionAlertT.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                descriptionViewT.setText("Descrição: " + descriptionEditT.getText());
            }
        });

        descriptionViewT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photo.getDescription().length() == 0) {
                    descriptionEditT.setText(photo.getDescription());
                } else {
                    descriptionEditT.setText(descriptionViewT.getText().subSequence(11,descriptionViewT.getText().length()));
                }

                descriptionAlertT.show();
            }
        });
        // Description --------------------------------------------------------------------------------------------------
    }

    private void updatePhoto(){
        String title = titleViewT.getText().toString().substring(8, titleViewT.getText().toString().length()).trim();

        String description = descriptionViewT.getText().toString().substring(11, descriptionViewT.getText().length()).trim();

        Photo updatedPhoto = new Photo(title, description, photo.getImageUrl());

        photoDB.updatePhotoRegister(photoID, this, updatedPhoto);

        goToHome();
    }

    private void goToHome() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
package info.codestart.trabalho01;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import info.codestart.trabalho01.Utils.PhotoDB;
import info.codestart.trabalho01.model.Photo;
import static info.codestart.trabalho01.R.*;

public class AddRegisterActivity extends AppCompatActivity {

    private EditText titleEditT;
    private EditText descriptionEditT;
    private EditText photoEditT;
    private Button saveButton;
    private PhotoDB photoDB;
    private File photoFile = null;
    private long photoID;

    private final int CAMERA = 3;
    private final int IMAGE_GALERY = 1;
    private boolean pictureTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_register);

        titleEditT = (EditText)findViewById(R.id.photoTitle);
        descriptionEditT = (EditText)findViewById(R.id.photoDescription);
        //photoEditT = (EditText)findViewById(R.id.);
        saveButton = (Button)findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePhoto();
            }
        });

        // Execute this code just if the picture was already taken
        // -----------------------------------------------------------------------------------------
        //if(pictureTaken) {
            ImageView imageView = (ImageView) findViewById(R.id.image);

            photoDB = new PhotoDB(this);

            try {
                photoID = getIntent().getLongExtra("USER_ID", 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Photo photo = photoDB.getPhoto(photoID);

            // Make it dynamic
            Picasso.with(this).load("file:///storage/emulated/0/Pictures/JPG_20200424_180616.jpg").rotate(90).into(imageView);
        //}
        // -----------------------------------------------------------------------------------------
    }

    private void savePhoto(){
        String title = titleEditT.getText().toString().trim();

        String description = descriptionEditT.getText().toString().trim();

        photoDB = new PhotoDB(this);

        if(title.isEmpty()){
            Toast.makeText(this, "O título não pode ser vazio", Toast.LENGTH_SHORT).show();
        }

        if(description.isEmpty()){
            Toast.makeText(this, "A descrição não pode ser vazia", Toast.LENGTH_SHORT).show();
        }

        /*if(image.isEmpty()){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }*/

        Photo photo = new Photo(title, description, "aaa");

        photoDB.saveNewPhoto(photo);

        goToHome();
    }

    private void goToHome(){
        startActivity(new Intent(AddRegisterActivity.this, MainActivity.class));
    }

    private File createFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // TODO Adapt dir
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //File dir = Environment.getExternalStoragePublicDirectory("/sdcard/Pictures");


        File image = new File(dir.getPath() + File.separator + "JPG_" + timeStamp + ".jpg");

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA) {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(photoFile))
            );

            Toast.makeText(this,  photoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
    }

    public void takePicture(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI;

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Falha ao Capturar Foto", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                try {
                    photoURI = FileProvider.getUriForFile(getBaseContext(),
                            getBaseContext().getApplicationContext().getPackageName() +
                                    ".provider", photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, CAMERA);

                } catch (Exception e) {
                    //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Toast.makeText(this, "Não Foi Possível Abrir a Câmera", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
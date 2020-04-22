package info.codestart.trabalho01;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import info.codestart.trabalho01.Utils.PhotoDB;
import info.codestart.trabalho01.model.Photo;

public class AddRegisterActivity extends AppCompatActivity {

    private EditText titleEditT;
    private EditText descriptionEditT;
    private EditText photoEditT;
    private Button saveButton;
    private PhotoDB photoDB;
    private File photoFile = null;

    private final int CAMERA = 3;

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

        // TODO Apapt dir
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = new File(dir.getPath() + File.separator + "JPG_" + timeStamp + ".jpg");

        return image;
    }

    public void takePicture(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createFile();
            } catch (IOException ex) {
                //mostraAlerta(getString(R.string.erro), getString(R.string.erro_salvando_foto));
            }
            if (photoFile != null) {
                /*Uri photoURI = FileProvider.getUriForFile(getBaseContext(),
                        getBaseContext().getApplicationContext().getPackageName() +
                                ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);*/
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }
}
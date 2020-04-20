package info.codestart.trabalho01;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import info.codestart.trabalho01.Utils.PhotoDB;
import info.codestart.trabalho01.model.Photo;

public class AddRegisterActivity extends AppCompatActivity {

    private EditText titleEditT;
    private EditText descriptionEditT;
    private EditText photoEditT;

    private Button saveButton;

    private PhotoDB photoDB;

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
}
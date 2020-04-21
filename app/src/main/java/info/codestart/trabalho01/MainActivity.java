package info.codestart.trabalho01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import info.codestart.trabalho01.Utils.PhotoAdapter;
import info.codestart.trabalho01.Utils.PhotoDB;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerVLayoutManager;
    private PhotoDB photoDB;
    private PhotoAdapter photoAdapter;
    private String currentFilter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerVLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerVLayoutManager);

        addRowsToRecyclerView(currentFilter);
    }

    private void addRowsToRecyclerView(String filter){
        photoDB = new PhotoDB(this);

        photoAdapter = new PhotoAdapter(photoDB.photoList(filter), this, recyclerView);

        recyclerView.setAdapter(photoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Depending of the selected item
        switch (item.getItemId()) {

            case R.id.addMenu:

                goToAddPhotoActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToAddPhotoActivity(){
        Intent intent = new Intent(MainActivity.this, AddRegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        photoAdapter.notifyDataSetChanged();
    }
}
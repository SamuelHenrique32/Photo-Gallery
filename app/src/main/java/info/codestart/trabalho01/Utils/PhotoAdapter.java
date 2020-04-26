package info.codestart.trabalho01.Utils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import info.codestart.trabalho01.R;
import info.codestart.trabalho01.UpdateRegisterActivity;
import info.codestart.trabalho01.model.Photo;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<Photo> photoList;
    private Context currentContext;
    private RecyclerView recyclerView;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView photoTitleTxtView;
        public TextView photoDescriptionTxtView;
        public ImageView photoImageUrlView;

        public View currentLayout;

        public ViewHolder(View view) {
            super(view);
            currentLayout = view;
            photoTitleTxtView = (TextView) view.findViewById(R.id.title);
            photoDescriptionTxtView = (TextView) view.findViewById(R.id.description);
            photoImageUrlView = (ImageView) view.findViewById(R.id.image);
        }
    }

    public void addPhoto(int pos, Photo photo) {
        photoList.add(pos, photo);
        notifyItemInserted(pos);
    }

    public void removePhoto(int pos) {
        photoList.remove(pos);
        notifyItemRemoved(pos);
    }
    
    //Constructor 01
    public PhotoAdapter(List<Photo> photos, Context currentContext, RecyclerView recyclerView) {
        photoList = photos;
        this.currentContext = currentContext;
        this.recyclerView = recyclerView;
    }

    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        
        View v = inflater.inflate(R.layout.register, parent, false);
        
        // Set the view's  parameters
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int pos) {

        final Photo photo = photoList.get(pos);

        // If the photo has no title, show URL
        if(photo.getTitle().length() == 0) {
            viewHolder.photoTitleTxtView.setText("URL: " + photo.getImageUrl());
        } else {
            viewHolder.photoTitleTxtView.setText("Título: " + photo.getTitle());
        }

        // If the photo has no description, show a message
        if(photo.getDescription().length() == 0) {
            viewHolder.photoDescriptionTxtView.setText("Descrição: não há descrição");
        } else{
            viewHolder.photoDescriptionTxtView.setText("Descrição: " + photo.getDescription());
        }

        // Load photo
        Picasso.with(currentContext).load("file://" + photo.getImageUrl()).rotate(90).into(viewHolder.photoImageUrlView);

        viewHolder.currentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(currentContext);
                builder.setTitle("Escolha uma Opção");
                builder.setMessage("Visualizar ou Apagar?");
                builder.setPositiveButton("Visualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    goToUpdateActivity(photo.getId());
                    }
                });
                builder.setNeutralButton("Apagar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PhotoDB photoDB = new PhotoDB(currentContext);

                        photoDB.deletePhotoRegister(photo.getId(), currentContext);

                        photoList.remove(pos);

                        recyclerView.removeViewAt(pos);

                        notifyItemRemoved(pos);

                        notifyItemRangeChanged(pos, photoList.size());

                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void goToUpdateActivity(long photoId){
        Intent updateIntent = new Intent(currentContext, UpdateRegisterActivity.class);
        updateIntent.putExtra("PHOTO_ID", photoId);
        currentContext.startActivity(updateIntent);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
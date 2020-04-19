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
import info.codestart.trabalho01.UpdateRecordActivity;
import info.codestart.trabalho01.model.Photo;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    private List<Photo> photoList;
    private Context currentContext;
    private RecyclerView recyclerView;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView photoTitleTxtView;
        public TextView personAgeTxtV;
        public TextView personOccupationTxtV;
        public ImageView personImageImgV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            photoTitleTxtView = (TextView) v.findViewById(R.id.title);
            personOccupationTxtV = (TextView) v.findViewById(R.id.description);
            personImageImgV = (ImageView) v.findViewById(R.id.image);




        }
    }

    public void add(int position, Photo photo) {
        photoList.add(position, photo);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        photoList.remove(position);
        notifyItemRemoved(position);
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public PersonAdapter(List<Photo> myDataset, Context context, RecyclerView recyclerView) {
        photoList = myDataset;
        currentContext = context;
        this.recyclerView = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.single_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Photo photo = photoList.get(position);
        holder.photoTitleTxtView.setText("Título: " + photo.getTitle());
//        holder.personAgeTxtV.setText("Age: " + photo.getAge());
        holder.personOccupationTxtV.setText("Descrição: " + photo.getDescription());
        Picasso.with(currentContext).load(photo.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.personImageImgV);

        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(currentContext);
                builder.setTitle("Choose option");
                builder.setMessage("Update or delete user?");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    //go to update activity
                        goToUpdateActivity(photo.getId());

                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PersonDBHelper dbHelper = new PersonDBHelper(currentContext);
                        dbHelper.deletePersonRecord(photo.getId(), currentContext);

                        photoList.remove(position);
                        recyclerView.removeViewAt(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, photoList.size());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });


    }

    private void goToUpdateActivity(long personId){
        Intent goToUpdate = new Intent(currentContext, UpdateRecordActivity.class);
        goToUpdate.putExtra("USER_ID", personId);
        currentContext.startActivity(goToUpdate);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return photoList.size();
    }



}
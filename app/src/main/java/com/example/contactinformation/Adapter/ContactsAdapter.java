package com.example.contactinformation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactinformation.FirebaseModel.Contacts;
import com.example.contactinformation.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactsAdapter extends FirebaseRecyclerAdapter<Contacts,ContactsAdapter.ViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ContactsAdapter(@NonNull FirebaseRecyclerOptions<Contacts> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Contacts model) {
        if(model.getImageUrl()!=null){
            String url = model.getImageUrl();
            System.out.println(url);
            Picasso.get().load(url).into(holder.imageView);
        }else{
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.textViewName.setText(model.getName());
        holder.textViewContact.setText(model.getPhone());
        Boolean fav= model.getFavourite();
        if(fav){
            holder.favourite.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
        }else{
            holder.favourite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
        }

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                popupMenu.inflate(R.menu.pop_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:
                                FirebaseDatabase.getInstance().getReference().child("Contacts").child(getRef(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(v.getContext(),"Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(),"Oops! Can't delete",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return true;
                            default:
                                return false;

                        }

                    }
                });
                popupMenu.show();
            }
        });

        holder.favourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(model.getFavourite() == true){
                    model.setFavourite(false);
                    holder.favourite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                    FirebaseDatabase.getInstance().getReference().child("Contacts").child(getRef(position).getKey()).child("Favourite").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Favourites Removed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(v.getContext(), "Failed to Remove", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    holder.favourite.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    model.setFavourite(true);
                    FirebaseDatabase.getInstance().getReference().child("Contacts").child(getRef(position).getKey()).child("Favourite").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Favourites Added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(v.getContext(), "Failed to Add", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView textViewName,textViewContact;
        ImageButton favourite,more;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(CircleImageView)itemView.findViewById(R.id.imageView);
            textViewName=(TextView)itemView.findViewById(R.id.textViewName);
            textViewContact=(TextView)itemView.findViewById(R.id.textViewContact);
            favourite=(ImageButton)itemView.findViewById(R.id.btnFavourite);
            more = (ImageButton)itemView.findViewById(R.id.btnMore);


        }
    }
}

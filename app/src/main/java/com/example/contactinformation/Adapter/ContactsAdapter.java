package com.example.contactinformation.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contactinformation.FirebaseModel.Contacts;
import com.example.contactinformation.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.contactinformation.R.drawable.ic_baseline_favorite_24;

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
            holder.favourite.setImageResource(ic_baseline_favorite_24);
        }else{
            holder.favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
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
        ImageButton favourite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(CircleImageView)itemView.findViewById(R.id.imageView);
            textViewName=(TextView)itemView.findViewById(R.id.textViewName);
            textViewContact=(TextView)itemView.findViewById(R.id.textViewContact);
            favourite=(ImageButton)itemView.findViewById(R.id.btnFavourite);
        }
    }
}

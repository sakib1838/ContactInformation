package com.example.contactinformation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.contactinformation.Adapter.ContactsAdapter;
import com.example.contactinformation.FirebaseModel.Contacts;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.sql.SQLOutput;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewContacts;
    ContactsAdapter contactsAdapter;
    FloatingActionButton floatingActionButton;

    FirebaseDatabase firebaseDatabase;
    Contacts contacts;

    public static final int ADD_CONTACT_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton=(FloatingActionButton)findViewById(R.id.addNewContacts);

        contacts = new Contacts();
        firebaseDatabase = FirebaseDatabase.getInstance();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddContactsActivity.class);
                startActivityForResult(intent,ADD_CONTACT_REQUEST);
            }
        });

        recyclerViewContacts=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));


        Query query = FirebaseDatabase.getInstance().getReference().child("Contacts");

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(query, Contacts.class)
                        .build();


        contactsAdapter = new ContactsAdapter(options);
        recyclerViewContacts.setAdapter(contactsAdapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("reqCode:"+requestCode+"resCode:"+requestCode);
        System.out.println(ADD_CONTACT_REQUEST+"resOK:"+RESULT_OK);
        if(requestCode==ADD_CONTACT_REQUEST && resultCode==RESULT_OK){
            assert data != null;
            String name = data.getStringExtra("Name");
            String phone = data.getStringExtra("Phone");
            String url = data.getStringExtra("Url");
            boolean fav = data.getBooleanExtra("Favourite",false);


            contacts.setName(data.getStringExtra("Name"));
            contacts.setPhone(data.getStringExtra("Phone"));
            contacts.setImageUrl(data.getStringExtra("Url"));
            contacts.setFavourite(data.getBooleanExtra("Favourite",false));

            addContact();

            Toast.makeText(MainActivity.this,name+phone+url,Toast.LENGTH_SHORT).show();
            System.out.println(name+"/"+phone+"/"+url+"/"+ fav);
        }

    }

    private void addContact(){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Contacts");

        HashMap<String,Object>contact = new HashMap<>();
        contact.put("Name",contacts.getName());
        contact.put("Phone",contacts.getPhone());
        contact.put("ImageUrl",contacts.getImageUrl());
        contact.put("Favourite",contacts.getFavourite());

        databaseReference.push().setValue(contact).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Done")
                        .setMessage("Product Added")
                        .setNegativeButton("Ok",null)
                        .show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage(e.toString())
                        .setNegativeButton("Ok",null)
                        .show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        contactsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        contactsAdapter.stopListening();
    }

}
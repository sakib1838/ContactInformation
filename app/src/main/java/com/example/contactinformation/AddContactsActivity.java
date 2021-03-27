package com.example.contactinformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactsActivity extends AppCompatActivity {


    CircleImageView imageViewNew;
    TextView textViewAddNewImage,textViewRemoveNewImage;
    TextInputEditText textInputEditTextName,textInputEditTextPhone;
    MaterialButton btnUploadInformation;
    SwitchMaterial switchMaterial;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private String url;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        switchMaterial = (SwitchMaterial)findViewById(R.id.favSwitch);
        textInputEditTextName = (TextInputEditText)findViewById(R.id.textInputEditTextName);
        textInputEditTextPhone = (TextInputEditText)findViewById(R.id.textInputEditTextPhoneNumber);
        textViewAddNewImage=(TextView)findViewById(R.id.textViewAddNewImage);
        textViewRemoveNewImage=(TextView)findViewById(R.id.textViewRemoveNewImage);
        imageViewNew = (CircleImageView)findViewById(R.id.imageViewNew);
        btnUploadInformation = (MaterialButton)findViewById(R.id.btnUploadInformation);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        textViewRemoveNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage();

            }
        });

        textViewAddNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        btnUploadInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInformation();
            }
        });


    }

    private void removeImage(){
        if(url!=null){
            StorageReference storageReference1 = firebaseStorage.getReferenceFromUrl(url);
            storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddContactsActivity.this,"Removed",Toast.LENGTH_SHORT).show();
                    textViewRemoveNewImage.setVisibility(View.INVISIBLE);
                    textViewAddNewImage.setVisibility(View.VISIBLE);
                    imageViewNew.setImageResource(R.drawable.ic_launcher_background);
                    filePath=null;


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddContactsActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(AddContactsActivity.this,"No Image Reference", Toast.LENGTH_SHORT).show();
        }


    }

    private void uploadInformation(){



        String name = textInputEditTextName.getText().toString();
        String phone = textInputEditTextPhone.getText().toString();
        Boolean fav = switchMaterial.isChecked();


        if(name.trim().isEmpty() || phone.trim().isEmpty()){
            Toast.makeText(this, "Fill the empty field", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent();
        intent.putExtra("Name",name);
        intent.putExtra("Phone",phone);
        intent.putExtra("Url",url);
        intent.putExtra("Favourite",fav);

        System.out.println(name+phone+url+fav);

        setResult(RESULT_OK,intent);
        System.out.println("ResultOK:"+RESULT_OK);

        finish();

    }

    private void uploadImage(){
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddContactsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url=uri.toString();
                                    System.out.println(url);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddContactsActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private  void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            uploadImage();
            textViewAddNewImage.setVisibility(View.INVISIBLE);
            textViewRemoveNewImage.setVisibility(View.VISIBLE);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewNew.setImageBitmap(bitmap);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
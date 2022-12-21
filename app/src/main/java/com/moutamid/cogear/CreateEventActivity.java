package com.moutamid.cogear;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.moutamid.cogear.databinding.ActivityCreateEventBinding;
import com.moutamid.cogear.models.EventModel;
import com.moutamid.cogear.models.UserModel;
import com.moutamid.cogear.utilis.Constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.util.UUID;

public class CreateEventActivity extends AppCompatActivity {

    ActivityCreateEventBinding binding;
    Uri image = null;
    boolean isSubscribe = false;
    String uuID, imageLink="";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating your event...");
        progressDialog.setCancelable(false);

        binding.uploadImageCard.setOnClickListener(v -> {
            ImagePicker.with(this).crop(16f, 9f).compress(1024).galleryOnly().start();
        });

        binding.btnExit.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isSubscribe = snapshot.getValue(UserModel.class).isSubscribe();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.btnNext.setOnClickListener(v -> {
            if (!isSubscribe){
                startActivity(new Intent(this, SubscribeActivity.class));
            } else {
                if (validate()){
                    uuID = UUID.randomUUID().toString();
                    progressDialog.show();
                    Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                            .child("Events").child(uuID).child("image")
                            .putFile(image).addOnSuccessListener(taskSnapshot -> {
                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                    imageLink = uri.toString();
                                    uploadEvent();
                                }).addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                // Error, Image not uploaded
                                Toast.makeText(CreateEventActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

    }

    private void uploadEvent() {
        EventModel model = new EventModel(
                uuID,
                binding.productName.getEditText().getText().toString(),
                binding.productDesc.getEditText().getText().toString(),
                binding.productCategory.getEditText().getText().toString(),
                binding.cityName.getEditText().getText().toString(),
                imageLink,
                0
        );
        Constants.databaseReference().child("events").child(Constants.auth().getCurrentUser().getUid())
                .child(uuID)
                .setValue(model)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    onBackPressed();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                });
    }

    private boolean validate() {
        if (binding.productName.getEditText().getText().toString().isEmpty()){
            binding.productName.getEditText().setError("Please Provide a valid name");
            binding.productName.getEditText().requestFocus();
            return false;
        }
        if (binding.productDesc.getEditText().getText().toString().isEmpty()){
            binding.productDesc.getEditText().setError("Description is Required");
            binding.productDesc.getEditText().requestFocus();
            return false;
        }
        if (binding.productCategory.getEditText().getText().toString().isEmpty()){
            binding.productCategory.getEditText().setError("Category is Required*");
            binding.productCategory.getEditText().requestFocus();
            return false;
        }
        if (binding.cityName.getEditText().getText().toString().isEmpty()){
            binding.cityName.getEditText().setError("City is Required*");
            binding.cityName.getEditText().requestFocus();
            return false;
        }
        if (image == null){
            Toast.makeText(this, "Please Upload a image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null){
            image = data.getData();
            binding.uploadImage.setImageURI(image);
        } else if (resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    }
}
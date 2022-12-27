package com.moutamid.cogear;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.moutamid.cogear.databinding.ActivityCreateEventBinding;
import com.moutamid.cogear.models.EventModel;
import com.moutamid.cogear.models.UserModel;
import com.moutamid.cogear.utilis.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateEventActivity extends AppCompatActivity {

    ActivityCreateEventBinding binding;
    Uri image = null;
    boolean isSubscribe;
    String uuID, imageLink = "";
    ProgressDialog progressDialog;
    ArrayAdapter<CharSequence> categoryAdapter;

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

        categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category, R.layout.dropdown_layout);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categories.setAdapter(categoryAdapter);

        /*Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isSubscribe = snapshot.getValue(UserModel.class).isSubscribe();
                        Stash.put("isSubscribe", isSubscribe);
                        Toast.makeText(CreateEventActivity.this, "is "+isSubscribe, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/

        binding.btnNext.setOnClickListener(v -> {
            Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                    .get().addOnSuccessListener(dataSnapshot -> {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        if (!model.isSubscribe()) {
                            startActivity(new Intent(CreateEventActivity.this, SubscribeActivity.class));
                        } else {
                            if (validate()) {
                                uuID = UUID.randomUUID().toString();
                                progressDialog.show();
                                if (model.getEventsCreated() < 3) {
                                    Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                                            .child("Events").child(uuID).child("image")
                                            .putFile(image).addOnSuccessListener(taskSnapshot -> {
                                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                                    imageLink = uri.toString();
                                                    uploadEvent(model.getEventsCreated());
                                                }).addOnFailureListener(e -> {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                            }).addOnFailureListener(e -> {
                                                progressDialog.dismiss();
                                                Toast.makeText(CreateEventActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, "You can only create 2 events", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }

    private void uploadEvent(int eventsCreated) {
        EventModel model = new EventModel(
                uuID,
                Constants.auth().getCurrentUser().getUid(),
                binding.productName.getEditText().getText().toString(),
                binding.productDesc.getEditText().getText().toString(),
                binding.productCategory.getEditText().getText().toString(),
                binding.cityName.getEditText().getText().toString(),
                imageLink,
                0
        );
        Constants.databaseReference().child("events").child(uuID)
                .setValue(model)
                .addOnSuccessListener(unused -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("eventsCreated", (eventsCreated + 1));
                    Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                            .updateChildren(map).addOnSuccessListener(unused1 -> {
                                progressDialog.dismiss();
                                onBackPressed();
                                finish();
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                });
    }

    private boolean validate() {
        if (binding.productName.getEditText().getText().toString().isEmpty()) {
            binding.productName.getEditText().setError("Please Provide a valid name");
            binding.productName.getEditText().requestFocus();
            return false;
        }
        if (binding.productDesc.getEditText().getText().toString().isEmpty()) {
            binding.productDesc.getEditText().setError("Description is Required");
            binding.productDesc.getEditText().requestFocus();
            return false;
        }
        if (binding.productCategory.getEditText().getText().toString().isEmpty()) {
            binding.productCategory.getEditText().setError("Category is Required*");
            binding.productCategory.getEditText().requestFocus();
            return false;
        }
        if (binding.cityName.getEditText().getText().toString().isEmpty()) {
            binding.cityName.getEditText().setError("City is Required*");
            binding.cityName.getEditText().requestFocus();
            return false;
        }
        if (image == null) {
            Toast.makeText(this, "Please Upload a image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            image = data.getData();
            binding.uploadImage.setImageURI(image);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    }
}
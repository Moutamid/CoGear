package com.moutamid.cogear;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moutamid.cogear.databinding.ActivityEditProfileBinding;
import com.moutamid.cogear.models.UserModel;
import com.moutamid.cogear.utilis.Constants;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    Uri imageURI;
    private static final int PICK_FROM_GALLERY = 1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Your Profile...");
        progressDialog.setCancelable(false);

        Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    UserModel model = dataSnapshot.getValue(UserModel.class);
                   binding.name.getEditText().setText(model.getName());
                    Glide.with(EditProfileActivity.this).load(model.getImage()).placeholder(R.drawable.profile_icon).into(binding.profileImage);
                }).addOnFailureListener(e ->{
                });

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        binding.addImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "Continue with"), PICK_FROM_GALLERY);
        });

        binding.btnCreate.setOnClickListener(v -> {
            progressDialog.show();
            if (imageURI == null) {
                updateName();
            } else {
                updateBoth();
            }
        });

    }

    private void updateBoth() {
        Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                .child("userProfile").putFile(imageURI).addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("image", uri.toString());
                        map.put("name", binding.name.getEditText().getText().toString());
                        Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                                .updateChildren(map).addOnSuccessListener(unused -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateName() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", binding.name.getEditText().getText().toString());
        Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                .updateChildren(map).addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_FROM_GALLERY) {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    imageURI = data.getData();
                    Glide.with(EditProfileActivity.this).load(imageURI).into(binding.profileImage);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
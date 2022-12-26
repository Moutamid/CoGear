package com.moutamid.cogear;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.moutamid.cogear.databinding.ActivityResetPasswordBinding;
import com.moutamid.cogear.utilis.Constants;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {
    ActivityResetPasswordBinding binding;
    AuthCredential authCredential;
    FirebaseUser user;
    String oldPassword, newPassword, retypePass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Your Password");
        progressDialog.setCancelable(false);

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        binding.forgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgetPasswordActivity.class));
            finish();
        });

        binding.btnChangePassword.setOnClickListener(v -> {
            if (validate()) {
                authCredential = EmailAuthProvider.getCredential(user.getEmail(), binding.oldPassword.getEditText().getText().toString());
                user.reauthenticate(authCredential).addOnSuccessListener(unused -> {
                    user.updatePassword(binding.password.getEditText().getText().toString())
                            .addOnSuccessListener(unused1 -> {
                                Map<String, Object> map = new HashMap<>();
                                map.put("password", binding.password.getEditText().getText().toString());
                                Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                                        .updateChildren(map)
                                        .addOnSuccessListener(unused2 -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Current Password Doesn't Match", Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    private boolean validate() {
        if (binding.oldPassword.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Your Current Password", Toast.LENGTH_SHORT).show();
            binding.oldPassword.getEditText().setError("*Required");
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            binding.password.getEditText().setError("*Required");
            return false;
        }
        if (binding.cnfrmPassword.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Confirm Your Password", Toast.LENGTH_SHORT).show();
            binding.cnfrmPassword.getEditText().setError("*Required");
            return false;
        }
        if (binding.cnfrmPassword.getEditText().getText().toString().equals(binding.password.getEditText().getText().toString())) {
            Toast.makeText(this, "Password is not Matched", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
package com.moutamid.cogear;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.moutamid.cogear.databinding.ActivitySignUpBinding;
import com.moutamid.cogear.models.UserModel;
import com.moutamid.cogear.utilis.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    ProgressDialog progressDialog;
    SimpleDateFormat format = new SimpleDateFormat("yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Your Account...");
        progressDialog.setCancelable(false);

        binding.btnSignup.setOnClickListener(v -> {
            Date date = new Date();
            String d = format.format(date);
            if (validate()){
                Constants.auth().createUserWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    progressDialog.show();
                    UserModel model = new UserModel(
                            binding.name.getEditText().getText().toString(),
                            binding.email.getEditText().getText().toString(),
                            binding.password.getEditText().getText().toString(), d, 0, 0, "", false);
                    Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                            .setValue(model)
                            .addOnSuccessListener(unused -> {
                                progressDialog.dismiss();
                                startActivity(new Intent(SignUpActivity.this, SelectInterestActivity.class));
                                finish();
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    private boolean validate() {
        if (binding.name.getEditText().getText().toString().isEmpty()){
            binding.name.getEditText().setError("Name is Required*");
            binding.name.getEditText().requestFocus();
            return false;
        }
        if (binding.email.getEditText().getText().toString().isEmpty()){
            binding.email.getEditText().setError("Email is Required*");
            binding.email.getEditText().requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()){
            binding.email.getEditText().setError("Email is not Valid*");
            binding.email.getEditText().requestFocus();
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()){
            binding.password.getEditText().setError("Password is Required*");
            binding.password.getEditText().requestFocus();
            return false;
        }
        if (binding.cnfrmPassword.getEditText().getText().toString().isEmpty()){
            binding.cnfrmPassword.getEditText().setError("Password is Required*");
            binding.cnfrmPassword.getEditText().requestFocus();
            return false;
        }

        if (!binding.password.getEditText().getText().toString().equals(binding.cnfrmPassword.getEditText().getText().toString())){
            binding.cnfrmPassword.getEditText().setError("Password doesn't match*");
            binding.cnfrmPassword.getEditText().requestFocus();
            return false;
        }

        return true;
    }


}
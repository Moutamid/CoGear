package com.moutamid.cogear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.cogear.databinding.ActivityForgetPasswordBinding;
import com.moutamid.cogear.utilis.Constants;

public class ForgetPasswordActivity extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnReset.setOnClickListener(v -> {
            if (binding.email.getEditText().getText().toString().isEmpty()){
                Toast.makeText(this, "Please Provide a email to reset your password", Toast.LENGTH_SHORT).show();
            } else {
                Constants.auth().sendPasswordResetEmail(binding.email.getEditText().getText().toString())
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Please Check Your Email", Toast.LENGTH_SHORT).show();
                            binding.email.getEditText().setText("");
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
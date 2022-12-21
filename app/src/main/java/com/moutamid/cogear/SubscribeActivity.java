package com.moutamid.cogear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.moutamid.cogear.databinding.ActivitySubscribeBinding;
import com.moutamid.cogear.utilis.Constants;

import java.util.HashMap;
import java.util.Map;

public class SubscribeActivity extends AppCompatActivity {

    ActivitySubscribeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscribeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        
        binding.btnReturn.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        
        binding.btnNext.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("isSubscribe", true);
            Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                    .updateChildren(map)
                    .addOnSuccessListener(unused -> {
                        startActivity(new Intent(SubscribeActivity.this, CreateEventActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
        
    }
}
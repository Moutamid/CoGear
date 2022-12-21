package com.moutamid.cogear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.cogear.databinding.ActivityDetailEventBinding;

public class DetailEventActivity extends AppCompatActivity {
    ActivityDetailEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
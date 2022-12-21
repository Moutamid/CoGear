package com.moutamid.cogear;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.chip.Chip;
import com.google.firebase.database.core.operation.Merge;
import com.google.firebase.database.core.operation.Operation;
import com.moutamid.cogear.adapters.InterestsAdater;
import com.moutamid.cogear.databinding.ActivitySelectInterestBinding;
import com.moutamid.cogear.listners.InterestClickListner;
import com.moutamid.cogear.utilis.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectInterestActivity extends AppCompatActivity {

    ActivitySelectInterestBinding binding;
    InterestsAdater adater;
    String[] interestList = {
            "No Poverty", "Zero Hunger", "Good Health & well being", "Quality Education",
            "Gender Equality", "Clean Water and Sanitation", "Affordable and clean energy", "Decent Work and economic growth",
            "Industry innovation and infrastructure", "Reduced in qualities", "Sustainable cities and communities", "Responsible consumption and production",
            "Climate action", "Life below water", "Life on land", "Peace, justice and strong institutions"
    };
    ArrayList<String> interests;
    ArrayList<String> region;
    String selectedInterest = "", selectedRegion = "";

    ProgressDialog progressDialog;

    InterestClickListner clickListner = new InterestClickListner() {
        @Override
        public void onClick(String name, boolean selected) {
            if (selected) {
                interests.add(name);
            } else {
                interests.remove(interests.indexOf(name));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectInterestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        interests = new ArrayList<>();
        region = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        binding.interests.setLayoutManager(new GridLayoutManager(this, 4));
        binding.interests.setHasFixedSize(false);

        adater = new InterestsAdater(this, interestList, clickListner);
        binding.interests.setAdapter(adater);

        binding.btnComplete.setOnClickListener(v -> {
            getRegion();
            if (interests.size() == 0) {
                Toast.makeText(this, "Please Select any Interest", Toast.LENGTH_SHORT).show();
            } else {
                if (region.size() == 0) {
                    Toast.makeText(this, "Please Select any Region", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    for(String s : interests){
                        selectedInterest = selectedInterest + s + " } ";
                    }
                    for(String r : region){
                        selectedRegion = selectedRegion + r + " } ";
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("interests", selectedInterest);
                    map.put("region", selectedRegion);

                    Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                        .updateChildren(map)
                        .addOnSuccessListener(unused -> {
                            progressDialog.dismiss();
                            startActivity(new Intent(SelectInterestActivity.this, FeedActivity.class));
                            finish();
                        }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                        });
                }
            }
        });
    }

    private void getRegion() {
        region.clear();
        for (int i = 0; i < binding.regionChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) binding.regionChipGroup.getChildAt(i);
            if (chip.isChecked()){
                region.add(chip.getText().toString());
            }
        }
    }
}
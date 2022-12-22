package com.moutamid.cogear;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.cogear.databinding.ActivityDetailEventBinding;
import com.moutamid.cogear.models.EventModel;
import com.moutamid.cogear.models.UserModel;
import com.moutamid.cogear.utilis.Constants;

import java.util.HashMap;
import java.util.Map;

public class DetailEventActivity extends AppCompatActivity {
    ActivityDetailEventBinding binding;
    String ID, name;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ID = getIntent().getStringExtra("ID");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding you into the event...");
        progressDialog.setCancelable(false);

        Constants.databaseReference().child("events").child(ID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        EventModel model = snapshot.getValue(EventModel.class);
                        binding.title.setText(model.getTitle());
                        name = model.getTitle();
                        Glide.with(DetailEventActivity.this).load(model.getImage()).into(binding.image);
                        binding.description.setText(model.getDescription());
                        binding.location.setText(model.getCity());
                        binding.flags.setText(model.getCategory());
                        binding.events.setText("" + model.getMembers());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        binding.btnAttend.setOnClickListener(v -> {
            progressDialog.show();
            Map<String, Object> update = new HashMap<>();

            Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                    .get().addOnSuccessListener(dataSnapshot -> {
                        if (dataSnapshot.exists()) {
                            int i = dataSnapshot.getValue(UserModel.class).getNumberOfEvents() + 1;
                            update.put("numberOfEvents", i);
                            Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                                    .updateChildren(update).addOnSuccessListener(s -> {
                                        addEvents();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(DetailEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(DetailEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void addEvents() {
        Map<String, Object> events = new HashMap<>();
        events.put("ID", ID);
        Constants.databaseReference().child("userEvents").child(Constants.auth().getCurrentUser().getUid())
                .push().setValue(events)
                .addOnSuccessListener(unused -> {
                    updateMember();
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(DetailEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateMember() {
        Map<String, Object> update = new HashMap<>();
        Constants.databaseReference().child("events").child(ID)
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()){
                        int i = dataSnapshot.getValue(EventModel.class).getMembers() + 1;
                        update.put("members", i);
                        Constants.databaseReference().child("events").child(ID).updateChildren(update)
                                .addOnSuccessListener(unused -> {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(DetailEventActivity.this, ChatActivity.class);
                                    intent.putExtra("ID", ID);
                                    intent.putExtra("name", name);
                                    startActivity(intent);
                                }).addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(DetailEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(DetailEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
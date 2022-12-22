package com.moutamid.cogear.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.cogear.R;
import com.moutamid.cogear.adapters.ChipsAdapter;
import com.moutamid.cogear.databinding.FragmentProfileBinding;
import com.moutamid.cogear.models.UserModel;
import com.moutamid.cogear.utilis.Constants;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    Context context;
    String[] interests = null;
    ChipsAdapter adapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserModel model = snapshot.getValue(UserModel.class);
                                binding.name.setText(model.getName());
                                binding.memberSince.setText("member since " + model.getDate());
                                binding.events.setText(model.getNumberOfEvents() + " events attended");
                                try {
                                    String s = model.getInterests();
                                    interests = s.split(Pattern.quote(" } "));
                                    adapter = new ChipsAdapter(context, interests);
                                    binding.interestRC.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                } catch (Exception e){
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

        binding.interestRC.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        binding.interestRC.setHasFixedSize(false);

        return view;
    }
}
package com.moutamid.cogear.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.cogear.CreateEventActivity;
import com.moutamid.cogear.R;
import com.moutamid.cogear.adapters.EventsAdapter;
import com.moutamid.cogear.databinding.FragmentFeedBinding;
import com.moutamid.cogear.models.EventModel;
import com.moutamid.cogear.utilis.Constants;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
    FragmentFeedBinding binding;
    Context context;
    EventsAdapter adapter;
    ArrayList<EventModel> list;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        list = new ArrayList<>();

        binding.recyclerEvents.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerEvents.setHasFixedSize(false);

        binding.search.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Constants.databaseReference().child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // list.clear();
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        EventModel model = snapshot1.getValue(EventModel.class);
                        list.add(model);
                        adapter = new EventsAdapter(context, list);
                        adapter.notifyDataSetChanged();
                        binding.recyclerEvents.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnCreate.setOnClickListener(v -> {
            startActivity(new Intent(context, CreateEventActivity.class));
        });


        return view;
    }
}
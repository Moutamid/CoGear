package com.moutamid.cogear.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.cogear.R;
import com.moutamid.cogear.adapters.EventsAdapter;
import com.moutamid.cogear.databinding.FragmentAttendedBinding;
import com.moutamid.cogear.models.EventIDModel;
import com.moutamid.cogear.models.EventModel;
import com.moutamid.cogear.utilis.Constants;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class AttendedFragment extends Fragment {
    FragmentAttendedBinding binding;
    Context context;
    EventsAdapter adapter;
    ArrayList<EventModel> eventModelArrayList;
    ArrayList<String> IDS;

    public AttendedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAttendedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        eventModelArrayList = new ArrayList<>();
        IDS = new ArrayList<>();

        binding.recyclerEvents.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerEvents.setHasFixedSize(false);

        Constants.databaseReference().child("userEvents").child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1: snapshot.getChildren()){
                                EventIDModel model = snapshot1.getValue(EventIDModel.class);
                                IDS.add(model.getID());
                            }
                            loadEvents();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }

    private void loadEvents() {
        Set<String> s = new LinkedHashSet<>(IDS);
        IDS.clear();
        IDS.addAll(s);

        for (int i=0; i< IDS.size(); i++) {
            Constants.databaseReference().child("events").child(IDS.get(i))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                EventModel model = snapshot.getValue(EventModel.class);
                                eventModelArrayList.add(model);
                                adapter = new EventsAdapter(context, eventModelArrayList);
                                adapter.notifyDataSetChanged();
                                binding.recyclerEvents.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}
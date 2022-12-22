package com.moutamid.cogear.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.cogear.R;
import com.moutamid.cogear.adapters.NotificationAdapter;
import com.moutamid.cogear.databinding.FragmentNotificationBinding;
import com.moutamid.cogear.models.EventIDModel;
import com.moutamid.cogear.models.NotificationModel;
import com.moutamid.cogear.utilis.Constants;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    FragmentNotificationBinding binding;
    Context context;
    ArrayList<NotificationModel> list;
    ArrayList<EventIDModel> eventIDModelArrayList;
    NotificationAdapter adapter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        list = new ArrayList<>();
        eventIDModelArrayList = new ArrayList<>();

        binding.notificationRC.setLayoutManager(new LinearLayoutManager(context));
        binding.notificationRC.setHasFixedSize(false);

        Constants.databaseReference().child("userEvents").child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        EventIDModel model = snapshot1.getValue(EventIDModel.class);
                        eventIDModelArrayList.add(model);
                    }
                    getNotification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void getNotification() {
        for (int i = 0; i < eventIDModelArrayList.size(); i++) {
            Constants.databaseReference().child("notifications").child(eventIDModelArrayList.get(i).getID())
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if (snapshot.exists()){
                                NotificationModel model = snapshot.getValue(NotificationModel.class);
                                if (!model.getUserID().equals(Constants.auth().getCurrentUser().getUid())) {
                                    list.add(model);
                                    adapter = new NotificationAdapter(context, list);
                                    binding.notificationRC.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}
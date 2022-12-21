package com.moutamid.cogear.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.cogear.CreateEventActivity;
import com.moutamid.cogear.R;
import com.moutamid.cogear.databinding.FragmentFeedBinding;

public class FeedFragment extends Fragment {
    FragmentFeedBinding binding;
    Context context;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        binding.btnCreate.setOnClickListener(v -> {
            startActivity(new Intent(context, CreateEventActivity.class));
        });


        return view;
    }
}
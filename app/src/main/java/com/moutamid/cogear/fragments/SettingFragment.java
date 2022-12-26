package com.moutamid.cogear.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.cogear.EditProfileActivity;
import com.moutamid.cogear.R;
import com.moutamid.cogear.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    Context context;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        binding.editProfileBtn.setOnClickListener(v -> {
            startActivity(new Intent(context, EditProfileActivity.class));
        });

        return view;
    }
}
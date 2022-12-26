package com.moutamid.cogear.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.cogear.EditProfileActivity;
import com.moutamid.cogear.R;
import com.moutamid.cogear.ResetPasswordActivity;
import com.moutamid.cogear.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    Context context;
    Toolbar toolbar;

    public SettingFragment() {
        // Required empty public constructor
    }

    public SettingFragment(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        binding.editProfileBtn.setOnClickListener(v -> {
            startActivity(new Intent(context, EditProfileActivity.class));
        });

        binding.changePassBtn.setOnClickListener(v -> {
            startActivity(new Intent(context, ResetPasswordActivity.class));
        });

        binding.privacyBtn.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
            startActivity(browserIntent);
        });

        binding.notificationBtn.setOnClickListener(v -> {
            toolbar.setTitle("Notification!");
            FragmentManager.findFragment(view).getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
        });

        return view;
    }
}
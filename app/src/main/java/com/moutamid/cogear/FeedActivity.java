package com.moutamid.cogear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.moutamid.cogear.databinding.ActivityFeedBinding;
import com.moutamid.cogear.fragments.FeedFragment;
import com.moutamid.cogear.fragments.NotificationFragment;
import com.moutamid.cogear.fragments.ProfileFragment;
import com.moutamid.cogear.fragments.SettingFragment;
import com.moutamid.cogear.utilis.Constants;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityFeedBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.drawLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.drawLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setItemIconTintList(null);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
        binding.navView.setCheckedItem(R.id.nav_feed);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);

        MenuItem menuProfile = menu.findItem(R.id.profile_toolbar);
        View view = MenuItemCompat.getActionView(menuProfile);

        CircleImageView profileImage = view.findViewById(R.id.toolbar_profile_image);

        Glide.with(this).load(R.drawable.profle).placeholder(R.drawable.profle).into(profileImage);


        profileImage.setOnClickListener(v -> {
            binding.toolbar.setTitle("Profile!");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        });

        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_feed:
                binding.toolbar.setTitle("Feed!");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
                break;
            case R.id.nav_notification:
                binding.toolbar.setTitle("Notification!");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
                break;
            case R.id.nav_profile:
                binding.toolbar.setTitle("Profile!");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_setting:
                binding.toolbar.setTitle("Setting!");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();
                break;
            case R.id.nav_logout:
                Constants.auth().signOut();
                startActivity(new Intent(FeedActivity.this, SplashScreenActivity.class));
                finish();
                break;
            default:
                break;
        }
        binding.drawLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.drawLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
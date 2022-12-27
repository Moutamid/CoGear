package com.moutamid.cogear;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.moutamid.cogear.databinding.ActivitySubscribeBinding;
import com.moutamid.cogear.utilis.Constants;

import java.util.HashMap;
import java.util.Map;

public class SubscribeActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
//    BillingClient billingClient;

    BillingProcessor bp;
    ActivitySubscribeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscribeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();
        
        binding.btnReturn.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        
        binding.btnNext.setOnClickListener(v -> {
            bp.purchase(SubscribeActivity.this, Constants.TWO_HUNDRED_DOLLAR_PRODUCT);
        });
        
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
        if (productId.equals( Constants.TWO_HUNDRED_DOLLAR_PRODUCT)) {
            Toast.makeText(getApplicationContext(), "Purchased", Toast.LENGTH_SHORT).show();
            Map<String, Object> map = new HashMap<>();
            map.put("subscribe", true);
            Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                    .updateChildren(map)
                    .addOnSuccessListener(unused -> {
                        startActivity(new Intent(SubscribeActivity.this, CreateEventActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
        
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(SubscribeActivity.this, "onBillingError: code: " + errorCode + " \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
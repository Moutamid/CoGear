package com.moutamid.cogear;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.moutamid.cogear.adapters.ChatAdapter;
import com.moutamid.cogear.databinding.ActivityChatBinding;
import com.moutamid.cogear.models.ChatModel;
import com.moutamid.cogear.models.NotificationModel;
import com.moutamid.cogear.models.UserModel;
import com.moutamid.cogear.utilis.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    String ID, name;
    ChatAdapter chatAdapter;
    ChatModel modelChat, getModelChat;
    ArrayList<ChatModel> listChat;
    SimpleDateFormat format = new SimpleDateFormat("HH:mm aa");
    Date date;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ID = getIntent().getStringExtra("ID");
        name = getIntent().getStringExtra("name");

        binding.chatRC.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRC.setHasFixedSize(false);

        binding.title.setText(name);

        listChat = new ArrayList<>();

        getChat();

        binding.btnExit.setOnClickListener(v -> {
            startActivity(new Intent(this, FeedActivity.class));
            finish();
        });

        binding.send.setOnClickListener(v -> {
            if (!binding.message.getText().toString().isEmpty()){
                String msg = binding.message.getText().toString();
                binding.message.setText("");
                date = new Date();
                UID = UUID.randomUUID().toString();
                String d = format.format(date);

                Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelChat = new ChatModel(msg, d,
                                Constants.auth().getCurrentUser().getUid(),
                                snapshot.getValue(UserModel.class).getImage(),
                                date.getTime());
                        Constants.databaseReference().child("chat").child(ID)
                                .child(UID)
                                .setValue(modelChat).addOnSuccessListener(unused -> {
                                    binding.message.setText("");
                                    notification();
                                }).addOnFailureListener(e -> {});
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void notification() {
        Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            UserModel model = snapshot.getValue(UserModel.class);
                            NotificationModel notificationModel = new NotificationModel(
                                    Constants.auth().getCurrentUser().getUid(),
                                    ID,
                                    model.getName(),
                                    name,
                                    date.getTime()
                            );
                            Constants.databaseReference().child("notifications").child(ID).push().setValue(notificationModel)
                                    .addOnSuccessListener(unused -> {}).addOnFailureListener(e->{});
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getChat() {
        Constants.databaseReference().child("chat").child(ID)
                .addChildEventListener(new ChildEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){
                            getModelChat = snapshot.getValue(ChatModel.class);
                            listChat.add(getModelChat);
                            listChat.sort(Comparator.comparing(ChatModel::getTimestamps));
                            chatAdapter = new ChatAdapter(ChatActivity.this, listChat);
                            binding.chatRC.setAdapter(chatAdapter);
                            chatAdapter.notifyItemInserted(listChat.size()-1);
                            binding.chatRC.scrollToPosition(listChat.size()-1);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()) {
                            chatAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
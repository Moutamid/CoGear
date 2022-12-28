package com.moutamid.cogear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.cogear.R;
import com.moutamid.cogear.models.ChatModel;
import com.moutamid.cogear.utilis.Constants;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatVH> {

    private final Context context;
    private final ArrayList<ChatModel> list;

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    public ChatAdapter(Context context, ArrayList<ChatModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
        }
        return new ChatVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatVH holder, int position) {
        ChatModel modelAndroid = list.get(position);

        holder.message_chat.setText(modelAndroid.getMessage());
        holder.time_chat.setText(modelAndroid.getTime());
        Glide.with(context).load(modelAndroid.getImage()).placeholder(R.drawable.profile_icon).into(holder.sender_img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed in user
        return Constants.auth().getCurrentUser().getUid().equals(list.get(position).getUserID()) ? MSG_TYPE_RIGHT : MSG_TYPE_LEFT;
    }

    public class ChatVH extends RecyclerView.ViewHolder{
        private final ImageView sender_img;
        private final TextView message_chat;
        private final TextView time_chat;
        public ChatVH(@NonNull View itemView) {
            super(itemView);
            sender_img = itemView.findViewById(R.id.sender_img);
            message_chat = itemView.findViewById(R.id.message_chat);
            time_chat = itemView.findViewById(R.id.time_chat);
        }
    }
}

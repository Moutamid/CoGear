package com.moutamid.cogear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.cogear.R;
import com.moutamid.cogear.models.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationVH> {

    Context context;
    ArrayList<NotificationModel> list;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notificaion_card, parent, false);
        return new NotificationVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationVH holder, int position) {
        NotificationModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.event.setText(model.getEventName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationVH extends RecyclerView.ViewHolder{
        TextView name, event;
        public NotificationVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            event = itemView.findViewById(R.id.event);
        }
    }
}

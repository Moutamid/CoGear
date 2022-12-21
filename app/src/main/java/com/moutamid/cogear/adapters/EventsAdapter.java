package com.moutamid.cogear.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.cogear.DetailEventActivity;
import com.moutamid.cogear.R;
import com.moutamid.cogear.models.EventModel;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventVH> {
    Context context;
    ArrayList<EventModel> eventList;

    public EventsAdapter(Context context, ArrayList<EventModel> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_card, parent, false);
        return new EventVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventVH holder, int position) {
        EventModel model = eventList.get(holder.getAdapterPosition());

        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        holder.location.setText(model.getCity());
        holder.flag.setText(model.getCategory());
        holder.member.setText(""+model.getMembers());

        Glide.with(context).load(model.getImage()).into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailEventActivity.class);
            intent.putExtra("ID", model.getID());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventVH extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, desc, location, member, flag;
        public EventVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.eventTitle);
            member = itemView.findViewById(R.id.totalMembers);
            location = itemView.findViewById(R.id.location);
            flag = itemView.findViewById(R.id.flags);
            desc = itemView.findViewById(R.id.detail);
        }
    }
}

package com.moutamid.cogear.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.cogear.DetailEventActivity;
import com.moutamid.cogear.R;
import com.moutamid.cogear.models.EventModel;

import java.util.ArrayList;
import java.util.Collection;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventVH> implements Filterable {
    Context context;
    ArrayList<EventModel> eventList, listAll;

    public EventsAdapter(Context context, ArrayList<EventModel> eventList) {
        this.context = context;
        this.eventList = eventList;
        listAll = new ArrayList<>(eventList);
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<EventModel> filterList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filterList.addAll(listAll);
            } else {
                for (EventModel listModel : listAll){
                    if (
                            listModel.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            listModel.getCity().toLowerCase().contains(charSequence.toString().toLowerCase())
                    ){
                        filterList.add(listModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        //run on Ui thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            eventList.clear();
            eventList.addAll((Collection<? extends EventModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

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

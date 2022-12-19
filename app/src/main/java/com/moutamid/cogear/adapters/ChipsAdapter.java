package com.moutamid.cogear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.cogear.R;

public class ChipsAdapter extends RecyclerView.Adapter<ChipsAdapter.ChipsVH> {

    Context context;
    String[] interest;

    public ChipsAdapter(Context context, String[] interest) {
        this.context = context;
        this.interest = interest;
    }

    @NonNull
    @Override
    public ChipsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chips_layout, parent, false);
        return new ChipsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipsVH holder, int position) {
        holder.text.setText(interest[position].trim());
    }

    @Override
    public int getItemCount() {
        return interest.length;
    }

    public class ChipsVH extends RecyclerView.ViewHolder{
        TextView text;
        public ChipsVH(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}

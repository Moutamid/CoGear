package com.moutamid.cogear.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.cogear.R;
import com.moutamid.cogear.listners.InterestClickListner;

import java.util.Locale;
import java.util.Random;

public class InterestsAdater extends RecyclerView.Adapter<InterestsAdater.InterestVH> {

    Context context;
    String[] interestList;
    InterestClickListner clickListner;

    public InterestsAdater(Context context, String[] interestList, InterestClickListner clickListner) {
        this.context = context;
        this.interestList = interestList;
        this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public InterestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.interest_card, parent, false);
        return new InterestVH(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull InterestVH holder, int position) {
        holder.card.setCardBackgroundColor(holder.itemView.getResources().getColor(getRandomColor(), null));
        holder.title.setText(interestList[position].toUpperCase(Locale.ROOT));

        holder.itemView.setOnClickListener(v -> {
            if (holder.selected) {
                holder.selected = false;
                holder.card.setStrokeColor(0);
                clickListner.onClick(interestList[position], false);
            } else {
                holder.selected = true;
                holder.card.setStrokeColor(context.getResources().getColor(R.color.dark));
                clickListner.onClick(interestList[position], true);
            }
        });

    }

    private int getRandomColor() {
        Random generator = new Random();
        int[] colors = {R.color.red, R.color.yellow, R.color.greenL, R.color.redD, R.color.blueL, R.color.mehroon, R.color.pink, R.color.green, R.color.blueD, R.color.blue, R.color.orange, R.color.yellowD};
        int randomIndex = generator.nextInt(colors.length);
        return colors[randomIndex];
    }

    @Override
    public int getItemCount() {
        return interestList.length;
    }

    public class InterestVH extends RecyclerView.ViewHolder {
        TextView title;
        MaterialCardView card;
        boolean selected = false;

        public InterestVH(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            title = itemView.findViewById(R.id.title);
        }
    }
}

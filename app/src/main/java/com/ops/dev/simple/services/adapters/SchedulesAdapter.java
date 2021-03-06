package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.models.SchedulesModel;

import java.util.List;

public class SchedulesAdapter extends RecyclerView.Adapter<SchedulesAdapter.ViewHolder>{

    private Context mContext;
    private List<SchedulesModel> mData;

    public SchedulesAdapter(Context mContext, List<SchedulesModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_schedules, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SchedulesModel schedulesModel = mData.get(position);
        TextView day = holder.day;
        TextView number = holder.time;

        day.setText(schedulesModel.getDay());
        number.setText(schedulesModel.getTime());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day);
            time = itemView.findViewById(R.id.time);
        }
    }
}
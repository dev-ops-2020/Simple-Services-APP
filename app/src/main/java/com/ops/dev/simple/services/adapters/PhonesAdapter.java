package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.models.PhonesModel;

import java.util.List;

public class PhonesAdapter extends RecyclerView.Adapter<PhonesAdapter.ViewHolder>{

    private Context mContext;
    private List<PhonesModel> mData;

    public PhonesAdapter(Context mContext, List<PhonesModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int pos) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.__card_phones, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhonesModel phone = mData.get(position);
        TextView name = holder.name;
        TextView number = holder.number;

        name.setText(phone.getName());
        number.setText(phone.getNumber());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
        }
    }
}
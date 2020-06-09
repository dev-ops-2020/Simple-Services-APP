package com.ops.dev.simple.services.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.models.CommentsModel;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context mContext;
    private List<CommentsModel> mData;

    public CommentsAdapter(Context mContext, List<CommentsModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.__card_comments, parent, false);
        final ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
        CommentsModel comment = mData.get(pos);
        ImageView picture = holder.picture;
        TextView date = holder.date;
        TextView user = holder.user;
        TextView commentary = holder.comment;

        Glide
                .with(mContext)
                .load(comment.getPictureUser())
                .transform(new RoundedCorners(R.dimen.min_margin))
                .into(picture);
        date.setText(comment.getDate());
        user.setText(comment.getNameUser());
        commentary.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView picture;
        TextView date, user, comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.picture);
            date = itemView.findViewById(R.id.date);
            user = itemView.findViewById(R.id.user);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}

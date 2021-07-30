package com.example.museum.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.museum.R;
import com.example.museum.activities.HomeActivity;
import com.example.museum.models.Cover;
import com.example.museum.models.Journal;
import com.example.museum.models.Piece;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private final Context context;
    private final List<Journal> journals;

    // adapter for journals in calendar
    public CalendarAdapter(Context context, List<Journal> journals) {
        this.context = context;
        this.journals = journals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Journal journal = journals.get(position);
        holder.bind(journal);
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCover;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        public void bind(Journal journal) {
            Cover cover = journal.getCover();
            Piece piece = cover.getPiece();
            Glide.with(context).load(piece.getImageUrl()).into(ivCover);
            tvTitle.setText(journal.getTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Journal journal = journals.get(position);
                        HomeActivity activity = (HomeActivity) context;
                        activity.readJournal(journal);
                    }
                }
            });
        }
    }
}

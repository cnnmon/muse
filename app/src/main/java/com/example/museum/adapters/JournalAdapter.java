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
import com.example.museum.models.Journal;

import org.json.JSONException;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {

    private final Context context;
    private final List<Journal> journals;

    public JournalAdapter(Context context, List<Journal> journals) {
        this.context = context;
        this.journals = journals;
    }

    @NonNull
    @Override
    public JournalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_journal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalAdapter.ViewHolder holder, int position) {
        Journal journal = journals.get(position);
        try {
            holder.bind(journal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCover;
        TextView tvTitle;
        TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        public void bind(Journal journal) throws JSONException {
            // TODO: Connect cover JSON file
            // Piece piece = Piece.fromJson(journal.getCover());
            Glide.with(context).load(journal.getImageUrl()).into(ivCover);
            tvTitle.setText(journal.getTitle());
            Format f = new SimpleDateFormat("MM/dd/yy");
            String date = f.format(journal.getCreatedAt());
            tvDate.setText(date);
        }
    }
}

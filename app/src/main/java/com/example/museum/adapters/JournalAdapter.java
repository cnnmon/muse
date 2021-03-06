package com.example.museum.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.museum.R;
import com.example.museum.activities.HomeActivity;
import com.example.museum.models.Cover;
import com.example.museum.models.Journal;
import com.example.museum.models.Piece;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {

    private final Context context;
    private final List<Journal> journals;
    private static final int BUTTON_VIEW = 1, JOURNAL_VIEW = 2;

    public JournalAdapter(Context context, List<Journal> journals) {
        this.context = context;
        this.journals = journals;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return BUTTON_VIEW;
        return JOURNAL_VIEW;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_journal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Journal journal = journals.get(position);
        switch (holder.getItemViewType()){
            case BUTTON_VIEW:
                holder.setButton();
                break;
            case JOURNAL_VIEW:
                holder.bind(journal);
                break;
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

        public void bind(Journal journal) {
            Cover cover = journal.getCover();
            Piece piece = cover.getPiece();
            Glide.with(context).load(piece.getImageUrl()).into(ivCover);
            tvTitle.setText(journal.getTitle());
            tvDate.setText(journal.getSimpleDate());

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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // build dialog to prompt user whether to confirm deletion
                    Resources r = itemView.getResources();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    String confirmation = String.format(
                            r.getString(R.string.display_delete_confirmation),
                            journal.getTitle());
                    builder.setMessage(confirmation)
                            .setPositiveButton(r.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    HomeActivity activity = (HomeActivity) context;
                                    activity.removeJournal(journal);
                                }
                            })
                            .setNegativeButton(r.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    Dialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.outlined_white);
                    dialog.show();
                    return true;
                }
            });
        }

        // uses dummy journal data to create a "new journal" button in 0 spot
        public void setButton() {
            tvTitle.setText(itemView.getResources().getString(R.string.untitled));
            tvTitle.setTextColor(context.getColor(R.color.gray));
            tvDate.setText(Journal.getSimpleDateCurrent());

            ImageView border = itemView.findViewById(R.id.border);
            Glide.with(context).load(context.getDrawable(R.drawable.outlined_dashed_transparent)).into(border);
            Glide.with(context).load(context.getDrawable(R.drawable.new_journal)).into(ivCover);

            LinearLayout textLayout = itemView.findViewById(R.id.textLayout);
            textLayout.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity homeActivity = (HomeActivity) context;
                    homeActivity.createJournal();
                }
            });
        }
    }
}

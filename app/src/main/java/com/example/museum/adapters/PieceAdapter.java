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
import com.example.museum.models.Piece;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.ViewHolder> {

    private Context context;
    private List<Piece> pieces;

    public PieceAdapter(Context context, List<Piece> pieces) {
        this.context = context;
        this.pieces = pieces;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_piece, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Piece piece = pieces.get(position);
        holder.bind(piece);
    }

    @Override
    public int getItemCount() {
        return pieces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPiece;
        TextView tvTitle;
        TextView tvTags;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivPiece = itemView.findViewById(R.id.ivPiece);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTags = itemView.findViewById(R.id.tvTags);
        }

        public void bind(Piece piece) {
            tvTitle.setText(String.format(piece.getTitle(), " - ", piece.getArtist()));
            tvTags.setText(piece.getTags().toString());
            Glide.with(context).load(piece.getImageURL()).into(ivPiece);
        }
    }
}

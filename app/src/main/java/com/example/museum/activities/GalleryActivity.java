package com.example.museum.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.museum.R;
import com.example.museum.adapters.PieceAdapter;
import com.example.museum.models.Piece;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    List<Piece> pieces;
    RecyclerView rvPieces;
    PieceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        pieces = new ArrayList<>();

        // initialize layout
        adapter = new PieceAdapter(this, pieces);
        rvPieces = findViewById(R.id.rvPieces);
        rvPieces.setLayoutManager(new LinearLayoutManager(this));
        rvPieces.setAdapter(adapter);

        pieces.addAll(Parcels.unwrap(getIntent().getParcelableExtra("pieces")));
        adapter.notifyDataSetChanged();
    }
}
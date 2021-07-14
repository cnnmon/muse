package com.example.museum;

import com.example.museum.models.Piece;

public interface Callback extends Runnable {
    public void run(Piece piece);
}
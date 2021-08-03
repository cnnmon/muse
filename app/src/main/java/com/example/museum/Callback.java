package com.example.museum;

import com.example.museum.models.Piece;

import java.util.List;
import java.util.Map;

public interface Callback {

    void run(Map<String, List<Piece>> options);
}
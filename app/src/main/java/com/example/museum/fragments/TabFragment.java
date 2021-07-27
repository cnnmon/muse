package com.example.museum.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.museum.ParseApplication;
import com.example.museum.R;
import com.example.museum.activities.OptionsActivity;
import com.example.museum.models.Cover;
import com.example.museum.models.Piece;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends Fragment {

    private static final String KEY = "key";
    private static final String PIECES = "pieces";

    private String keyword;
    private List<Piece> pieces;

    public TabFragment() {
        // Required empty public constructor
    }

    public static TabFragment newInstance(String keyword, List<Piece> pieces) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(KEY, keyword);
        args.putParcelable(PIECES, Parcels.wrap(pieces));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            keyword = bundle.getString(KEY);
            pieces = Parcels.unwrap(bundle.getParcelable(PIECES));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        OptionsActivity activity = (OptionsActivity) getActivity();

        ImageView ivCoverA = view.findViewById(R.id.ivCoverA);
        ImageView ivCoverB = view.findViewById(R.id.ivCoverB);
        ImageView ivCoverC = view.findViewById(R.id.ivCoverC);
        List<ImageView> images = new ArrayList<>();
        images.add(ivCoverA);
        images.add(ivCoverB);
        images.add(ivCoverC);

        for (int i = 0; i < images.size() && i < pieces.size(); i += 1) {
            int index = i;
            ImageView image = images.get(index);
            Piece piece = pieces.get(index);
            Glide.with(this).load(pieces.get(index).getImageUrl()).into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("OptionFragment", piece.getTitle());
                    Cover cover = activity.cover;
                    cover.setActiveCover(keyword, index);
                    ParseApplication.updateActiveCover(activity.journal, activity.cover, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            activity.initActiveCover(piece);
                        }
                    });
                }
            });
        }
    }
}
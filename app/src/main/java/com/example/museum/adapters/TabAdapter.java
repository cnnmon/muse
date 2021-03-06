package com.example.museum.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.museum.TRApplication;
import com.example.museum.fragments.TabFragment;
import com.example.museum.models.Cover;
import com.example.museum.models.Piece;

import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {

    private Cover cover;

    public TabAdapter(FragmentManager fm, Cover cover) {
        super(fm);
        this.cover = cover;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String keyword = cover.getKeywords().get(position);
        List<Piece> pieces = cover.getOptions(keyword);
        TabFragment tab = TabFragment.newInstance(keyword, pieces);
        return tab;
    }

    @Override
    public int getCount() {
        return TRApplication.MAX_OPTIONS;
    }

}

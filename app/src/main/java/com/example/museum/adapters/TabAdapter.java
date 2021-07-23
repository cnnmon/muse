package com.example.museum.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.museum.TRApplication;
import com.example.museum.fragments.OptionFragment;
import com.example.museum.models.Cover;
import com.example.museum.models.Piece;

import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {

    private Context context;
    private Cover cover;

    public TabAdapter(Context c, FragmentManager fm, Cover cover) {
        super(fm);
        context = c;
        this.cover = cover;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String keyword = cover.getKeywords().get(position);
        List<Piece> pieces = cover.getOptions(keyword);
        OptionFragment tab = OptionFragment.newInstance(keyword, pieces);
        return tab;
    }

    @Override
    public int getCount() {
        return TRApplication.MAX_OPTIONS;
    }

}

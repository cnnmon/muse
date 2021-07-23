package com.example.museum.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.museum.R;
import com.example.museum.TRApplication;
import com.example.museum.adapters.TabAdapter;
import com.example.museum.models.Cover;
import com.example.museum.models.Journal;
import com.example.museum.models.Piece;
import com.google.android.material.tabs.TabLayout;

import org.parceler.Parcels;

import java.util.List;

public class OptionsActivity extends AppCompatActivity {

    public Journal journal;
    public Cover cover;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView ivCover;
    private TextView tvName;
    private TextView tvArtist;
    private TextView tvDetails;
    private TextView tvDetailsKeywords;
    private List<String> keywords;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        journal = Parcels.unwrap(getIntent().getParcelableExtra(Journal.class.getSimpleName()));
        cover = journal.getCover();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ivCover = findViewById(R.id.ivCover);
        tvName = findViewById(R.id.tvName);
        tvArtist = findViewById(R.id.tvArtist);
        tvDetails = findViewById(R.id.tvDetails);
        tvDetailsKeywords = findViewById(R.id.tvDetailsKeywords);

        keywords = cover.getKeywords();
        tvDetailsKeywords.setText("Your keywords for this entry: " +
                keywords.get(0) + ", " + keywords.get(1) + ", and " + keywords.get(2) +
                ". The above piece was chosen based on one of those.");

        initActiveCover(cover.getPiece());
        initTabs();
    }

    @SuppressLint("SetTextI18n")
    public void initActiveCover(Piece piece) {
        tvName.setText(piece.getTitle());
        tvArtist.setText(piece.getArtist());
        tvDetails.setText(piece.getMedium() + ", " + piece.getObjectDate());
        Glide.with(this).load(piece.getImageUrl()).into(ivCover);
    }

    private void initTabs() {
        for (int i = 0; i < cover.getOptionsSize() && i < TRApplication.MAX_OPTIONS; i += 1) {
            tabLayout.addTab(tabLayout.newTab().setText(keywords.get(i)));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabAdapter adapter = new TabAdapter(this, getSupportFragmentManager(), cover);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }

        });

        // select current index
        int index = keywords.indexOf(cover.getActiveKeyword());
        tabLayout.selectTab(tabLayout.getTabAt(index));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent();
            i.putExtra(Journal.class.getSimpleName(), Parcels.wrap(journal));
            setResult(RESULT_OK, i);
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }
}
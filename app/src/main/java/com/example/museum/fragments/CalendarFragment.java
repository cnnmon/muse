package com.example.museum.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.museum.R;
import com.example.museum.activities.HomeActivity;
import com.example.museum.adapters.CalendarAdapter;
import com.example.museum.models.Journal;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalendarFragment extends Fragment {

    private HomeActivity activity;
    private RecyclerView rvJournals;

    public CalendarFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialCalendarView calendarView = view.findViewById(R.id.calendarView);
        activity = (HomeActivity) getActivity();
        calendarView.addDecorator(new DayDecorator(activity.datedJournals));

        List<Journal> dayJournals = new ArrayList<>();
        CalendarAdapter adapter = new CalendarAdapter(activity, dayJournals);

        rvJournals = view.findViewById(R.id.rvJournals);
        rvJournals.setAdapter(adapter);
        rvJournals.setLayoutManager(new GridLayoutManager(activity, 1));

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Map<String, List<Journal>> journals = activity.datedJournals;
                List<Journal> journalsAtDate = journalAtDate(journals, date);
                if (journalsAtDate != null) {
                    rvJournals.setVisibility(View.VISIBLE);
                    dayJournals.clear();
                    dayJournals.addAll(journalsAtDate);
                    adapter.notifyDataSetChanged();
                } else {
                    dayJournals.clear();
                    rvJournals.setVisibility(View.GONE);
                }
            }
        });
    }

    public List<Journal> journalAtDate(Map<String, List<Journal>> journals, CalendarDay day) {
        return journals.get(Journal.getSimpleDate(day.getDate()));
    }

    public class DayDecorator implements DayViewDecorator {

        private Map<String, List<Journal>> journals;

        public DayDecorator(Map<String, List<Journal>> journals) {
            this.journals = journals;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return journalAtDate(journals, day) != null;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.outlined_calendar));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == activity.REQUEST_CODE && resultCode == activity.RESULT_OK) {
            activity.queryJournals();
            rvJournals.setVisibility(View.GONE);
        }
    }

}
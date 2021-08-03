package com.example.museum.presenters;

import com.example.museum.ParseApplication;
import com.example.museum.contracts.HomeContract;
import com.example.museum.models.Journal;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View view;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void queryJournals(List<Journal> allJournals, Map<String, List<Journal>> datedJournals) {
        view.showProgress();
        ParseApplication.get().queryJournals(new FindCallback<Journal>() {
            @Override
            public void done(List<Journal> journals, ParseException e) {
                view.hideProgress();
                if (e != null) {
                    view.error();
                    return;
                }
                allJournals.addAll(journals);

                // generate hashmap so decorator has easier time processing data
                for (int i = 0; i < journals.size(); i += 1) {
                    Journal j = journals.get(i);
                    String date = j.getSimpleDate();

                    if (datedJournals.containsKey(date)) {
                        datedJournals.get(date).add(j);
                    } else {
                        List<Journal> journalsAtDate = new ArrayList<>();
                        journalsAtDate.add(j);
                        datedJournals.put(date, journalsAtDate);
                    }
                }

                view.updateJournals();
            }
        });
    }

    @Override
    public void removeJournal(Journal journal) {
        ParseApplication.get().deleteJournal(journal, new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                view.refresh();
            }
        });
    }
}

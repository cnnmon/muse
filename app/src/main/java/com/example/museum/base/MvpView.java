package com.example.museum.base;

/**
 * Base view which act as a View in Model-View-Presenter.
 * This interface will be extended by more specific interface and which will be implemented
 * in any class that want to act as a MVP view.
 */
public interface MvpView<T extends MvpPresenter> {

    void setPresenter(T presenter);

    void showProgress();

    void hideProgress();

}

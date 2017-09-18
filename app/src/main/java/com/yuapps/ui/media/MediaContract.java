package com.yuapps.ui.media;

import android.support.annotation.NonNull;

import com.popularmovies.vpaliy.domain.configuration.SortType;
import com.popularmovies.vpaliy.domain.model.MediaCover;
import com.popularmovies.vpaliy.popularmoviesapp.ui.base.BasePresenter;
import com.popularmovies.vpaliy.popularmoviesapp.ui.base.BaseView;

import java.util.List;

public interface MediaContract {

    interface View extends BaseView<Presenter> {
        void attachPresenter(@NonNull Presenter presenter);
        void showMedia(@NonNull SortType sortType, @NonNull List<MediaCover> movies);
        void appendMedia(@NonNull SortType sortType, @NonNull List<MediaCover> movies);
        void showErrorMessage();
        void showEmptyMessage();
    }

    interface Presenter extends BasePresenter<View> {
        void attachView(@NonNull View view);
        void requestRefresh(@NonNull SortType sortType);
        void requestMore(@NonNull SortType sortType);
        void start(SortType sortType);
        void stop();
    }
}

package com.yuapps.ui.media;


import android.graphics.Color;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class PersonalFragment extends MediaFragment {

    @Inject @Override @Movies
    public void attachPresenter(@NonNull @Movies MediaContract.Presenter presenter) {
        this.presenter=presenter;
        this.presenter.attachView(this);
    }

    @Override
    public void initializeDependencies() {
        DaggerViewComponent.builder()
                .applicationComponent(App.appInstance().appComponent())
                .presenterModule(new PresenterModule())
                .build().inject(this);
    }

    @Override
    public List<SortType> getSortTypes() {
        return Arrays.asList(SortType.POPULAR, SortType.LATEST,
                SortType.NOW_PLAYING, SortType.UPCOMING,SortType.TOP_RATED);
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.PERSONAL;
    }

    @Override
    public int getColor(SortType sortType) {
        return Color.parseColor("#00695c");
    }

    @Override
    public String getTitle(SortType sortType) {
        switch (sortType){
            case POPULAR:
                return getString(R.string.popular_media);
            case LATEST:
                return getString(R.string.latest_media);
            case NOW_PLAYING:
                return getString(R.string.now_playing_media);
            case UPCOMING:
                return getString(R.string.upcoming_media);
            case TOP_RATED:
                return getString(R.string.top_rated_media);
            default:
                throw new IllegalArgumentException();
        }
    }
}

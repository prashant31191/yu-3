package com.yuapps.ui.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.popularmovies.vpaliy.data.utils.scheduler.BaseSchedulerProvider;
import com.popularmovies.vpaliy.domain.configuration.SortType;
import com.popularmovies.vpaliy.domain.model.MediaCover;
import com.popularmovies.vpaliy.domain.repository.ICoverRepository;
import com.popularmovies.vpaliy.popularmoviesapp.di.scope.ViewScope;
import com.popularmovies.vpaliy.popularmoviesapp.ui.media.MediaContract.View;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

@ViewScope
public class MediaPresenter implements MediaContract.Presenter{

    private final ICoverRepository<MediaCover> iRepository;
    private final BaseSchedulerProvider schedulerProvider;
    private final CompositeSubscription subscriptions;
    private View view;

    @Inject
    public MediaPresenter(@NonNull ICoverRepository<MediaCover> iRepository,
                          @NonNull BaseSchedulerProvider schedulerProvider){
        this.iRepository=iRepository;
        this.schedulerProvider=schedulerProvider;
        this.subscriptions=new CompositeSubscription();
    }

    @Override
    public void attachView(@NonNull View view) {
        checkNotNull(view);
        this.view=view;
    }

    @Override
    public void start(SortType sortType) {
       startLoading(sortType);
    }

    @Override
    public void stop() {
        view=null;
        if(subscriptions.hasSubscriptions()){
            subscriptions.clear();
        }
    }

    @Override
    public void requestRefresh(@NonNull SortType sortType) {
        startLoading(sortType);
    }

    private void startLoading(SortType sortType){
        subscriptions.add(iRepository.get(sortType)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(movies->processData(sortType,movies),
                        this::handleErrorMessage));
    }

    private void processData(SortType sortType, List<MediaCover> movieList){
        if(movieList!=null) {
            if (!movieList.isEmpty()) {
                view.showMedia(sortType,movieList);
                return;
            }
        }
        view.showEmptyMessage();
    }

    @Override
    public void requestMore(@NonNull SortType sortType) {
            subscriptions.add(iRepository.requestMore(sortType)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(movies->appendData(sortType,movies),
                            this::handleErrorMessage));
    }

    private void appendData(SortType sortType,@Nullable List<MediaCover> movieList){
        if(movieList!=null) {
            if (!movieList.isEmpty()) {
                view.appendMedia(sortType,movieList);
            }
        }
    }

    private void handleErrorMessage(Throwable throwable){
        throwable.printStackTrace();
        view.showErrorMessage();
    }
}

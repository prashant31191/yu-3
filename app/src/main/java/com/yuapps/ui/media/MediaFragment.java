package com.yuapps.ui.media;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popularmovies.vpaliy.domain.configuration.SortType;
import com.popularmovies.vpaliy.domain.model.MediaCover;
import com.popularmovies.vpaliy.popularmoviesapp.R;
import com.popularmovies.vpaliy.popularmoviesapp.ui.base.AbstractMediaAdapter;
import com.popularmovies.vpaliy.popularmoviesapp.ui.base.BaseFragment;
import com.popularmovies.vpaliy.popularmoviesapp.ui.base.MediaType;
import com.popularmovies.vpaliy.popularmoviesapp.ui.base.bus.events.RequestMoreEvent;
import com.popularmovies.vpaliy.popularmoviesapp.ui.media.MediaContract.Presenter;
import com.popularmovies.vpaliy.popularmoviesapp.ui.media.MediaTypeAdapter.MediaTypeWrapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

public abstract class MediaFragment extends BaseFragment
        implements MediaContract.View{

    protected Presenter presenter;
    private Map<SortType,MediaAdapter> mediaAdapters;
    private AbstractMediaAdapter<MediaTypeWrapper> mediaTypeAdapter;
    private CompositeDisposable disposables;

    @BindView(R.id.media_recycler_view)
    protected RecyclerView mediaList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initializeDependencies();
        disposables=new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_media,container,false);
        bind(root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            mediaAdapters=new LinkedHashMap<>();
            mediaTypeAdapter=new MediaTypeAdapter(getContext(),rxBus);
            mediaList.setAdapter(mediaTypeAdapter);
            List<SortType> sortTypes=getSortTypes();
            if(sortTypes!=null) {
                for (SortType sortType : sortTypes) {
                    presenter.start(sortType);
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        disposables.add(rxBus.asFlowable().subscribe(this::processEvent));
    }

    private void processEvent(Object event){
        if(event!=null) {
            if (event instanceof RequestMoreEvent) {
                RequestMoreEvent requestMoreEvent=RequestMoreEvent.class.cast(event);
                presenter.requestMore(requestMoreEvent.sortType());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(disposables!=null) disposables.clear();
    }

    @Override
    public void showEmptyMessage() {
    }

    @Override
    public void showErrorMessage() {
    }

    @Override
    public void showMedia(@NonNull SortType sortType, @NonNull List<MediaCover> media) {
        if(!mediaAdapters.containsKey(sortType)) {
            mediaAdapters.put(sortType,new MediaAdapter(getContext(),rxBus));
        }
        MediaAdapter adapter = mediaAdapters.get(sortType);
        adapter.setData(media);
        MediaTypeWrapper wrapper=MediaTypeWrapper.wrap(getTitle(sortType),sortType,adapter,getMediaType(),getColor(sortType));
        mediaTypeAdapter.addItem(wrapper);
    }


    @Override
    public void appendMedia(@NonNull SortType sortType, @NonNull List<MediaCover> movies) {
        if(!mediaAdapters.containsKey(sortType)){
            throw new IllegalArgumentException("Map does not contain this sort type"+sortType.name());
        }
        mediaAdapters.get(sortType).appendData(movies);
    }

    public abstract String getTitle(SortType sortType);
    public abstract int getColor(SortType sortType);
    public abstract MediaType getMediaType();
    public abstract List<SortType> getSortTypes();
}

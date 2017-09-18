package com.yuapps.ui.media;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.popularmovies.vpaliy.domain.model.MediaCover;
import com.popularmovies.vpaliy.popularmoviesapp.R;
import com.popularmovies.vpaliy.popularmoviesapp.ui.base.AbstractMediaAdapter;
import com.popularmovies.vpaliy.popularmoviesapp.ui.base.bus.RxBus;
import com.popularmovies.vpaliy.popularmoviesapp.ui.base.bus.events.ExposeEvent;
import com.popularmovies.vpaliy.popularmoviesapp.ui.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaAdapter extends AbstractMediaAdapter<MediaCover> {

    public MediaAdapter(@NonNull Context context, @NonNull RxBus rxBus){
        super(context,rxBus);
    }

    public void setData(List<MediaCover> data) {
        this.data = data;
    }

    public class MediaViewHolder extends GenericViewHolder {

        @BindView(R.id.media_poster)
        ImageView posterImage;

        @BindView(R.id.media_title)
        TextView mediaTitle;

        MediaViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            posterImage.setOnClickListener(v -> {
                if(!isLocked()) {
                    lock();
                    Context context=itemView.getContext();
                    Bundle args = new Bundle();
                    MediaCover cover=at(getAdapterPosition());
                    String transitionName=context.getString(R.string.poster_transition_name)+getAdapterPosition();
                    args.putString(Constants.EXTRA_ID, cover.getMediaId());
                    args.putString(Constants.EXTRA_DATA,cover.getMainBackdrop());
                    args.putString(Constants.EXTRA_POSTER_PATH,cover.getPosterPath());
                    args.putString(Constants.EXTRA_TRANSITION_NAME,transitionName);
                    args.putBoolean(Constants.EXTRA_IS_TV,cover.isTvShow());
                    ViewCompat.setTransitionName(posterImage,transitionName);
                    rxBus.send(ExposeEvent.exposeMediaDetails(args, Pair.create(posterImage, transitionName)));
                    unlockAfter(UNLOCK_TIMEOUT);
                }
            });
        }

        @Override
        public void onBindData(){
            MediaCover cover=at(getAdapterPosition());
            mediaTitle.setText(cover.getMovieTitle());
            Glide.with(itemView.getContext())
                    .load(cover.getPosterPath())
                    .priority(Priority.IMMEDIATE)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.drawable.placeholder)
                    .animate(R.anim.fade_in)
                    .into(posterImage);
        }
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root=inflater.inflate(R.layout.adapter_media_item,parent,false);
        return new MediaViewHolder(root);
    }
}

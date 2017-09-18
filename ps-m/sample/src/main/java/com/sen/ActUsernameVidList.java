package com.sen;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.sen.api.AppBase;
import com.sen.api.IApiMethods;
import com.sen.api.UsernameChannelVideos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Adminsss on 08-02-2016.
 */
public class ActUsernameVidList extends ActionBarActivity {

    UsernameChannelVideos.Items obj;

    String part = "id";// for more details = "snippet,id";
    String channelId = "UCq-Fj5jknLsUf-MWSy4_brA";
    String key = "AIzaSyDGDGk8ctTus1DCsWgldVeq2UaUwO7i4WM";
    String order = "date";
    String maxResults = "45";

    String Tag = "-tg--ActUsernameVidList.java---";

    Bundle bundle;

    YouTubeThumbnailView vv;
    GridView gridVideos;
    int width=300,height=600;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_list_videos);
        gridVideos = (GridView) findViewById(R.id.gridVideos);


        bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getString("channelId") != null && bundle.getString("channelId").toString().length() > 1) {
                channelId = bundle.getString("channelId");
            }
        }


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppBase.strBaseUrl)
                .build();
        IApiMethods methods = restAdapter.create(IApiMethods.class);
        Callback callback = new Callback() {
            @SuppressLint("LongLogTag")
            @Override
            public void success(Object o, Response response) {

                Log.v(Tag, "Sucess---1");
                UsernameChannelVideos curators = (UsernameChannelVideos) o;

                Log.v(Tag, "Sucess---1");

                String strData = "";

                strData = curators.getKind() + " \n ";
                Log.v(Tag, "Sucess---1---getKind---" + strData);


                if (curators.getArrayListItems() != null && curators.getArrayListItems().size() > 0) {
                    for (int i = 0; i < curators.getArrayListItems().size(); i++) {
                        strData = strData + "\n" + i + "-->" + curators.getArrayListItems().get(i).getId().getVideoId();
                        Log.v(Tag, "Sucess---2-strData---" + strData);
                        //tvData.setText(i+"-->"+curators.getArrayListItems().get(i).getId().getVideoId());
                    }

                    VideoAdapter videoAdapter = new VideoAdapter(ActUsernameVidList.this, curators.getArrayListItems());
                    gridVideos.setAdapter(videoAdapter);
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.v(Tag, "failure---1--");
            }
        };

        //getUserChannelName
        //getUserChannelVideos
        methods.getUserChannelVideos(part, channelId, key, order, maxResults, callback);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
         width = size.x;
         height = size.y;

    }


    public class VideoAdapter extends BaseAdapter implements YouTubeThumbnailView.OnInitializedListener {
        ArrayList<UsernameChannelVideos.Items> mArrListItems;
        Context mContext;
        LayoutInflater llInflater;
        Map<View, YouTubeThumbnailLoader> mLoaders;


        public VideoAdapter(Context context, ArrayList<UsernameChannelVideos.Items> arrListItems) {
            mArrListItems = arrListItems;
            mContext = context;
            llInflater = LayoutInflater.from(mContext);
            mLoaders = new HashMap<View, YouTubeThumbnailLoader>();

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;


            if (mArrListItems.get(position) != null) {
                obj = mArrListItems.get(position);
            }


            if (convertView == null) {
                convertView = llInflater.inflate(R.layout.adapter_videos, null);
                holder = new ViewHolder();

                holder.imageView_thumbnail = (YouTubeThumbnailView) convertView.findViewById(R.id.imageView_thumbnail);
                holder.tvTitleName = (TextView) convertView.findViewById(R.id.tvTitleName);

                //Case 1 - Initalise the thumbnailholder.thumb = (YouTubeThumbnailView) mCurrentRow.findViewById(R.id.list_content_thumb);
                holder.imageView_thumbnail.setTag(obj.getId().getVideoId());
                holder.imageView_thumbnail.initialize(key, this);

                if(width > 300)
                {
                    System.out.print("======set new H W========");
                    holder.imageView_thumbnail.getLayoutParams().width = (int) width/3;
                    holder.imageView_thumbnail.getLayoutParams().height=  (int) width/3;
                }



                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                try {
                    // 2) and 3) The view is already created...
                    YouTubeThumbnailLoader loader = mLoaders.get(holder.imageView_thumbnail);

                    // ...and is currently being initialized. We store the current videoId in the tag.
                    if (loader == null) {
                        holder.imageView_thumbnail.setTag(obj.getId().getVideoId());

                        // ...and already initialized. Simply set the right videoId on the loader.
                    } else {
                      //  holder.imageView_thumbnail.setImageResource(R.drawable.ic_launcher);
                        loader.setVideo(obj.getId().getVideoId());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            try {

                holder.tvTitleName.setText(obj.getId().getVideoId());


            } catch (Exception e) {
                e.printStackTrace();
            }


            return convertView;
        }


        @Override
        public int getCount() {
            return mArrListItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrListItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mArrListItems.size();
        }


        class ViewHolder {

            TextView tvTitleName;
            YouTubeThumbnailView imageView_thumbnail;
        }


        @Override
        public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            String videoId = (String) view.getTag();
            mLoaders.put(view, loader);
           // view.setImageResource(R.drawable.ic_launcher);
            loader.setVideo(videoId);
            System.out.print("======sucess========");
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView thumbnailView, YouTubeInitializationResult errorReason) {
            if (errorReason.isUserRecoverableError()) {
               /* if (errorDialog == null || !errorDialog.isShowing()) {
                    //errorDialog = errorReason.getErrorDialog(, RECOVERY_DIALOG_REQUEST);
                    errorDialog.show();
                }*/
                System.out.print("======Error========");
            } else {
        /*String errorMessage =
                String.format(getString(R.string.error_thumbnail_view), errorReason.toString());
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();*/
                System.out.print("======Error========");
            }
        }


    }




 /*   private class BackgroundTask extends AsyncTask<Void, Void,Curator> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .build();
        }

        @Override
        protected Curator doInBackground(Void... params) {
            IApiMethods methods = restAdapter.create(IApiMethods.class);
            Curator curators = methods.getUserChannelName(API_KEY);

            return curators;
        }

        @Override
        protected void onPostExecute(Curator curators) {
            tvData.setText(curators.title + "\n\n");
            for (Curator.Dataset dataset : curators.dataset) {
                tvData.setText(tvData.getText() + dataset.curator_title +" - " + dataset.curator_tagline + "\n");
            }
        }
    }*/
}

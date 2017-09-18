package com.sen;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.sen.api.AppBase;
import com.sen.api.IApiMethods;
import com.sen.api.UsernameChannelVideos;
import com.squareup.picasso.Picasso;

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





public class ActSearchVidList extends ActionBarActivity {

    UsernameChannelVideos.Items obj;

    String part = "id";// for more details = "snippet,id";
    String q = "andriod";
    String key = "AIzaSyDGDGk8ctTus1DCsWgldVeq2UaUwO7i4WM";
    String order = "date";
    String type = "video";
    //String maxResults = "45";
    String maxResults = "50";

    String Tag = "-tag--ActUsernameVidList.java---";

    Bundle bundle;

    YouTubeThumbnailView vv;
    GridView gridVideos;
    int width=300,height=600;

    private SearchBox search;
    private Toolbar toolbar;

    IApiMethods methods;
    Callback callback;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_searchlist_videos);
        gridVideos = (GridView) findViewById(R.id.gridVideos);


        bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getString("channelId") != null && bundle.getString("channelId").toString().length() > 1) {
                q = bundle.getString("channelId");
            }
        }


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppBase.strBaseUrl)
                .build();
         methods = restAdapter.create(IApiMethods.class);
         callback = new Callback() {
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

                   // VideoAdapter videoAdapter = new VideoAdapter(ActSearchVidList.this, curators.getArrayListItems());
                    SearchVideoAdapter videoAdapter = new SearchVideoAdapter(ActSearchVidList.this, curators.getArrayListItems());
                    gridVideos.setAdapter(videoAdapter);
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void failure(RetrofitError retrofitError) {
                Log.v(Tag, "failure---1--");
                Log.e("-Error-","--failure--"+retrofitError.getMessage());
            }
        };

        //getUserChannelName
        //getUserChannelVideos

        Log.e("-apiCall-","part-->"+part+"--q-->"+ q+"--type-->"+type+"--key-->"+key+"--maxResults-->"+maxResults);
        methods.getSeachVideos(part, q,  type,key, maxResults, callback);



        search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Search videos");

        this.setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openSearch();
                return true;
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
         width = size.x;
         height = size.y;

    }








    // for the menu search view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.youtube_search, menu);
        return true;
    }

    public void openSearch() {

        search.revealFromMenuItem(R.id.action_search, this);
        /*for (int x = 0; x < 10; x++) {
            SearchResult option = new SearchResult("Result "
                    + Integer.toString(x), getResources().getDrawable(
                    R.drawable.ic_history));
            search.addSearchable(option);
        }*/

        SearchResult option = new SearchResult("New Movies Trailer ", getResources().getDrawable(R.drawable.ic_history));
        search.addSearchable(option);
        SearchResult option1 = new SearchResult("Popular videos ", getResources().getDrawable(R.drawable.ic_history));
        search.addSearchable(option1);
        SearchResult option2 = new SearchResult("Amazing world", getResources().getDrawable(R.drawable.ic_history));
        search.addSearchable(option2);
        SearchResult option3 = new SearchResult("Funny Pupular ", getResources().getDrawable(R.drawable.ic_history));
        search.addSearchable(option3);



        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                // Hamburger has been clicked
                Toast.makeText(ActSearchVidList.this, "Menu click",
                        Toast.LENGTH_LONG).show();
            }

        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
            }

            @Override
            public void onSearchTermChanged(String term) {
                // React to the search term changing
                // Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(ActSearchVidList.this, searchTerm + " Searched",
                        Toast.LENGTH_LONG).show();
                toolbar.setTitle(searchTerm);
                Log.e("-apiCall-","part-->"+part+"--q-->"+ searchTerm+"--type-->"+type+"--key-->"+key+"--maxResults-->"+maxResults);
                methods.getSeachVideos(part, searchTerm,  type,key, maxResults, callback);


            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to result being clicked
            }

            @Override
            public void onSearchCleared() {

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        search.hideCircularly(this);
        if(search.getSearchText().isEmpty())
        {
          //  toolbar.setTitle("Wall sen");
        }
    }







    private class SearchVideoAdapter extends BaseAdapter
    {
        ArrayList<UsernameChannelVideos.Items> mArrListItems;
        Context mContext;
        LayoutInflater llInflater;

        public SearchVideoAdapter(Context context,ArrayList<UsernameChannelVideos.Items> arrListItems)
        {
            mArrListItems = arrListItems;
            mContext = context;
            llInflater = LayoutInflater.from(mContext);
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (mArrListItems.get(position) != null) {
                obj = mArrListItems.get(position);
            }

            if (convertView == null) {
                convertView = llInflater.inflate(R.layout.adapter_videos, null);
                holder = new ViewHolder();

                //  holder.imageView_thumbnail = (YouTubeThumbnailView) convertView.findViewById(R.id.imageView_thumbnail);
                holder.tvTitleName = (TextView) convertView.findViewById(R.id.tvTitleName);
                holder.ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try {

                holder.tvTitleName.setText(obj.getId().getVideoId());
                // https://i.ytimg.com/vi/8bMbcNUM68U/maxresdefault.jpg
                String strImgUrl = "https://i.ytimg.com/vi/"+obj.getId().getVideoId()+"/maxresdefault.jpg";
                Picasso.with(mContext).load(strImgUrl).placeholder(R.drawable.ic_history).into(holder.ivThumbnail);

                holder.ivThumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intYouTubePlayerView = new Intent(ActSearchVidList.this, ActYouTubePlayer.class);
                        intYouTubePlayerView.putExtra("from","ActSearchVidList");
                        intYouTubePlayerView.putExtra("videoID",obj.getId().getVideoId());
                        mContext.startActivity(intYouTubePlayerView);

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


            return  convertView;
        }
        class ViewHolder
        {
             TextView tvTitleName;
            ImageView ivThumbnail;
        }
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

             //  holder.imageView_thumbnail = (YouTubeThumbnailView) convertView.findViewById(R.id.imageView_thumbnail);
                holder.tvTitleName = (TextView) convertView.findViewById(R.id.tvTitleName);
                holder.ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);

                //Case 1 - Initalise the thumbnailholder.thumb = (YouTubeThumbnailView) mCurrentRow.findViewById(R.id.list_content_thumb);
              /* holder.imageView_thumbnail.setTag(obj.getId().getVideoId());
                holder.imageView_thumbnail.initialize(key, this);

                if(width > 300)
                {

                    System.out.print("======set new H W========");
                    holder.imageView_thumbnail.getLayoutParams().width = (int) (width/3);
                    holder.imageView_thumbnail.getLayoutParams().height=  (int) (width/3) ;
                }*/

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
              /*  try {
                    // 2) and 3) The view is already created...
                    YouTubeThumbnailLoader loader = mLoaders.get(holder.imageView_thumbnail);

                    // ...and is currently being initialized. We store the current videoId in the tag.
                    if (loader == null) {
                        holder.imageView_thumbnail.setTag(obj.getId().getVideoId());

                        // ...and already initialized. Simply set the right videoId on the loader.
                    } else {
                        //holder.imageView_thumbnail.setImageResource(R.drawable.ic_launcher);
                        loader.setVideo(obj.getId().getVideoId());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }


            try {

                holder.tvTitleName.setText(obj.getId().getVideoId());


                // https://i.ytimg.com/vi/8bMbcNUM68U/maxresdefault.jpg
                String strImgUrl = "https://i.ytimg.com/vi/"+obj.getId().getVideoId()+"/maxresdefault.jpg";
                Picasso.with(mContext).load(strImgUrl).placeholder(R.drawable.ic_history).into(holder.ivThumbnail);

                holder.ivThumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intYouTubePlayerView = new Intent(mContext, YouTubePlayerView.class);
                        intYouTubePlayerView.putExtra("from","ActSearchVidList");
                        intYouTubePlayerView.putExtra("videoID",obj.getId().getVideoId());
                        mContext.startActivity(intYouTubePlayerView);

                    }
                });


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
           // YouTubeThumbnailView imageView_thumbnail;
            ImageView ivThumbnail;
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

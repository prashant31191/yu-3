package com.sen;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
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

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.youtube.utils.CustomProgressDialog;
import com.youtube.utils.SpinnerLoading;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.sen.api.AppBase;
import com.sen.api.IApiMethods;
import com.sen.api.SearchVideoTitle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Adminsss on 08-02-2016.
 */





public class ActSearchVidTitleList extends ActionBarActivity {



    String part = "snippet";// for more details = "snippet,id";
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
    CustomProgressDialog customProgressDialog;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_searchlist_videos);
        gridVideos = (GridView) findViewById(R.id.gridVideos);

        customProgressDialog = new CustomProgressDialog(ActSearchVidTitleList.this);

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
                Log.v(Tag, "Response"+response.toString());
                SearchVideoTitle curators = (SearchVideoTitle) o;

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

                   // VideoAdapter videoAdapter = new VideoAdapter(ActSearchVidTitleList.this, curators.getArrayListItems());
                    SearchVideoAdapter videoAdapter = new SearchVideoAdapter(ActSearchVidTitleList.this, curators.getArrayListItems());
                    gridVideos.setAdapter(videoAdapter);
                }
                customProgressDialog.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void failure(RetrofitError retrofitError) {
                Log.v(Tag, "failure---1--");
                Log.e("-Error-","--failure--"+retrofitError.getMessage());
                customProgressDialog.dismiss();
            }
        };

        //getUserChannelName
        //getUserChannelVideos

        Log.e("-apiCall-","part-->"+part+"--q-->"+ q+"--type-->"+type+"--key-->"+key+"--maxResults-->"+maxResults);
        methods.getSeachVideosTitle(part, q,  type,key, maxResults, callback);
        customProgressDialog.show();


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
                Toast.makeText(ActSearchVidTitleList.this, "Menu click",
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
                Toast.makeText(ActSearchVidTitleList.this, searchTerm + " Searched",
                        Toast.LENGTH_LONG).show();
                toolbar.setTitle(searchTerm);
                Log.e("-apiCall-","part-->"+part+"--q-->"+ searchTerm+"--type-->"+type+"--key-->"+key+"--maxResults-->"+maxResults);
                methods.getSeachVideosTitle(part, searchTerm,  type,key, maxResults, callback);
                customProgressDialog.show();

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
        ArrayList<SearchVideoTitle.Items> mArrListItems;
        Context mContext;
        LayoutInflater llInflater;

        public SearchVideoAdapter(Context context,ArrayList<SearchVideoTitle.Items> arrListItems)
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            SearchVideoTitle.Items obj = null;
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

                if(obj !=null && obj.getSnippet() !=null )
                {
                    if(obj.getSnippet().getVideoTitle() !=null)
                    {
                        holder.tvTitleName.setText(obj.getSnippet().getVideoTitle());
                    }
                    else
                    {
                        Log.e("No found","--getSnippet title not found--");
                    }
                }
                else
                {
                    Log.e("No found","--getSnippet not found--");
                }

                // https://i.ytimg.com/vi/8bMbcNUM68U/maxresdefault.jpg
                if( obj !=null && obj.getId() !=null && obj.getId().getVideoId()!=null) {
                    String strImgUrl = "https://i.ytimg.com/vi/" + obj.getId().getVideoId() + "/maxresdefault.jpg";
                    Picasso.with(mContext).load(strImgUrl).placeholder(R.drawable.ic_launcher).into(holder.ivThumbnail);

                    holder.ivThumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intYouTubePlayerView = new Intent(ActSearchVidTitleList.this, ActYouTubePlayer.class);
                            intYouTubePlayerView.putExtra("from", "ActSearchVidTitleList");
                            intYouTubePlayerView.putExtra("videoID", mArrListItems.get(position).getId().getVideoId());
                            mContext.startActivity(intYouTubePlayerView);

                        }
                    });
                }
                else
                {
                    Log.e("No found","--title not found--");
                }

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


}

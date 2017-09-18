package com.youtube.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.sen.ActYouTubePlayer;
import com.sen.R;
import com.sen.api.AppBase;
import com.sen.api.IApiMethods;
import com.squareup.picasso.Picasso;
import com.youtube.apicall.YTrailerModel;
import com.youtube.database.AppDB;
import com.youtube.database.classes.NoteDao;
import com.youtube.database.classes.YouTubeModelDao;
import com.youtube.utils.CustomProgressDialog;
import com.youtube.utils.MyData;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Admin on 6/30/2016.
 */



public class YTrailerOfflineFragment extends android.support.v4.app.Fragment {
    int color;

    Activity mActivity;
//    CustomProgressDialog customProgressDialog;
    Bundle  bundle;

    String Tag = "YTrailerFragment",pageToken="test";

    ArrayList<Items> arrayListItems ;
    RecyclerView recyclerView;
    YListAdapter adapter;
    int positionSelected = 0;


    private Cursor cursor;
    public static final String TAG = "YTrailerFragment--";

    public YTrailerOfflineFragment() {
    }

    @SuppressLint("ValidFragment")
    public YTrailerOfflineFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ytrailer, container, false);
        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor(color);
         recyclerView = (RecyclerView) view.findViewById(R.id.rvRecent);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mActivity = getActivity();
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            Log.i("--setUserVisibleHint--","==  YTrailerFragment  ==");

            getdataFromDatabase();
            setYTrailerDataApiCall();

        }
        else {
        }
    }

    private void setYTrailerDataApiCall() {
        //customProgressDialog = new CustomProgressDialog(mActivity);
        bundle = mActivity.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("channelId") != null && bundle.getString("channelId").toString().length() > 1) {
                MyData.apiQ = bundle.getString("channelId");
            }
        }
        if (arrayListItems != null && arrayListItems.size() > 2) {
            Log.e("-apiCall-", "--there are some data on the database---");
            adapter = new YListAdapter(mActivity,arrayListItems);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("-apiCall-", "part-there is no any data on database->");
        }
    }

    public void getdataFromDatabase() {
        Log.e("-getFrom Db-", "getting data from youtube..database->");
        String textColumnTitle = YouTubeModelDao.Properties.Title.columnName;
        String textColumnDesc = YouTubeModelDao.Properties.Desc.columnName;
        String textColumnImage = YouTubeModelDao.Properties.Image.columnName;
        String textColumnTab= YouTubeModelDao.Properties.Tab.columnName;


        String orderBy = textColumnTitle + " COLLATE LOCALIZED ASC";
        cursor = getDb().query(getYouTubeModelDao().getTablename(), getYouTubeModelDao().getAllColumns(), null, null, null, null, orderBy);
       // String[] objectStringFrom = {textColumnTitle, textColumnDesc,textColumnImage,textColumnTab};
        arrayListItems = new ArrayList<>();

       /* for (int i=0;i<objectStringFrom.length; i++)
        {

            Log.e("-getFrom Db-", "==Title="+textColumnTitle);
           Items items = new Items();

            Id id = new Id();
            Snippet  snippet = new Snippet();

            items.id.setVideoId(textColumnImage);
            items.snippet.setVideoTitle(textColumnTitle);
            items.snippet.setVideoDescription(textColumnDesc);


            arrayListItems.add(items);
        }
*/


        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            //Log.e("-getFrom Db-", "==Title="+cursor.getColumnName(2));
           Log.e("-getFrom Db-", "==Title="+cursor.getString(cursor.getColumnIndex(textColumnTitle)));


            // The Cursor is now set to the right position
           // mArrayList.add(mCursor.getWhateverTypeYouWant(WHATEVER_COLUMN_INDEX_YOU_WANT));


            Items items = new Items();

            Id id = new Id();
            Snippet snippet = new Snippet();
            id.setVideoId(cursor.getString(cursor.getColumnIndex(textColumnImage)));
            snippet.setVideoTitle(cursor.getString(cursor.getColumnIndex(textColumnTitle)));
            snippet.setVideoDescription(cursor.getString(cursor.getColumnIndex(textColumnDesc)));

            items.setId(id);
            items.setSnippet(snippet);

            arrayListItems.add(items);


        }



        adapter = new YListAdapter(mActivity,arrayListItems);
        recyclerView.setAdapter(adapter);

        //String[] from = {textColumn, NoteDao.Properties.Comment.columnName};
        //int[] to = {android.R.id.text1, android.R.id.text2};
        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from,to);
        //setListAdapter(adapter);
    }

    private YouTubeModelDao getYouTubeModelDao() {
        // Provided by AppDB class get Dao Session () for specific Dao
        return ((AppDB) mActivity.getApplicationContext()).getDaoSession().getYouTubeModelDao();
    }

    private SQLiteDatabase getDb() {
        // Provided through the App DB class getDb () for specific db
        return ((AppDB) mActivity.getApplicationContext()).getDb();
    }











    public class YListAdapter  extends RecyclerView.Adapter<YListAdapter.ViewHolder> {

        ArrayList<Items> mArrayListItems;
        Context mContext;

        public YListAdapter(Context context, ArrayList<Items> arrayListItems ) {
            mArrayListItems = arrayListItems;
            mContext = context;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.raw_ytrailer_fragment, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final Items yTrailerModel_Items  = mArrayListItems.get(position);
            holder.tvTtile.setText(position+"-"+yTrailerModel_Items.getSnippet().getVideoTitle());
            holder.tvDesc.setText(yTrailerModel_Items.getSnippet().getVideoDescription());

            Log.e("--SET data--","-SET-video id--"+yTrailerModel_Items.getId().getVideoId());
            if( yTrailerModel_Items !=null && yTrailerModel_Items.getId() !=null && yTrailerModel_Items.getId().getVideoId()!=null) {
                String strImgUrl = "https://i.ytimg.com/vi/" + yTrailerModel_Items.getId().getVideoId() + "/maxresdefault.jpg";
                Picasso.with(mContext).load(strImgUrl).placeholder(R.drawable.ic_launcher).into(holder.ivImage);

                holder.ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("--click on image--","--video id--"+yTrailerModel_Items.getId().getVideoId());
                        Intent intYouTubePlayerView = new Intent(mActivity, ActYouTubePlayer.class);
                        intYouTubePlayerView.putExtra("from", "ActSearchVidTitleList");
                        intYouTubePlayerView.putExtra("videoID", yTrailerModel_Items.getId().getVideoId());
                        mContext.startActivity(intYouTubePlayerView);
                    }
                });
            }
            else
            {
                Log.e("No found","--title not found--");
            }
        }

        @Override
        public int getItemCount() {
            return mArrayListItems.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            CardView cardItemLayout;
            TextView tvTtile;
            TextView tvDesc;
            ImageView ivImage;

            public ViewHolder(View view) {
                super(view);
                cardItemLayout = (CardView) view.findViewById(R.id.cardlist_item);
                tvTtile = (TextView) view.findViewById(R.id.tvTtile);
                ivImage = (ImageView) view.findViewById(R.id.ivImage);
                tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            }
        }
    }










    public class Items {
        @SerializedName("id")
        Id id; //channel id

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }


        @SerializedName("snippet")
        Snippet snippet; //channel id

        public Snippet getSnippet() {
            return snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }


    }


    public class Id {
        @SerializedName("videoId")
        String videoId; //channel id

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }

    public class Snippet {
        @SerializedName("title")
        String videoTitle; //channel id

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        @SerializedName("description")
        String videoDescription; //channel id

        public String getVideoDescription() {
            return videoDescription;
        }

        public void setVideoDescription(String videoDescription) {
            this.videoDescription = videoDescription;
        }
    }

}
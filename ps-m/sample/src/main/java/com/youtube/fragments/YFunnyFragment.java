package com.youtube.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sen.ActSearchVidTitleList;
import com.sen.ActYouTubePlayer;
import com.sen.R;
import com.sen.api.AppBase;
import com.sen.api.IApiMethods;


import com.squareup.picasso.Picasso;
import com.youtube.adapters.RecentAdapter;
import com.youtube.apicall.YTrailerModel;

import com.youtube.database.AppDB;
import com.youtube.database.classes.Note;
import com.youtube.database.classes.NoteDao;
import com.youtube.database.classes.YouTubeModel;
import com.youtube.database.classes.YouTubeModelDao;
import com.youtube.utils.CustomProgressDialog;
import com.youtube.utils.MyData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Admin on 6/30/2016.
 */


public class YFunnyFragment extends android.support.v4.app.Fragment {
    int color;

    Activity mActivity;
    CustomProgressDialog customProgressDialog;
    Bundle bundle;

    IApiMethods methods;
    Callback callback;

    String Tag = "YTrailerFragment", pageToken = "test";

    ArrayList<YTrailerModel.Items> arrayListItems;
    RecyclerView recyclerView;
    YListAdapter adapter;
    int positionSelected = 0;


    //MaterialRefreshLayout llMRefLayout;


    private Cursor cursor;
    public static final String TAG = "YTrailerFragment--";


    public YFunnyFragment() {
    }

    @SuppressLint("ValidFragment")
    public YFunnyFragment(int color) {
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

            Log.i("--setUserVisibleHint--", "==  YTrailerFragment  ==");

            Log.e("-getFrom Db-", "getting data from youtube..database->");
            String textColumnTitle = YouTubeModelDao.Properties.Title.columnName;
            String orderBy = textColumnTitle + " COLLATE LOCALIZED ASC";
            cursor = getDb().query(getYouTubeModelDao().getTablename(), getYouTubeModelDao().getAllColumns(), null, null, null, null, orderBy);

            setYTrailerDataApiCall();
        } else {
        }
    }

    /*

    private void setRefreshLayout() {



        llMRefLayout.setWaveColor(0x90ffffff);

        llMRefLayout.setIsOverLay(true);
        llMRefLayout.setWaveShow(true);
        llMRefLayout.setLoadMore(true);

        llMRefLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //refreshing...
                Log.d("===Data=", "==========onRefresh============");
                Snackbar.make(materialRefreshLayout," Refresh ",Snackbar.LENGTH_LONG).show();

                stopLoding();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //load more refreshing...
                Log.d("===Data=", "==========onRefreshLoadMore============");

                Snackbar.make(materialRefreshLayout," More ",Snackbar.LENGTH_LONG).show();

                stopLoding();
            }
        });


    }


    public void stopLoding() {

        // refresh complete
        llMRefLayout.finishRefresh();

// load more refresh complete
        llMRefLayout.finishRefreshLoadMore();
    }
*/

    private void setYTrailerDataApiCall() {

        customProgressDialog = new CustomProgressDialog(mActivity);

        bundle = mActivity.getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getString("channelId") != null && bundle.getString("channelId").toString().length() > 1) {
                MyData.apiQ = bundle.getString("channelId");
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
                Log.v(Tag, "Response" + response.toString());
                YTrailerModel curators = (YTrailerModel) o;

                if (curators != null && curators.getNextPageToken() != null) {
                    pageToken = curators.getNextPageToken();
                } else {
                    pageToken = "";
                }

                Log.v(Tag, "Sucess---1");

                String strData = "";

                strData = curators.getKind() + " \n ";
                Log.v(Tag, "Sucess---1---getKind---" + strData);


                if (curators.getArrayListItems() != null && curators.getArrayListItems().size() > 0) {
                    if (arrayListItems != null && arrayListItems.size() > 3) {
                        arrayListItems.addAll(curators.getArrayListItems());
                    } else {
                        arrayListItems = new ArrayList<>();
                        arrayListItems = curators.getArrayListItems();
                    }

                    if (adapter != null && adapter.getItemCount() > 3) {
                        try {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        adapter = new YListAdapter(mActivity, arrayListItems);
                        recyclerView.setAdapter(adapter);
                    }


                    if (arrayListItems != null && arrayListItems.size() > 2) {
                        for (int i = 0; i < arrayListItems.size(); i++) {
                            //Inserts, as simple as long as you create a Java object
                            String strTitle = "" + arrayListItems.get(i).getSnippet().getVideoTitle();
                            String strDesc = "" + arrayListItems.get(i).getSnippet().getVideoDescription();
                            String strImage = "" + arrayListItems.get(i).getId().getVideoId();
                            String strTab = "2";
                            YouTubeModel youTubeModel = new YouTubeModel(null, strTitle, strDesc, strImage, strTab);
                            //(Long id, String text, String comment, String strImage,String strTab)
                            try {
                                getYouTubeModelDao().insert(youTubeModel);
                            } catch (Exception ex) {
                                Log.e("==Database==","-Funny-Already inserted-");
                                ex.printStackTrace();
                            } catch (Error e) {
                                e.printStackTrace();
                                Log.e("==Database==","-Funny-Already inserted-");
                            }
                            //getYouTubeModelDao().insertOrReplace(youTubeModel);
                            Log.d(TAG, "Inserted new note, ID: " + youTubeModel.getId());
                            cursor.requery();
                        }
                    }
                }
                else
                {
                    Snackbar.make(recyclerView," No data found. ",Snackbar.LENGTH_SHORT).show();
                }
                customProgressDialog.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void failure(RetrofitError retrofitError) {
                Log.v(Tag, "failure---1--");
                Log.e("-Error-", "--failure--" + retrofitError.getMessage());
                Snackbar.make(recyclerView,getResources().getString(R.string.str_something_went_wrong),Snackbar.LENGTH_SHORT).show();
                customProgressDialog.dismiss();
            }
        };


        //methods.getSeachVideosTitle(part, q,  type,key, maxResults, callback);
        if (arrayListItems != null && arrayListItems.size() > 2) {
            Log.e("-apiCall-", "--there are some data---");
            adapter = new YListAdapter(mActivity, arrayListItems);
            recyclerView.setAdapter(adapter);
        } else {
            MyData.apiQ = "New Trailer";
            Log.e("-apiCall-", "part-->" + MyData.apiPart + "--q-->" + MyData.apiQ + "--type-->" + MyData.apiType + "--key-->" + MyData.apiKey + "--maxResults-->" + MyData.apiMaxResults);
            methods.getSeachVideosTrailer(MyData.apiPart, MyData.apiQ, MyData.apiType, MyData.apiKey, MyData.apiMaxResults, callback);
            customProgressDialog.show();
        }


    }


    private YouTubeModelDao getYouTubeModelDao() {
        // Provided by AppDB class get Dao Session () for specific Dao
        return ((AppDB) mActivity.getApplicationContext()).getDaoSession().getYouTubeModelDao();
    }

    private SQLiteDatabase getDb() {
        // Provided through the App DB class getDb () for specific db
        return ((AppDB) mActivity.getApplicationContext()).getDb();
    }


    int LastPositionAutoLoad = 40;


    public class YListAdapter extends RecyclerView.Adapter<YListAdapter.ViewHolder> {

        ArrayList<YTrailerModel.Items> mArrayListItems;
        Context mContext;

        public YListAdapter(Context context, ArrayList<YTrailerModel.Items> arrayListItems) {
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

            final YTrailerModel.Items yTrailerModel_Items = mArrayListItems.get(position);
            holder.tvTtile.setText(position + "-" + yTrailerModel_Items.getSnippet().getVideoTitle());
            holder.tvDesc.setText(yTrailerModel_Items.getSnippet().getVideoDescription());

            Log.e("--SET data--", "-SET-video id--" + yTrailerModel_Items.getId().getVideoId());

            if (position > LastPositionAutoLoad) {
                LastPositionAutoLoad = LastPositionAutoLoad + 40;
                if (pageToken != null && pageToken.length() > 2) {
                    Log.e("--SET data--", "-SET-pageToken===" + pageToken);
                    positionSelected = position;
                    MyData.apiQ = "New Trailer";
                    methods.getSeachVideosTrailerNextPage(MyData.apiPart, MyData.apiQ, MyData.apiType, MyData.apiKey, pageToken, MyData.apiMaxResults, callback);
                    customProgressDialog.show();
                }

            }

            if (yTrailerModel_Items != null && yTrailerModel_Items.getId() != null && yTrailerModel_Items.getId().getVideoId() != null) {
                String strImgUrl = "https://i.ytimg.com/vi/" + yTrailerModel_Items.getId().getVideoId() + "/maxresdefault.jpg";
                Picasso.with(mContext).load(strImgUrl).placeholder(R.drawable.ic_launcher).into(holder.ivImage);

                holder.ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("--click on image--", "--video id--" + yTrailerModel_Items.getId().getVideoId());
                        Intent intYouTubePlayerView = new Intent(mActivity, ActYouTubePlayer.class);
                        intYouTubePlayerView.putExtra("from", "ActSearchVidTitleList");
                        intYouTubePlayerView.putExtra("videoID", yTrailerModel_Items.getId().getVideoId());
                        mContext.startActivity(intYouTubePlayerView);
                    }
                });
            } else {
                Log.e("No found", "--title not found--");
            }
            //holder.year.setText(movie.getYear());
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

}
package com.youtube.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.youtube.utils.CustomProgressDialog;
import com.youtube.utils.MyData;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Admin on 6/30/2016.
 */



public class YSerialFragment extends android.support.v4.app.Fragment {
    int color;

    Activity mActivity;
    CustomProgressDialog customProgressDialog;
    Bundle  bundle;

    IApiMethods methods;
    Callback callback;

    String Tag = "YTrailerFragment",pageToken="test";

    ArrayList<YTrailerModel.Items> arrayListItems ;
    RecyclerView recyclerView;
    YListAdapter adapter;
    int positionSelected = 0;


    //MaterialRefreshLayout llMRefLayout;



    public YSerialFragment() {
    }

    @SuppressLint("ValidFragment")
    public YSerialFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_ytrailer, container, false);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor(color);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvRecent);
        //llMRefLayout = (MaterialRefreshLayout) view.findViewById(R.id.llMRefLayout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mActivity = getActivity();



       /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < RecentModel.data.length; i++) {
            list.add(RecentModel.data[i]);
        }

        adapter = new RecentAdapter(list);
        recyclerView.setAdapter(adapter);*/

        setYTrailerDataApiCall();
        //  setRefreshLayout();

        return view;
    }/*

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
                Log.v(Tag, "Response"+response.toString());
                YTrailerModel curators = (YTrailerModel) o;

                if(curators !=null && curators.getNextPageToken() !=null)
                {
                    pageToken = curators.getNextPageToken();
                }
                else
                {
                    pageToken = "";
                }

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
                  /*  ActSearchVidTitleList.SearchVideoAdapter videoAdapter = new ActSearchVidTitleList.SearchVideoAdapter(ActSearchVidTitleList.this, curators.getArrayListItems());
                    gridVideos.setAdapter(videoAdapter);*/
                    if(arrayListItems !=null && arrayListItems.size() > 3)
                    {
                        arrayListItems.addAll(curators.getArrayListItems());
                    }
                    else
                    {
                        arrayListItems = new ArrayList<>();
                        arrayListItems = curators.getArrayListItems();
                    }

                    if(adapter !=null && adapter.getItemCount() > 3)
                    {
                        /*new Thread() {
                            public void run() {
                                try {
                                  mActivity.runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          //adapter.notifyDataSetChanged();
                                                          //adapter.notifyItemChanged(positionSelected);


                                                      }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
*/
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        adapter = new YListAdapter(mActivity,arrayListItems);
                        recyclerView.setAdapter(adapter);
                    }
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




        //methods.getSeachVideosTitle(part, q,  type,key, maxResults, callback);
        if (arrayListItems != null && arrayListItems.size() < 2) {
            Log.e("-apiCall-", "--there are some data---");
        } else {
            MyData.apiQ = "hindi serial";
            Log.e("-apiCall-", "part-->" + MyData.apiPart + "--q-->" + MyData.apiQ + "--type-->" + MyData.apiType + "--key-->" + MyData.apiKey + "--maxResults-->" + MyData.apiMaxResults);
            methods.getSeachVideosTrailer(MyData.apiPart, MyData.apiQ, MyData.apiType, MyData.apiKey, MyData.apiMaxResults, callback);
            customProgressDialog.show();
        }



    }



    int LastPositionAutoLoad = 40;


    public class YListAdapter  extends RecyclerView.Adapter<YListAdapter.ViewHolder> {

        ArrayList<YTrailerModel.Items> mArrayListItems;
        Context mContext;

        public YListAdapter(Context context, ArrayList<YTrailerModel.Items> arrayListItems ) {
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

            final YTrailerModel.Items yTrailerModel_Items  = mArrayListItems.get(position);
            holder.tvTtile.setText(position+"-"+yTrailerModel_Items.getSnippet().getVideoTitle());
            holder.tvDesc.setText(yTrailerModel_Items.getSnippet().getVideoDescription());

            Log.e("--SET data--","-SET-video id--"+yTrailerModel_Items.getId().getVideoId());

            if(position > LastPositionAutoLoad)
            {
                LastPositionAutoLoad = LastPositionAutoLoad + 40;
                if(pageToken!=null && pageToken.length() > 2)
                {
                    Log.e("--SET data--","-SET-pageToken==="+pageToken);
                    positionSelected = position;
                    methods.getSeachVideosTrailerNextPage(MyData.apiPart, MyData.apiQ, MyData.apiType, MyData.apiKey, pageToken ,MyData.apiMaxResults, callback);
                    customProgressDialog.show();
                }

            }

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
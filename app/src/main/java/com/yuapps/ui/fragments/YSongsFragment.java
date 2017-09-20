package com.yuapps.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yuapps.App;
import com.yuapps.R;
import com.yuapps.network.ApiService;
import com.yuapps.network.model.SearchModel;
import com.yuapps.ui.ActYouTubePlayer;
import com.yuapps.ui.ActYuMain;
import com.yuapps.utils.IClickDownload;
import com.yuapps.utils.Temp;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YSongsFragment extends android.support.v4.app.Fragment {
    int color;

    Activity mActivity;
    ProgressDialog customProgressDialog;
    Bundle  bundle;


    String Tag = "YTrailerFragment",pageToken="test";

    ArrayList<SearchModel.Items> arrayListItems ;
    RecyclerView recyclerView;
    YListAdapter adapter;
    int positionSelected = 0;

    public YSongsFragment() {
    }

    @SuppressLint("ValidFragment")
    public YSongsFragment(int color) {
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


        Log.i("--onCreateView--","==  YSongsFragment  ==");
        setYTrailerDataApiCall();
        
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            Log.i("--setUserVisibleHint--","==  YSongsFragment  ==");
            //setYTrailerDataApiCall();
        }
        else {
        }
    }

    

    private void setYTrailerDataApiCall() {

        // customProgressDialog = new ProgressDialog(mActivity);


       /* if (mActivity.getIntent() !=null && mActivity.getIntent().getExtras() !=null) {
            bundle = mActivity.getIntent().getExtras();

            if (bundle !=null && bundle.getString("channelId") != null && bundle.getString("channelId").toString().length() > 1) {
                Temp.apiQ = bundle.getString("channelId");
            }
        }*/



        Temp.apiQ = "TV serial";
        Log.e("-apiCall-", "part-->" + Temp.apiPart + "--q-->" + Temp.apiQ + "--type-->" + Temp.apiType + "--key-->" + Temp.apiKey + "--maxResults-->" + Temp.apiMaxResults);

        // customProgressDialog.show();

        Call call = App.getApiService().getSeachVideosTrailer(Temp.apiPart, Temp.apiQ, Temp.apiType, Temp.apiKey, Temp.apiMaxResults);
        call.enqueue(callbackApi);

    }


    Callback callbackApi = new Callback<SearchModel>()
    {
        @Override
        public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
            try {
                // customProgressDialog.dismiss();
                SearchModel model = response.body();
                if (model == null) {
                    //404 or the response cannot be converted to User.
                    App.showLog("Test---null response--", "==Something wrong=");
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody != null) {
                        try {
                            App.showLog("Test---error-", "" + responseBody.string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //200 sucess
                      /*  App.showLog("===Response== " + response.body().toString());
                        App.showLog("==**==Success==**==asyncReadNotification==> ", new Gson().toJson(response.body()));
*/
                    App.showLog("==**==Success==**==new song==> ", new Gson().toJson(response.body()));


                    if (model != null && model.getNextPageToken() != null) {
                        pageToken = model.getNextPageToken();
                    } else {
                        pageToken = "";
                    }

                    Log.v(Tag, "Sucess---1");

                    String strData = "";

                    strData = model.getKind() + " \n ";
                    Log.v(Tag, "Sucess---1---getKind---" + strData);


                    if (model.getArrayListItems() != null && model.getArrayListItems().size() > 0) {
                        /*for (int i = 0; i < model.getArrayListItems().size(); i++) {
                            strData = strData + "\n" + i + "-->" + model.getArrayListItems().get(i).getId().getVideoId();
                            Log.v(Tag, "Sucess---2-strData---" + strData);
                        }*/
                        if (arrayListItems != null && arrayListItems.size() > 3) {
                            arrayListItems.addAll(model.getArrayListItems());
                        } else {
                            arrayListItems = new ArrayList<>();
                            arrayListItems = model.getArrayListItems();
                        }

                        if (adapter != null && adapter.getItemCount() > 3) {

                            adapter.notifyDataSetChanged();
                        } else {
                            adapter = new YListAdapter(mActivity, arrayListItems);
                            recyclerView.setAdapter(adapter);
                        }
                    }


                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<SearchModel> call, Throwable t) {
            t.printStackTrace();

            Log.e("-Error-","--failure--"+t.getMessage());
            // customProgressDialog.dismiss();
        }
    };

    int LastPositionAutoLoad = 40;

    public class YListAdapter  extends RecyclerView.Adapter<YListAdapter.ViewHolder> {

        ArrayList<SearchModel.Items> mArrayListItems;
        Context mContext;

        public YListAdapter(Context context, ArrayList<SearchModel.Items> arrayListItems ) {
            mArrayListItems = arrayListItems;
            mContext = context;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mActivity.getApplicationContext())
                    .inflate(R.layout.raw_ytrailer_fragment, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final SearchModel.Items yTrailerModel_Items  = mArrayListItems.get(position);
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
                    Temp.apiQ = "New Song";

                    Call call = App.getApiService().getSeachVideosTrailerNextPage(Temp.apiPart, Temp.apiQ, Temp.apiType, Temp.apiKey, pageToken ,Temp.apiMaxResults);
                    call.enqueue(callbackApi);

                }

            }

            holder.tvDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IClickDownload iClickDownload = new ActYuMain();
                    iClickDownload.onDownloadClick("0",getActivity());

                }
            });

            if( yTrailerModel_Items !=null && yTrailerModel_Items.getId() !=null && yTrailerModel_Items.getId().getVideoId()!=null) {
                String strImgUrl = "https://i.ytimg.com/vi/" + yTrailerModel_Items.getId().getVideoId() + "/maxresdefault.jpg";
                Glide.with(mContext).load(strImgUrl).placeholder(R.drawable.header).into(holder.ivImage);

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
            TextView tvDownload;
            TextView tvDesc;
            ImageView ivImage;

            public ViewHolder(View view) {
                super(view);
                cardItemLayout = (CardView) view.findViewById(R.id.cardlist_item);
                tvTtile = (TextView) view.findViewById(R.id.tvTtile);
                ivImage = (ImageView) view.findViewById(R.id.ivImage);
                tvDesc = (TextView) view.findViewById(R.id.tvDesc);
                tvDownload = (TextView) view.findViewById(R.id.tvDownload);
            }
        }
    }

}
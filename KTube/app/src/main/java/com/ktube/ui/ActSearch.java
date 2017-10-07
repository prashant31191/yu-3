package com.ktube.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.ktube.App;
import com.ktube.R;
import com.ktube.network.model.SearchModel;
import com.ktube.utils.Temp;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActSearch extends ActAds {

    String Tag = "=ActSearch ==",pageToken="test";

    ArrayList<SearchModel.Items> arrayListItems ;
    RecyclerView recyclerView;
    YListAdapter adapter;
    TextView tvLoading;
    int positionSelected = 0;
    SearchView svSearchVideos;
    String strKeyword = "#prnclwp";

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);
        try{

        if(getIntent() !=null && getIntent().getExtras() !=null)
        {
            if(getIntent().getExtras().getString("keyword") !=null)
            {
                strKeyword = getIntent().getExtras().getString("keyword");
                strKeyword = "#prnclwp";
                App.showLog("====strKeyword==");
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.rvRecent);
        tvLoading = (TextView) findViewById(R.id.tvLoading);
        svSearchVideos = (SearchView) findViewById(R.id.svSearchVideos);
        svSearchVideos.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if(strKeyword !=null && strKeyword.length() > 1)
        {

            svSearchVideos.onActionViewExpanded();

            svSearchVideos.setQuery(strKeyword, false);

            /*searchView.setIconifiedByDefault(true);
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
*/




        }

        svSearchVideos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mNextLevelButton.performClick();

                arrayListItems = null;
                adapter = null;
                Temp.apiQ = query;
                Call call = App.getApiService().getSeachVideosTrailer(Temp.apiPart, Temp.apiQ, Temp.apiType, Temp.apiKey, Temp.apiMaxResults);
                call.enqueue(callbackApi);
                tvLoading.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setYTrailerDataApiCall();
        setDisplayBanner();
    }catch (Exception e){
        e.printStackTrace();
    }
    }



    private void setDisplayBanner()
    {


        //String deviceid = tm.getDeviceId();

        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(Temp.adsAppBnrId);

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.test);
        layout.addView(mAdView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("33BE2250B43518CCDA7DE426D04EE232")
                .build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);



        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                App.showLog("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                App.showLog("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                App.showLog("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                App.showLog("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                App.showLog("Ads", "onAdClosed");
            }
        });
    }

    private void setYTrailerDataApiCall() {

        // customProgressDialog = new ProgressDialog(mActivity);

       /* if (mActivity.getIntent() !=null && mActivity.getIntent().getExtras() !=null) {
            bundle = mActivity.getIntent().getExtras();

            if (bundle !=null && bundle.getString("channelId") != null && bundle.getString("channelId").toString().length() > 1) {
                Temp.apiQ = bundle.getString("channelId");
            }
        }*/

        //methods.getSeachVideosTitle(part, q,  type,key, maxResults, callback);
        if (arrayListItems != null && arrayListItems.size() > 2) {
            Log.e("-apiCall-", "--there are some data---");
            adapter = new YListAdapter(ActSearch.this,arrayListItems);
            recyclerView.setAdapter(adapter);
        } else {
            Temp.apiQ = strKeyword;
            Log.e("-apiCall-", "part-->" + Temp.apiPart + "--q-->" + Temp.apiQ + "--type-->" + Temp.apiType + "--key-->" + Temp.apiKey + "--maxResults-->" + Temp.apiMaxResults);

            Call call = App.getApiService().getSeachVideosTrailer(Temp.apiPart, Temp.apiQ, Temp.apiType, Temp.apiKey, Temp.apiMaxResults);
            call.enqueue(callbackApi);
            if(tvLoading !=null)
                tvLoading.setVisibility(View.VISIBLE);

            // customProgressDialog.show();
        }



    }


    Callback callbackApi = new Callback<SearchModel>()
    {
        @Override
        public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
            try {
                if(tvLoading !=null)
                    tvLoading.setVisibility(View.GONE);
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


                    if(model !=null && model.getNextPageToken() !=null)
                    {
                        pageToken = model.getNextPageToken();
                    }
                    else
                    {
                        pageToken = "";
                    }


                    String strData = "";

                    strData = model.getKind() + " \n ";
                    App.showLog(Tag, "Sucess---1---getKind---" + strData);


                    if (model.getArrayListItems() != null && model.getArrayListItems().size() > 0) {
                        if(arrayListItems !=null && arrayListItems.size() > 3)
                        {
                            arrayListItems.addAll(model.getArrayListItems());
                        }
                        else
                        {
                            arrayListItems = new ArrayList<>();
                            arrayListItems = model.getArrayListItems();
                        }

                        if(adapter !=null && adapter.getItemCount() > 3)
                        {

                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            adapter = new YListAdapter(ActSearch.this,arrayListItems);
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
            View itemView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.raw_list_item, parent, false);

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

                    Call call = App.getApiService().getSeachVideosTrailerNextPage(Temp.apiPart, Temp.apiQ, Temp.apiType, Temp.apiKey, pageToken ,Temp.apiMaxResults);
                    call.enqueue(callbackApi);
                    if(tvLoading !=null)
                        tvLoading.setVisibility(View.VISIBLE);
                }

            }

            holder.tvDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   IClickDownload iClickDownload = new ActYuMain();
                    iClickDownload.onDownloadClick("0",getActivity());*/

                    mNextLevelButton.performClick();

                }
            });

            if( yTrailerModel_Items !=null && yTrailerModel_Items.getId() !=null && yTrailerModel_Items.getId().getVideoId()!=null) {
                String strImgUrl = "https://i.ytimg.com/vi/" + yTrailerModel_Items.getId().getVideoId() + "/maxresdefault.jpg";
                Glide.with(mContext).load(strImgUrl).into(holder.ivImage);

                holder.ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("--click on image--","--video id--"+yTrailerModel_Items.getId().getVideoId());

                        mNextLevelButton.performClick();

                        Intent intYouTubePlayerView = new Intent(ActSearch.this, ActPlay.class);
                        intYouTubePlayerView.putExtra("from", "ActSearchVidTitleList");
                        intYouTubePlayerView.putExtra("videoID", yTrailerModel_Items.getId().getVideoId());
                        mContext.startActivity(intYouTubePlayerView);


                    }
                });

              /*  holder.ivImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.e("--click on image long--", "--video id--" + yTrailerModel_Items.getId().getVideoId());
                        Intent intYouTubePlayerView = new Intent(mActivity, ActWebviewTag.class);
                        intYouTubePlayerView.putExtra("from", "ActSearchVidTitleList");
                        intYouTubePlayerView.putExtra("channelId", yTrailerModel_Items.getId().getVideoId());
                        intYouTubePlayerView.putExtra("title", yTrailerModel_Items.getSnippet().getVideoTitle());
                        mContext.startActivity(intYouTubePlayerView);
                        return true;
                    }
                });*/

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

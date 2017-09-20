package com.yuapps.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.yuapps.App;
import com.yuapps.R;
import com.yuapps.ui.base.SplashActivity;
import com.yuapps.ui.fragments.YSearchFragment;
import com.yuapps.ui.fragments.YSongsFragment;
import com.yuapps.ui.fragments.YTrailerFragment;
import com.yuapps.utils.IClickDownload;
import com.yuapps.utils.NonSwipeableViewPager;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActYuMain extends AppCompatActivity implements IClickDownload{ // extends AppCompatActivity

    private TextView mMessageView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView header;
    ViewPager btab_viewpager;
    NonSwipeableViewPager viewPager;
    ProgressDialog customProgressDialog;
    BottomBar bottomBar;

    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_youtube);

        mMessageView = (TextView) findViewById(R.id.messageView);
        // customProgressDialog = new ProgressDialog(ActYuMain.this);
     //  mBottomBar = BottomBar.attach(this, savedInstanceState);

         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Prince ");
         header = (ImageView) findViewById(R.id.header);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                int vibrantDarkColor = palette.getDarkVibrantColor(R.color.primary_700);
                collapsingToolbar.setContentScrimColor(vibrantColor);
                collapsingToolbar.setStatusBarScrimColor(vibrantDarkColor);
            }
        });
        setViewPagerData();

         bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
            //    messageView.setText(TabMessage.get(tabId, false));
                App.showLog("=======setOnTabSelectListener=====","======onTabSelected===tabId==="+tabId);


                mMessageView.setText(getMessage(tabId, false));
                collapsingToolbar.setTitle(getMessage(tabId, false));

                //111
                //viewPager.setCurrentItem(bottomBar.getCurrentTabPosition());

                App.showLog("====selectedPos==="+selectedPos);
                viewPager.setCurrentItem(selectedPos);
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onTabReSelected(@IdRes int tabId) {
            App.showLog("=======setOnTabReselectListener=====","======onTabReSelected===tabId==="+tabId);
                //    Toast.makeText(getApplicationContext(), TabMessage.get(tabId, true), Toast.LENGTH_LONG).show();
            }
        });


        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        mInterstitialAd = newInterstitialAd();
        adRequest = new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build();
        loadInterstitial();


    }


    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(ActYuMain.this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            App.showLog("=====showInterstitial=======Ad did not load============");
            goToNextLevel();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }






    private void setViewPagerData() {
        viewPager = (NonSwipeableViewPager) findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

      //  RecentFragment recentFragment =   new RecentFragment(getResources().getColor(R.color.clrTab1));
       // YFunnyFragment yFunnyFragment =   new YFunnyFragment(getResources().getColor(R.color.clrTab2));

        YTrailerFragment yTrailerFragment =   new YTrailerFragment(getResources().getColor(R.color.clrTab_BG));
        YSongsFragment ySongsFragment =   new YSongsFragment(getResources().getColor(R.color.clrTab_BG));
        YSearchFragment ySearchFragment =   new YSearchFragment(getResources().getColor(R.color.clrTab_BG));

    //    adapter.addFrag(recentFragment,  getResources().getString(R.string.tab_1));
        //adapter.addFrag(yFunnyFragment, getResources().getString(R.string.tab_2));

        adapter.addFrag(yTrailerFragment, getResources().getString(R.string.tab_3));
        adapter.addFrag(ySongsFragment, getResources().getString(R.string.tab_4));
        adapter.addFrag(ySearchFragment, getResources().getString(R.string.tab_5));


     viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                bottomBar.selectTabAtPosition(position,false);

            }
        });


        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
    }

    @Override
    public void onDownloadClick(String strData, Activity activity) {
        App.showLog("========Load Ads===="+strData);

        Intent intent=new Intent(activity,ActAds.class);
        activity.startActivity(intent);

/*
        Intent intent=new Intent(ActYuMain.this,ActAds.class);
        startActivity(intent);*/

        //showInterstitial();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }











int selectedPos = 0;
    private String getMessage(int menuItemId, boolean isReselection) {
        String message = "";

        switch (menuItemId) {
            case R.id.tab_movies:
                message += "Movie";
                selectedPos = 0;
                break;
            case R.id.tab_tv_serial:
                message += "TV";
                selectedPos = 1;
                break;
            case R.id.tab_popular:
                message += "Popular";
                selectedPos = 2;
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }


    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);


    }

    @Override
    protected void onDestroy() {
        bottomBar.setOnMenuTabClickListener(null);
        super.onDestroy();
    }*/
}

package com.ktube.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktube.App;
import com.ktube.R;
import com.ktube.ui.fragments.SearchFragment;
import com.ktube.ui.fragments.TrailerFragment;
import com.ktube.ui.fragments.TvFragment;
import com.ktube.utils.IClickDownload;
import com.ktube.utils.NonSwipeableViewPager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActYuMain extends AppCompatActivity implements IClickDownload { // extends AppCompatActivity

    private TextView mMessageView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView header;
    NonSwipeableViewPager viewPager;
    BottomBar bottomBar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_youtube);
        mMessageView = (TextView) findViewById(R.id.messageView);
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

       /* Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
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
        });*/
        setViewPagerData();

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                //    messageView.setText(TabMessage.get(tabId, false));
                App.showLog("=======setOnTabSelectListener=====", "======onTabSelected===tabId===" + tabId);


                mMessageView.setText(getMessage(tabId, false));
                collapsingToolbar.setTitle(getMessage(tabId, false));

                //111
                //viewPager.setCurrentItem(bottomBar.getCurrentTabPosition());

                App.showLog("====selectedPos===" + selectedPos);
                viewPager.setCurrentItem(selectedPos);
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                App.showLog("=======setOnTabReselectListener=====", "======onTabReSelected===tabId===" + tabId);
            }
        });

    }

    private void setViewPagerData() {
        viewPager = (NonSwipeableViewPager) findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        TrailerFragment yTrailerFragment = new TrailerFragment(getResources().getColor(R.color.clrTab_BG));
        TvFragment ySongsFragment = new TvFragment(getResources().getColor(R.color.clrTab_BG));
        SearchFragment ySearchFragment = new SearchFragment(getResources().getColor(R.color.clrTab_BG));

        adapter.addFrag(yTrailerFragment, getResources().getString(R.string.tab_3));
        adapter.addFrag(ySongsFragment, getResources().getString(R.string.tab_4));
        adapter.addFrag(ySearchFragment, getResources().getString(R.string.tab_5));

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                bottomBar.selectTabAtPosition(position, false);

            }
        });


        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
    }

    @Override
    public void onDownloadClick(String strData, Activity activity) {
        App.showLog("========Load Ads====" + strData);
        Intent intent = new Intent(activity, ActAds.class);
        activity.startActivity(intent);
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
                message += "Song";
                selectedPos = 2;
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}

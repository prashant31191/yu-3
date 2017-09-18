package com.youtube;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.sen.ActSearchVidTitleList;
import com.sen.R;
import com.youtube.fragments.RecentFragment;
import com.youtube.fragments.YFunnyFragment;
import com.youtube.fragments.YSearchFragment;
import com.youtube.fragments.YSerialFragment;
import com.youtube.fragments.YSongsFragment;
import com.youtube.fragments.YTrailerFragment;
import com.youtube.utils.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;

public class OnlineYouTubeActivity extends AppCompatActivity { // extends AppCompatActivity
    private BottomBar mBottomBar;
    private TextView mMessageView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView header;
    ViewPager btab_viewpager;
    ViewPager viewPager;
    CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_youtube);

        mMessageView = (TextView) findViewById(R.id.messageView);
        customProgressDialog = new CustomProgressDialog(OnlineYouTubeActivity.this);
     //  mBottomBar = BottomBar.attach(this, savedInstanceState);

         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Prince ");
         header = (ImageView) findViewById(R.id.header);
        toolbar.setNavigationIcon(R.drawable.ic_apps_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_favorites);
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

        //header.setImageBitmap(bitmap);

        // Instead of attach(), use attachShy():
       //mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.myCoordinator),findViewById(R.id.myScrollingContent), savedInstanceState);
        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.myCoordinator),findViewById(R.id.btab_viewpager), savedInstanceState);
        mBottomBar.noTopOffset();

        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                mMessageView.setText(getMessage(menuItemId, false));
               //111
                 viewPager.setCurrentItem(mBottomBar.getCurrentTabPosition());
                collapsingToolbar.setTitle(getMessage(menuItemId, false));
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
               //111 Toast.makeText(getApplicationContext(), getMessage(menuItemId, true), Toast.LENGTH_SHORT).show();
            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
       /* mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");
        mBottomBar.mapColorForTab(3, "#FF5252");
        mBottomBar.mapColorForTab(4, "#FF9800");*/

        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.clrTab1));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.clrTab2));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.clrTab3));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.clrTab4));
        mBottomBar.mapColorForTab(4, ContextCompat.getColor(this, R.color.clrTab5));


//        mBottomBar.noNavBarGoodness();
        //mBottomBar.noTopOffset();
        //mBottomBar.setMaxFixedTabs(n-1);


    }

    private void setViewPagerData() {
        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        RecentFragment recentFragment =   new RecentFragment(getResources().getColor(R.color.clrTab1));
        YFunnyFragment yFunnyFragment =   new YFunnyFragment(getResources().getColor(R.color.clrTab2));
        YTrailerFragment yTrailerFragment =   new YTrailerFragment(getResources().getColor(R.color.clrTab3));
        YSongsFragment ySongsFragment =   new YSongsFragment(getResources().getColor(R.color.clrTab4));
        YSearchFragment ySearchFragment =   new YSearchFragment(getResources().getColor(R.color.clrTab5));

        adapter.addFrag(recentFragment,  getResources().getString(R.string.tab_1));
        adapter.addFrag(yFunnyFragment, getResources().getString(R.string.tab_2));
        adapter.addFrag(yTrailerFragment, getResources().getString(R.string.tab_3));
        adapter.addFrag(ySongsFragment, getResources().getString(R.string.tab_4));
        adapter.addFrag(ySearchFragment, getResources().getString(R.string.tab_5));


        /*
        adapter.addFrag(new RecentFragment(getResources().getColor(R.color.clrTab1)),  getResources().getString(R.string.tab_1));
        adapter.addFrag(new YFunnyFragment(getResources().getColor(R.color.clrTab2)), getResources().getString(R.string.tab_2));
        adapter.addFrag(new YTrailerFragment(getResources().getColor(R.color.clrTab3)), getResources().getString(R.string.tab_3));
        adapter.addFrag(new YSongsFragment(getResources().getColor(R.color.clrTab4)), getResources().getString(R.string.tab_4));
        adapter.addFrag(new YSearchFragment(getResources().getColor(R.color.clrTab5)), getResources().getString(R.string.tab_5));*/

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                mBottomBar.selectTabAtPosition(position,false);

            }
        });
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












    private String getMessage(int menuItemId, boolean isReselection) {
        String message = "Content for ";

        switch (menuItemId) {
            case R.id.bb_menu_recents:
                message += "recents";
                break;
            case R.id.bb_menu_favorites:
                message += "favorites";
                break;
            case R.id.bb_menu_nearby:
                message += "nearby";
                break;
            case R.id.bb_menu_friends:
                message += "friends";
                break;
            case R.id.bb_menu_food:
                message += "food";
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);


    }

    @Override
    protected void onDestroy() {
        mBottomBar.setOnMenuTabClickListener(null);
        super.onDestroy();
    }
}

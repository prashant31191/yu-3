package com.sen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.profile.feature.ProfileActivity;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchBox.MenuListener;
import com.quinny898.library.persistentsearch.SearchBox.SearchListener;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

public class MainActivity extends Activity {
    Boolean isSearch;
    private SearchBox search;
    static DrawerLayout drawerLayout;
    LinearLayout llleft_drawer;
    LinearLayout llm_b, llm_c, llm_d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setContentView(R.layout.my_menu_activity);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        llleft_drawer = (LinearLayout) findViewById(R.id.llleft_drawer);
        search = (SearchBox) findViewById(R.id.searchbox);


        llm_b = (LinearLayout) findViewById(R.id.llm_b);
        llm_c = (LinearLayout) findViewById(R.id.llm_c);
        llm_d = (LinearLayout) findViewById(R.id.llm_d);


        search.enableVoiceRecognition(this);
        for (int x = 0; x < 10; x++) {
            SearchResult option = new SearchResult("Result " + Integer.toString(x), getResources().getDrawable(R.drawable.ic_history));
            search.addSearchable(option);
        }
        search.setMenuListener(new MenuListener() {

            @Override
            public void onMenuClick() {
                //Hamburger has been clicked
                Toast.makeText(MainActivity.this, "Menu click", Toast.LENGTH_LONG).show();
                search.toggleSearch();

                drawerLayout.openDrawer(llleft_drawer);


                setAnimation(llm_b);
                setAnimation(llm_c);
                setAnimation(llm_d);
            }

        });
        search.setSearchListener(new SearchListener() {

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
                search.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSearchTermChanged(String term) {
                //React to the search term changing
                //Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(MainActivity.this, searchTerm + " Searched", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }

        });
        search.setOverflowMenu(R.menu.overflow_menu);
        search.setOverflowMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.test_menu_item:
                        Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });


        llm_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();


                //startActivity(new Intent(MainYouTubeActivity.this, PlayActivity.class));
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));

            }
        });

        llm_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();


                startActivity(new Intent(MainActivity.this, ActAddUsername.class));
            }
        });


        llm_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ActData.class));
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

    public void reveal(View v) {
        startActivity(new Intent(this, RevealActivity.class));
    }


    public void menuDrawer(View v) {
        startActivity(new Intent(this, MyMenuDrawer.class));
    }


    public void menuPlay(View v) {
        startActivity(new Intent(this, PlayActivity.class));
    }


    @Override
    public void onBackPressed() {

        if(search.isShown())
        {
            Toast.makeText(MainActivity.this, "Back press!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Back press--Visible!", Toast.LENGTH_SHORT).show();
            search.setVisibility(View.VISIBLE);
        }

        //  super.onBackPressed();
    }


    public void setAnimation(View iv) {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(0, 200, 0, 0);
        trAnimation.setStartOffset(1000);
        trAnimation.setDuration(1000);

        trAnimation.setRepeatMode(Animation.REVERSE);// ---------> This will make the view translate in the reverse direction

        set.addAnimation(trAnimation);
        Animation anim = new AlphaAnimation(0.2f, 1.0f);
        anim.setDuration(1000);
        set.addAnimation(anim);
        iv.startAnimation(set);
    }
}

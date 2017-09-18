package com.downloader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sen.R;


public class DownloaderMainActivity extends AppCompatActivity {//implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_main);
     /*   findViewById(R.id.btnListView).setOnClickListener(this);
        findViewById(R.id.btnRecyclerView).setOnClickListener(this);*/

        Button btnListView = (Button)findViewById(R.id.btnListView);
        Button btnRecyclerView = (Button)findViewById(R.id.btnRecyclerView);

        btnListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("=Click=","===btnListView===");
                Intent intent = new Intent(DownloaderMainActivity.this, AppListActivity.class);
                intent.putExtra("EXTRA_TYPE", AppListActivity.TYPE.TYPE_LISTVIEW);
                startActivity(intent);
            }
        });

        btnRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("=Click=","===btnRecyclerView===");
                Intent intent = new Intent(DownloaderMainActivity.this, AppListActivity.class);

                intent.putExtra("EXTRA_TYPE", AppListActivity.TYPE.TYPE_RECYCLERVIEW);
                startActivity(intent);
            }
        });

    }

   /* @Override
    public void onClick(View v) {
        Log.d("=Click=","===btnListView===");
        Intent intent = new Intent(this, AppListActivity.class);
        switch (v.getId()) {
            case R.id.btnListView:
                Log.d("=Click=","===btnListView===");
                intent.putExtra("EXTRA_TYPE", AppListActivity.TYPE.TYPE_LISTVIEW);
                break;
            case R.id.btnRecyclerView:
                Log.d("=Click=","===btnRecyclerView===");
                intent.putExtra("EXTRA_TYPE", AppListActivity.TYPE.TYPE_RECYCLERVIEW);
                break;
            default:
                return;
        }
        startActivity(intent);
    }
*/
}

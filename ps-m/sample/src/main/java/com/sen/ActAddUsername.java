package com.sen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sen.api.AppBase;
import com.sen.api.IApiMethods;
import com.sen.api.UsernameChannel;

import java.io.File;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Adminsss on 08-02-2016.
 */
public class ActAddUsername extends ActionBarActivity {

    TextView tvData;
    Button btnViewVideos,btnSearchVideos;
    EditText etAddUsername;

    String part = "snippet";
    String forUsername = "tseries";
    String key = "AIzaSyDGDGk8ctTus1DCsWgldVeq2UaUwO7i4WM";
    String order = "date";
    String maxResults = "20";

    String channelId = "";


    String Tag = "-tg--ActAddUsername.java---";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_username);
        tvData = (TextView) findViewById(R.id.tvData);

        btnViewVideos = (Button)findViewById(R.id.btnViewVideos);
        btnSearchVideos = (Button)findViewById(R.id.btnSearchVideos);

        etAddUsername = (EditText)findViewById(R.id.etAddUsername);
        etAddUsername.setText(""+forUsername);



        etAddUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if(etAddUsername.getText().toString().trim().length() > 1)
                    {
                        forUsername = etAddUsername.getText().toString().trim();
                        asyncGetUserchannel();
                    }
                    else
                    {
                       Toast.makeText(ActAddUsername.this, "Please enter any username", Toast.LENGTH_SHORT).show();
                    }




                    hideKeyBoard(v);
                    return true;
                }
                return false;
            }
        });


        btnViewVideos.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                if(channelId.toString().length() > 4)
                {
//                    Toast.makeText(ActUsernameVidList.this, "View!", Toast.LENGTH_SHORT).show();

                    Intent intActUsernameVidList = new Intent(ActAddUsername.this, ActUsernameVidList.class);
                    intActUsernameVidList.putExtra("channelId",channelId);
                    startActivity(intActUsernameVidList);
                }
                else
                {
                    Toast.makeText(ActAddUsername.this, "Please enter valid username. \n No Channel found! ", Toast.LENGTH_SHORT).show();
                    Log.v(Tag, "No Channel id found");
                }
            }
        });

        btnSearchVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intActUsernameVidList = new Intent(ActAddUsername.this, ActSearchVidList.class);
                //intActUsernameVidList.putExtra("channelId",channelId);
                Intent intActUsernameVidList = new Intent(ActAddUsername.this, ActSearchVidTitleList.class);

                startActivity(intActUsernameVidList);
            }
        });



try{
    getApkFiles();
}
catch (Exception e)
{
    e.printStackTrace();
}



    }


   @SuppressLint("LongLogTag")
   private void getApkFiles()
    {
        try {
            File appsDir = new File("/data/app");

            String[] files = appsDir.list();

            for (int i = 0; i < files.length; i++) {
                Log.d(Tag, "File: " + files[i]);

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void asyncGetUserchannel()
    {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppBase.strBaseUrl)
                .build();
        IApiMethods methods = restAdapter.create(IApiMethods.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                UsernameChannel curators = (UsernameChannel) o;
                tvData.setText(">>" + curators.getKind() + "<<");

                if(curators.getArrayListItems() !=null && curators.getArrayListItems().size() > 0)
                {
                    for (int i=0; i< curators.getArrayListItems().size() ; i++)
                    {
                        tvData.setText(i+"-->"+curators.getArrayListItems().get(i).getId());

                        channelId = curators.getArrayListItems().get(i).getId();
                    }

                }

                if(channelId !=null && channelId.length() > 1)
                {
                    Toast.makeText(ActAddUsername.this, "Channel found -"+channelId, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ActAddUsername.this, "No Channel found!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        };
        methods.getUserChannelName(part, forUsername, key, order, maxResults, callback);
    }


    public void hideKeyBoard(View view)
    {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

 /*   private class BackgroundTask extends AsyncTask<Void, Void,Curator> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .build();
        }

        @Override
        protected Curator doInBackground(Void... params) {
            IApiMethods methods = restAdapter.create(IApiMethods.class);
            Curator curators = methods.getUserChannelName(API_KEY);

            return curators;
        }

        @Override
        protected void onPostExecute(Curator curators) {
            tvData.setText(curators.title + "\n\n");
            for (Curator.Dataset dataset : curators.dataset) {
                tvData.setText(tvData.getText() + dataset.curator_title +" - " + dataset.curator_tagline + "\n");
            }
        }
    }*/
}

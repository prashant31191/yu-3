package com.youtube;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.quinny898.library.persistentsearch.SearchBox;
import com.sen.ActSearchVidTitleList;
import com.sen.R;
import com.sen.api.AppBase;
import com.sen.api.IApiMethods;
import com.sen.api.SearchVideoTitle;
import com.youtube.save.MyDownloader;
import com.youtube.utils.CustomProgressDialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Admin on 7/5/2016.
 */

public class ActWebviewTag extends ActionBarActivity {

    Bundle bundle;
     SearchBox search;
     Toolbar toolbar;
    WebView wvData;
    TextView tvTagData;
    EditText etTagData;
    CustomProgressDialog customProgressDialog;

    String strVideoTitle = "test"; // title or file name
    String q="DxoshrYtNC8"; // video id

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);



    setContentView(R.layout.act_webview_tag);


    customProgressDialog = new CustomProgressDialog(ActWebviewTag.this);

    bundle = getIntent().getExtras();

    if (bundle != null) {

        if (bundle.getString("channelId") != null && bundle.getString("channelId").toString().length() > 1) {
            q = bundle.getString("channelId");
        }

        if (bundle.getString("title") != null && bundle.getString("title").toString().length() > 1) {
            strVideoTitle = bundle.getString("title");
        }
    }





    search = (SearchBox) findViewById(R.id.searchbox);
    tvTagData = (TextView)findViewById(R.id.tvTagData);
        wvData = (WebView)findViewById(R.id.wvData);
        etTagData = (EditText)findViewById(R.id.etTagData);
    search.enableVoiceRecognition(this);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitleTextColor(Color.WHITE);
    toolbar.setTitle("Search videos");

    this.setSupportActionBar(toolbar);
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            return true;
        }
    });



    //new  RetrieveSiteData().execute("http://keepvid.com/?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DRgKAFK5djSk");

        Log.i("--Link--","=="+"http://keepvid.com/?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3D"+q);
        //https://www.youtube.com/watch?v=erxxonSJhZk
        //http://keepvid.com/?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DerxxonSJhZk
    new  RetrieveSiteData().execute("http://keepvid.com/?url=https://www.youtube.com/watch?v="+q);
        customProgressDialog.show();
    //view-source:http://keepvid.com/?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DRgKAFK5djSk







}


    class RetrieveSiteData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder builder = new StringBuilder(100000);

            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        builder.append(s);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("--post--","--Data--"+result);
            customProgressDialog.dismiss();
            tvTagData.setText("-----Data----\n"+result);
            etTagData.setText("-----Data----\n"+result);


            if(result !=null && result.contains("\"applets\""))
            {

                int positionStart = result.indexOf("\"applets\"");
                Log.e("--post--","--Data-position=-applets---="+positionStart);

                int positionCenter = result.indexOf("id=\"info\"");
                Log.e("--post--","--Data-position=-info---="+positionCenter);



                int positionEnd = result.lastIndexOf("\"msg-error\"");
                        // indexOf("\"msg-error\"");
                Log.e("--post--","--Data-position=-msg-error---="+positionEnd);




                String finalData = result.substring(result.indexOf("\"applets\"") + 1, result.lastIndexOf("\"msg-error\""));

                Log.e("--post--","--Final-Data-"+finalData);
                tvTagData.setText("-----Data----\n"+finalData);
                      //  tvTagData.setText(Html.fromHtml(finalData));
                // 1111 tvTagData. setMovementMethod(LinkMovementMethod.getInstance());
                wvData.getSettings().setJavaScriptEnabled(true);
                wvData.loadData(finalData, "text/html", "UTF-8");

                // 111

                //new DownloadManager(ActWebviewTag.this).execute(strUrl, "filename");

              /*

              1111


              tvTagData.setMovementMethod(new TextViewLinkHandler() {
                @Override
                public void onLinkClick(String strUrl) {
                  //  Toast.makeText(tvTagData.getContext(), url, Toast.LENGTH_LONG).show();
                    try {

                      //  Snackbar.make(tvTagData," url \n"+url,Snackbar.LENGTH_LONG).show();
                        //  String urlPath = obj.stream_url + "?client_id=nnlknlkl";
                        Log.e("--post--","--Link--"+strUrl);



                        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(strUrl);
                        } else {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("text label",strUrl);
                            clipboard.setPrimaryClip(clip);
                        }

                        showDownloadAlert(strUrl,strVideoTitle+".mp4");
                        //showDownloadAlert(strUrl,"myadda.mp4");
                      } catch (Exception e) {
                          // TODO: handle exception
                          e.printStackTrace();
                      }
                }
            });*/



               /* tvTagData.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        boolean ret = false;
                        CharSequence text = ((TextView) v).getText();
                        Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
                        TextView widget = (TextView) v;
                        int action = event.getAction();

                        if (action == MotionEvent.ACTION_UP ||
                                action == MotionEvent.ACTION_DOWN) {
                            int x = (int) event.getX();
                            int y = (int) event.getY();

                            x -= widget.getTotalPaddingLeft();
                            y -= widget.getTotalPaddingTop();

                            x += widget.getScrollX();
                            y += widget.getScrollY();

                            Layout layout = widget.getLayout();
                            int line = layout.getLineForVertical(y);
                            int off = layout.getOffsetForHorizontal(line, x);

                            ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                            if (link.length != 0) {
                                if (action == MotionEvent.ACTION_UP) {
                                    link[0].onClick(widget);
                                }
                                ret = true;
                            }
                        }
                        return ret;
                    }
                });*/

                wvData.setWebViewClient(new WebViewClient()
                {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String strUrl)
                    {

                        Log.e("--post--","--Link--"+strUrl);


                        try {

                            //  Snackbar.make(tvTagData," url \n"+url,Snackbar.LENGTH_LONG).show();
                            //  String urlPath = obj.stream_url + "?client_id=nnlknlkl";
                            Log.e("--post--","--Link--"+strUrl);



                            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                clipboard.setText(strUrl);
                            } else {
                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                android.content.ClipData clip = android.content.ClipData.newPlainText("text label",strUrl);
                                clipboard.setPrimaryClip(clip);
                            }

                            showDownloadAlert(strUrl,strVideoTitle+".mp4");
                            //showDownloadAlert(strUrl,"myadda.mp4");
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }

                        return true;
                    }
                });



            }




        }
    }



    public abstract class TextViewLinkHandler extends LinkMovementMethod {

        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            if (event.getAction() != MotionEvent.ACTION_UP)
                return super.onTouchEvent(widget, buffer, event);

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length != 0) {
                onLinkClick(link[0].getURL());
            }
            return true;
        }

        abstract public void onLinkClick(String url);
    }



    private void showDownloadAlert(final String strUrl,final String strFilename)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Offline download video");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                MyDownloader downloader = new  MyDownloader();
                downloader.Download(ActWebviewTag.this, strUrl, strFilename);
                // Do nothing, but close the dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}

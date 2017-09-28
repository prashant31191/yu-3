package com.videbut01;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.videbut01.network.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
/**
 * Application
 */

public class App extends MultiDexApplication {

    /*private ApplicationComponent applicationComponent;*/

    private static Context mContext;
    private static App INSTANCE;
    private final static String ROBOTO_SLAB = "fonts/RobotoSlab-Regular.ttf";

    public static final String strBaseUrl = "https://www.googleapis.com/youtube/v3/";

    @Override
    public void onCreate() {
        super.onCreate();
        //initializeComponent();
//        configureDefaultFont(ROBOTO_SLAB);
        INSTANCE=this;
        mContext = getApplicationContext();

        //configureDefaultFont();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
        MultiDex.install(this);
    }


    private void configureDefaultFont(String robotoSlab) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(robotoSlab)
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
/*
    private void initializeComponent(){
       applicationComponent= DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .dataModule(new DataModule())
                .build();
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }*/

    @NonNull
    public static App appInstance(){
        return INSTANCE;
    }
/*

    public ApplicationComponent appComponent(){
        return applicationComponent;
    }
*/


   public static Retrofit getRetrofit()
   {
      Retrofit retrofitApiCall = new Retrofit.Builder()
               .baseUrl(App.strBaseUrl)
               .addConverterFactory(GsonConverterFactory.create())
               .build();

      return retrofitApiCall;
   }
    public static ApiService getApiService()
    {
        ApiService apiService = getRetrofit().create(ApiService.class);
        return apiService;
    }

    public static void showLog(Object o)
    {
        Log.d("===App-Show===","=====data===="+o.toString());
    }


    public static void showLog(Object o,Object o2)
    {
        Log.d("===App-Show==="+o.toString(),"=====data===="+o2.toString());
    }

}


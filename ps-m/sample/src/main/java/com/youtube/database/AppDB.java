package com.youtube.database;


import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadManager;
import com.downloader.CrashHandler;
import com.youtube.database.classes.DaoMaster;
import com.youtube.database.classes.DaoSession;

import java.io.File;

/**
 * Created by Admin on 7/2/2016.
 */

public class AppDB extends Application {
     public DaoSession daoSession;
     public SQLiteDatabase db;
     public DaoMaster.DevOpenHelper helper;
     public DaoMaster daoMaster;

     private static Context sContext;
     @Override
     public void onCreate() {
          super.onCreate();
          sContext = getApplicationContext();
          initDownloader();
          CrashHandler.getInstance(sContext);

          setupDatabase();

     }

     private void setupDatabase() {
          // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
          // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
          // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
          // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。

     /* helper = new DaoMaster.DevOpenHelper(this, Constants.DB_NAME, null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();*/

         /* File path = new File(Environment.getExternalStorageDirectory(), "sen/database/sen-db");
          path.getParentFile().mkdirs();*/

          //helper = new DaoMaster.DevOpenHelper(this, path.getAbsolutePath(), null);
          helper = new DaoMaster.DevOpenHelper(this, "sen", null);
          db = helper.getWritableDatabase();

          daoMaster = new DaoMaster(db);
          daoSession = daoMaster.newSession();



      /*  File path = new File(Environment.getExternalStorageDirectory(), "OrpheusApp/note");
        path.getParentFile().mkdirs();

        db = SQLiteDatabase.openOrCreateDatabase(path, null);
        DaoMaster.createAllTables(db, true);

        daoMaster = new DaoMaster(db);

        //daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
*/

     }

     public DaoSession getDaoSession() {
          return daoSession;
     }

     public SQLiteDatabase getDb() {
          return db;
     }




     // for the downloader

     private void initDownloader() {
          DownloadConfiguration configuration = new DownloadConfiguration();
          configuration.setMaxThreadNum(10);
          configuration.setThreadNum(3);
          DownloadManager.getInstance().init(getApplicationContext(), configuration);
     }

     public static Context getContext() {
          return sContext;
     }


}
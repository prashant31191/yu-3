package com.videbut01.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.videbut01.App;
import com.videbut01.R;
import com.videbut01.network.model.HomeListModel;

import java.util.ArrayList;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActHome extends ActAds {

    String TAG = "ActHome";

    SearchView svSearchVideos;
    OptionMenuAdapter optionMenuAdapter;
    RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4;

    ArrayList<HomeListModel> arrListMovies = new ArrayList<HomeListModel>();
    AListAdapter mAListAdapter;

    ArrayList<HomeListModel> arrListSongs = new ArrayList<HomeListModel>();
    BListAdapter mBListAdapter;

    ArrayList<HomeListModel> arrListSerial = new ArrayList<HomeListModel>();
    CListAdapter mCListAdapter;

    TextView tvTag3,tvTag5,tvTag7;

    String[] arrayMovie = null;
    String[] arraySong = null;
    String[] arrayTv = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        //ViewGroup.inflate(this, R.layout.act_home, ll_SubMainContainer);

        try{
            initialize();
            clickEvent();

            arrayMovie = getResources().getStringArray(R.array.strArrMovie);
            arraySong = getResources().getStringArray(R.array.strArrSong);
            arrayTv = getResources().getStringArray(R.array.strArrTv);


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void initialize(){
        try{


            /*----- This Activity -----*/

            recyclerView1 = (RecyclerView) findViewById(R.id.recyclerView1);
            recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
            recyclerView3 = (RecyclerView) findViewById(R.id.recyclerView3);
            recyclerView4 = (RecyclerView) findViewById(R.id.recyclerView4);
            svSearchVideos = (SearchView) findViewById(R.id.svSearchVideos);


            tvTag3 = (TextView) findViewById(R.id.tvTag3);
            tvTag5 = (TextView) findViewById(R.id.tvTag5);
            tvTag7 = (TextView) findViewById(R.id.tvTag7);


            setRecyclerView1();
            setRecyclerView2();
            setRecyclerView3();
            setRecyclerView4();



        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void clickEvent(){
        try{
            //ActSearch

            svSearchVideos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    String strKeyword = s.trim();
                    App.showLog("==search==strKeyword ==="+strKeyword);

                    if(strKeyword !=null && strKeyword .length() > 0) {

                        mNextLevelButton.performClick();

                        Intent intent = new Intent(ActHome.this, ActSearch.class);
                        intent.putExtra("keyword", strKeyword);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please enter search text",Toast.LENGTH_SHORT).show();
                    }


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });


            tvTag3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNextLevelButton.performClick();
                    Intent intent = new Intent(ActHome.this, ActSearch.class);
                    intent.putExtra("keyword", "English movies");
                    startActivity(intent);

                }
            });

            tvTag5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNextLevelButton.performClick();
                    Intent intent = new Intent(ActHome.this, ActSearch.class);
                    intent.putExtra("keyword", "English song");
                    startActivity(intent);

                }
            });

            tvTag7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNextLevelButton.performClick();
                    Intent intent = new Intent(ActHome.this, ActSearch.class);
                    intent.putExtra("keyword", "English serial");
                    startActivity(intent);

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /*------------------ 2. RecycleView Option Menu  ------------------*/
    public void setRecyclerView1(){
        try{

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActHome.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView1.setLayoutManager(linearLayoutManager);
            recyclerView1.setHasFixedSize(true);

            String strOptionMenu[] = {"Hollywood Movie", "Songs", "Serial", "Live Sports", "Football", "World News", "Pop Song", "Action Movies"};

            optionMenuAdapter = new OptionMenuAdapter(ActHome.this, strOptionMenu);
            recyclerView1.setAdapter(optionMenuAdapter);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class OptionMenuAdapter extends RecyclerView.Adapter<OptionMenuAdapter.VersionViewHolder> {
        String strOptionMenu[];
        Context mContext;



        public OptionMenuAdapter(Context context, String strOptionMenu[]) {
            this.strOptionMenu = strOptionMenu;
            mContext = context;
        }

        @Override
        public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_home_optionmenu, viewGroup, false);
            VersionViewHolder viewHolder = new VersionViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final VersionViewHolder versionViewHolder, final int i) {
            try {

                versionViewHolder.tvOption.setText(strOptionMenu[i]);

                versionViewHolder.tvOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActHome.this, "Search for " + strOptionMenu[i], Toast.LENGTH_SHORT).show();

                        mNextLevelButton.performClick();
                        Intent intent = new Intent(ActHome.this, ActSearch.class);
                        intent.putExtra("keyword", strOptionMenu[i]);
                        startActivity(intent);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return strOptionMenu.length;
        }


        class VersionViewHolder extends RecyclerView.ViewHolder {
            TextView tvOption;

            public VersionViewHolder(View itemView) {
                super(itemView);

                tvOption = (TextView) itemView.findViewById(R.id.tvOption);

            }

        }
    }



    /*------------------ 3. RecycleView Latest Arrive  ------------------*/
    public void setRecyclerView2(){
        try{

            // http://www.imdb.com/list/ls064733394/

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActHome.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView2.setLayoutManager(linearLayoutManager);
            recyclerView2.setHasFixedSize(true);

            String m1 = "Pirates of the Caribbean: Dead Men Tell No Tales";
            String mi1 = "https://images-na.ssl-images-amazon.com/images/M/MV5BMTYyMTcxNzc5M15BMl5BanBnXkFtZTgwOTg2ODE2MTI@._V1._SY209_CR0,0,140,209_.jpg";


            String m2 = "Thor: Ragnarok (2017)";
            String mi2 = "https://images-na.ssl-images-amazon.com/images/M/MV5BMjMyNDkzMzI1OF5BMl5BanBnXkFtZTgwODcxODg5MjI@._V1._SY209_CR0,0,140,209_.jpg";


            String m3 = "Guardians of the Galaxy Vol. 2 (2017)";
            String mi3 = "https://images-na.ssl-images-amazon.com/images/M/MV5BMTg2MzI1MTg3OF5BMl5BanBnXkFtZTgwNTU3NDA2MTI@._V1._SY209_CR0,0,140,209_.jpg";


            String m4 = "Justice League";
            String mi4 = "https://images-na.ssl-images-amazon.com/images/M/MV5BMjI2NjI2MDQ0NV5BMl5BanBnXkFtZTgwMTc1MjAwMjI@._V1._SY209_CR0,0,140,209_.jpg";


            String m5 = "The Fate of the Furious (2017)";
            String mi5 = "https://images-na.ssl-images-amazon.com/images/M/MV5BMjMxODI2NDM5Nl5BMl5BanBnXkFtZTgwNjgzOTk1MTI@._V1._SX140_CR0,0,140,209_.jpg";


            String productid[] = {"1", "2", "3", "4", "5", "6"};
            //int productimg[] = {R.drawable.header, R.drawable.header, R.drawable.header,R.drawable.header, R.drawable.header, R.drawable.header};
            String productName[] = {m1, m2, m3, m4, m5, m1};
            String productimg[] = {mi1, mi2, mi3, mi4, mi5, mi1};
            String compamyName[] = {"Full movie", "Trailer", "Full movie", "Full movie", "Trailer", "Full movie"};
            String rating[] = {"3", "4", "2", "5", "3", "1"};
            String price[] = {"412", "25", "130", "221", "150", "300"};


            for(int i=0; i<productid.length; i++){
                arrListMovies.add(new HomeListModel(productid[i], String.valueOf(productimg[i]), productName[i], compamyName[i], rating[i], price[i]));
            }


            mAListAdapter = new AListAdapter(ActHome.this, arrListMovies);
            recyclerView2.setAdapter(mAListAdapter);
            recyclerView2.setItemAnimator(new DefaultItemAnimator());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class AListAdapter extends RecyclerView.Adapter<AListAdapter.VersionViewHolder> {
        ArrayList<HomeListModel> arrListMovies;
        Context mContext;



        public AListAdapter(Context context, ArrayList<HomeListModel> arrListMovies) {
            this.arrListMovies = arrListMovies;
            mContext = context;
        }

        @Override
        public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_home_latestarrival, viewGroup, false);
            VersionViewHolder viewHolder = new VersionViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final VersionViewHolder versionViewHolder, final int i) {
            try {

                //versionViewHolder.ivImg.setImageResource(Integer.parseInt(arrListMovies.get(i).productimg));
                versionViewHolder.tvProductName.setText(arrListMovies.get(i).productName);
                versionViewHolder.tvCompanyName.setText(arrListMovies.get(i).compamyName);
                versionViewHolder.tvRating.setText(arrListMovies.get(i).rating);
                versionViewHolder.tvPrice.setText("$"+arrListMovies.get(i).price);
                App.showLog("----AListAdapter----"+arrListMovies.get(i).productimg);
                Glide.with(mContext).load(arrListMovies.get(i).productimg).into(versionViewHolder.ivImg);

                versionViewHolder.tvDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNextLevelButton.performClick();

                    }
                });

                versionViewHolder.ivImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNextLevelButton.performClick();

                        Intent intYouTubePlayerView = new Intent(ActHome.this, ActPlay.class);
                        intYouTubePlayerView.putExtra("from", "ActSearchVidTitleList");
                        String randomStrVid = arrayMovie[new Random().nextInt(arrayMovie.length)];
                        intYouTubePlayerView.putExtra("videoID", randomStrVid);
                        mContext.startActivity(intYouTubePlayerView);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return arrListMovies.size();
        }


        class VersionViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImg;
            TextView tvProductName, tvCompanyName, tvRating, tvPrice,tvDownload;

            public VersionViewHolder(View itemView) {
                super(itemView);

                ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
                tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);

                tvCompanyName = (TextView) itemView.findViewById(R.id.tvCompanyName);
                tvRating = (TextView) itemView.findViewById(R.id.tvRating);
                tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
                tvDownload = (TextView) itemView.findViewById(R.id.tvDownload);

            }

        }
    }


    /*------------------ 4. Action Week ------------------*/
    public void setRecyclerView4(){
        try{

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActHome.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView4.setLayoutManager(linearLayoutManager);
            recyclerView4.setHasFixedSize(true);

            String m1 = "Pirates of the Caribbean: Dead Men Tell No Tales";
            String mi1 = "https://images-na.ssl-images-amazon.com/images/M/MV5BMTYyMTcxNzc5M15BMl5BanBnXkFtZTgwOTg2ODE2MTI@._V1._SY209_CR0,0,140,209_.jpg";


            String m2 = "Thor: Ragnarok (2017)";
            String mi2 = "https://images-na.ssl-images-amazon.com/images/M/MV5BMjMyNDkzMzI1OF5BMl5BanBnXkFtZTgwODcxODg5MjI@._V1._SY209_CR0,0,140,209_.jpg";


            String m3 = "Guardians of the Galaxy Vol. 2 (2017)";
            String mi3 = "https://images-na.ssl-images-amazon.com/images/M/MV5BMTg2MzI1MTg3OF5BMl5BanBnXkFtZTgwNTU3NDA2MTI@._V1._SY209_CR0,0,140,209_.jpg";


            String m4 = "Justice League";
            String mi4 = "https://images-na.ssl-images-amazon.com/images/M/MV5BMjI2NjI2MDQ0NV5BMl5BanBnXkFtZTgwMTc1MjAwMjI@._V1._SY209_CR0,0,140,209_.jpg";


            String m5 = "The Fate of the Furious (2017)";
            String mi5 = "https://images-na.ssl-images-amazon.com/images/M/MV5BMjMxODI2NDM5Nl5BMl5BanBnXkFtZTgwNjgzOTk1MTI@._V1._SX140_CR0,0,140,209_.jpg";


            String productid[] = {"1", "2", "3", "4", "5", "6"};
            //int productimg[] = {R.drawable.header, R.drawable.header, R.drawable.header,R.drawable.header, R.drawable.header, R.drawable.header};
            String productName[] = {m1, m2, m3, m4, m5, m1};
            String productimg[] = {mi1, mi2, mi3, mi4, mi5, mi1};


            /*String productid[] = {"1", "2", "3", "4", "5", "6"};
            int productimg[] = {R.drawable.header, R.drawable.header, R.drawable.header,
                    R.drawable.header, R.drawable.header, R.drawable.header};
            String productName[] = {"Air Conditioner", "Electric Fans", "Air Coolers", "Table Fan", "Exhaust Fans", "Air Ventilators"};

            */
            String compamyName[] = {"Bharat Electronics", "Intex", "Voltas", "Intex", "Voltas", "Intex"};
            String rating[] = {"3", "4", "2", "5", "3", "1"};
            String price[] = {"412", "25", "130", "221", "150", "300"};


            for(int i=0; i<productid.length; i++){
                arrListSerial.add(new HomeListModel(productid[i], String.valueOf(productimg[i]), productName[i], compamyName[i], rating[i], price[i]));
            }


            mCListAdapter = new CListAdapter(ActHome.this, arrListSerial);
            recyclerView4.setAdapter(mCListAdapter);
            recyclerView4.setItemAnimator(new DefaultItemAnimator());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class CListAdapter extends RecyclerView.Adapter<CListAdapter.VersionViewHolder> {
        ArrayList<HomeListModel> arrListSerial;
        Context mContext;



        public CListAdapter(Context context, ArrayList<HomeListModel> arrListSerial) {
            this.arrListSerial = arrListSerial;
            mContext = context;
        }

        @Override
        public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_home_latestarrival, viewGroup, false);
            VersionViewHolder viewHolder = new VersionViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final VersionViewHolder versionViewHolder, final int i) {
            try {

               // versionViewHolder.ivImg.setImageResource(Integer.parseInt(arrListSongs.get(i).productimg));
                versionViewHolder.tvProductName.setText(arrListSerial.get(i).productName);
                versionViewHolder.tvCompanyName.setText(arrListSerial.get(i).compamyName);
                versionViewHolder.tvRating.setText(arrListSerial.get(i).rating);
                versionViewHolder.tvPrice.setText("$"+arrListSerial.get(i).price);
                App.showLog("----CListAdapter----"+arrListSerial.get(i).productimg);
                Glide.with(mContext).load(arrListSerial.get(i).productimg).into(versionViewHolder.ivImg);

                versionViewHolder.tvDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNextLevelButton.performClick();

                    }
                });

                versionViewHolder.ivImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNextLevelButton.performClick();

                        Intent intYouTubePlayerView = new Intent(ActHome.this, ActPlay.class);
                        intYouTubePlayerView.putExtra("from", "ActSearchVidTitleList");
                        String randomStrVid = arrayTv[new Random().nextInt(arrayTv.length)];
                        intYouTubePlayerView.putExtra("videoID", randomStrVid);
                        mContext.startActivity(intYouTubePlayerView);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return arrListSerial.size();
        }


        class VersionViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImg;
            TextView tvProductName, tvCompanyName, tvRating, tvPrice,tvDownload;

            public VersionViewHolder(View itemView) {
                super(itemView);

                ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
                tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
                tvCompanyName = (TextView) itemView.findViewById(R.id.tvCompanyName);
                tvRating = (TextView) itemView.findViewById(R.id.tvRating);
                tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
                tvDownload = (TextView) itemView.findViewById(R.id.tvDownload);

            }

        }
    }



    /*------------------ 5. RecycleView Latest Arrive-- bottom ------------------*/
    public void setRecyclerView3(){
        try{

            // https://www.letssingit.com/albums/popular/1

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActHome.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView3.setLayoutManager(linearLayoutManager);
            recyclerView3.setHasFixedSize(true);


            String m1 = "So Good [2017]";
            String mi1 = "https://img2.lsistatic.com/img/artists/pj29c/thumb_88677586.jpg";


            String m2 = "÷ [Divide] [2017]";
            String mi2 = "https://img2.lsistatic.com/img/artists/lktpz/thumb_22764638.jpg";


            String m3 = "Scooter Forever [2017]";
            String mi3 = "https://img2.lsistatic.com/img/artists/f987r/thumb_15224466.jpg";


            String m4 = "The Joshua Tree [1987]";
            String mi4 = "https://img2.lsistatic.com/img/artists/kj2mw/thumb_34679574.jpg";


            String m5 = "Rainbow [2017]";
            String mi5 = "https://img2.lsistatic.com/img/artists/m9sff/thumb_96127641.jpg";


            String productid[] = {"1", "2", "3", "4", "5", "6"};
            //int productimg[] = {R.drawable.header, R.drawable.header, R.drawable.header,R.drawable.header, R.drawable.header, R.drawable.header};
            String productName[] = {m1, m2, m3, m4, m5, m1};
            String productimg[] = {mi1, mi2, mi3, mi4, mi5, mi1};
            String compamyName[] = {"Zara Larsson", "Divide", "Scooter", "U2", "Kesha","Zara Larsson" };

           /* String productid[] = {"1", "2", "3", "4", "5", "6"};
            int productimg[] = {R.drawable.header, R.drawable.header, R.drawable.header,
                    R.drawable.header, R.drawable.header, R.drawable.header};
            String productName[] = {"Air Conditioner", "Electric Fans", "Air Coolers", "Table Fan", "Exhaust Fans", "Air Ventilators"};
            String compamyName[] = {"Bharat Electronics", "Intex", "Voltas", "Intex", "Voltas", "Intex"};
           */
            String rating[] = {"3", "4", "2", "5", "3", "1"};
            String price[] = {"412", "25", "130", "221", "150", "300"};


            for(int i=0; i<productid.length; i++){
                arrListSongs.add(new HomeListModel(productid[i], String.valueOf(productimg[i]), productName[i], compamyName[i], rating[i], price[i]));
            }


            mBListAdapter = new BListAdapter(ActHome.this, arrListSongs);
            recyclerView3.setAdapter(mBListAdapter);
            recyclerView3.setItemAnimator(new DefaultItemAnimator());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class BListAdapter extends RecyclerView.Adapter<BListAdapter.VersionViewHolder> {
        ArrayList<HomeListModel> arrListSongs;
        Context mContext;



        public BListAdapter(Context context, ArrayList<HomeListModel> arrListSongs) {
            this.arrListSongs = arrListSongs;
            mContext = context;
        }

        @Override
        public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_home_latestarrival, viewGroup, false);
            VersionViewHolder viewHolder = new VersionViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final VersionViewHolder versionViewHolder, final int i) {
            try {

              //  versionViewHolder.ivImg.setImageResource(Integer.parseInt(arrListSongs.get(i).productimg));
                versionViewHolder.tvProductName.setText(arrListSongs.get(i).productName);
                versionViewHolder.tvCompanyName.setText(arrListSongs.get(i).compamyName);
                versionViewHolder.tvRating.setText(arrListSongs.get(i).rating);
                versionViewHolder.tvPrice.setText("$"+arrListSongs.get(i).price);
                App.showLog("----BListAdapter----"+arrListSongs.get(i).productimg);
                Glide.with(mContext).load(arrListSongs.get(i).productimg).into(versionViewHolder.ivImg);
                versionViewHolder.tvDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNextLevelButton.performClick();

                    }
                });

                versionViewHolder.ivImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNextLevelButton.performClick();

                        Intent intYouTubePlayerView = new Intent(ActHome.this, ActPlay.class);
                        intYouTubePlayerView.putExtra("from", "ActSearchVidTitleList");
                        String randomStrVid = arraySong[new Random().nextInt(arraySong.length)];
                        intYouTubePlayerView.putExtra("videoID", randomStrVid);
                        mContext.startActivity(intYouTubePlayerView);

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return arrListSongs.size();
        }


        class VersionViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImg;
            TextView tvProductName, tvCompanyName, tvRating, tvPrice,tvDownload;

            public VersionViewHolder(View itemView) {
                super(itemView);

                ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
                tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);

                tvCompanyName = (TextView) itemView.findViewById(R.id.tvCompanyName);
                tvRating = (TextView) itemView.findViewById(R.id.tvRating);
                tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
                tvDownload = (TextView) itemView.findViewById(R.id.tvDownload);

            }

        }
    }




}

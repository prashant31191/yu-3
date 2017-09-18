package com.youtube.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.sen.ActYouTubePlayer;
import com.sen.R;
import com.squareup.picasso.Picasso;
import com.youtube.adapters.RecentAdapter;
import com.youtube.apicall.YTrailerModel;
import com.youtube.models.RecentModel;
import com.youtube.utils.MyData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 6/30/2016.
 */

public class RecentFragment extends android.support.v4.app.Fragment {
    int color;
    //RecentAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<String> listAllString;
    ArrayList<String> listPageDataString;
    PaginationAdapter adapter1;
    int page = 1;
    int pageSize = 20;
    int pageLoadSize = 18;


    public RecentFragment() {
    }

    @SuppressLint("ValidFragment")
    public RecentFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor(color);

         recyclerView = (RecyclerView) view.findViewById(R.id.rvRecent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < RecentModel.data.length; i++) {
            list.add(RecentModel.data[i]);
        }

        //adapter = new RecentAdapter(list);
       // recyclerView.setAdapter(adapter);






        setDataToRecieclerView();
        return view;
    }

    private void setDataToRecieclerView() {

        listAllString = new ArrayList<String>();
        listPageDataString = new ArrayList<String>();
        for (int i=0; i<1000; i++)
        {
            listAllString.add("== Item =="+i);

        }

        setDataToAdapter();

    }


    private void setDataToAdapter()
    {
        Log.i("-set data-","=PaginationAdapter=Set data to adapter notify=page="+page);


        if(listAllString !=null && listPageDataString!=null  && (listPageDataString.size() < listAllString.size()))
        {

            if(page*pageSize < listAllString.size())
            {
                Log.i("-IF-","==page*pageSize < listAllString.size()=");
                listPageDataString.clear();
                for (int j = 0; j < page * pageSize; j++) {
                    listPageDataString.add(listAllString.get(j));
                    Log.i("-set-",""+listAllString.get(j));
                }

            }
            else
            {
                Log.i("-ELSE-","==page*pageSize < listAllString.size()=");
            }

            if(page == 1)
            {
                Log.i("-IF-","= listPageDataString.size"+listPageDataString.size());
               adapter1 = new PaginationAdapter(getActivity(),listPageDataString);
                recyclerView.setAdapter(adapter1);
            }
            else
            {
                Log.i("-ELSE-","= listPageDataString.size"+listPageDataString.size());
                if(adapter1 !=null )
                {
                    adapter1.updateList(listPageDataString);
                }

            }
        }

    }


    public class PaginationAdapter  extends RecyclerView.Adapter<PaginationAdapter.VersionViewHolder> {
        ArrayList<String> versionModels;
        Context context;

        public PaginationAdapter(Context context,ArrayList<String> versionModels) {
            this.context = context;
            this.versionModels = versionModels;

        }

        @Override
        public PaginationAdapter.VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.raw_recent_fragment, viewGroup, false);
            PaginationAdapter.VersionViewHolder viewHolder = new PaginationAdapter.VersionViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PaginationAdapter.VersionViewHolder versionViewHolder, int i) {
            if(i > pageLoadSize)
            {
                page = page + 1;
                pageLoadSize = pageLoadSize + 18;
                setDataToAdapter();
            }
            versionViewHolder.title.setText(versionModels.get(i).toString());
        }

        @Override
        public int getItemCount() {
                return versionModels.size();
        }


       class VersionViewHolder extends RecyclerView.ViewHolder {
            CardView cardItemLayout;
            TextView title;
            TextView subTitle;

            public VersionViewHolder(View itemView) {
                super(itemView);

                cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
                title = (TextView) itemView.findViewById(R.id.listitem_name);
                subTitle = (TextView) itemView.findViewById(R.id.listitem_subname);
            }
        }

        public void updateList(final ArrayList<String> mArrayListItems) {
            Handler ha = new Handler();
            ha.post(new Runnable() {
                @Override
                public void run() {
                    versionModels = mArrayListItems;
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("--setUserVisibleHint--","==  RecentFragment  ==");
        }
        else {
        }
    }
}
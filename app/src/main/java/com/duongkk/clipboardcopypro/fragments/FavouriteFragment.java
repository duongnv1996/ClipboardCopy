package com.duongkk.clipboardcopypro.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.duongkk.clipboardcopypro.R;
import com.duongkk.clipboardcopypro.adapters.MessageFavouriteAdapter;
import com.duongkk.clipboardcopypro.databases.DatabaseHandler;
import com.duongkk.clipboardcopypro.interfaces.CallBackFirebase;
import com.duongkk.clipboardcopypro.models.Message;
import com.firebase.client.ChildEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends BaseFragment implements View.OnClickListener, CallBackFirebase {

    private  RecyclerView mRcvChat;
    private  static MessageFavouriteAdapter mAdapter;
    public   static List<Message> mListMessages;
    private   static LinearLayout mLayoutNotfound;
    private  DatabaseHandler mDb;
     View rootView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //RLog.e(SharedPref.getInstance(getContext()).getString(Constant.KEY_URL_ID,""));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  getList();
        mRcvChat = (RecyclerView) view.findViewById(R.id.rcv_chat);
        mLayoutNotfound = (LinearLayout) view.findViewById(R.id.ll_notfound);
        mRcvChat.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        mRcvChat.setLayoutManager(layoutManager);
        mListMessages = new ArrayList<>();
        mAdapter = new MessageFavouriteAdapter(getContext(), mListMessages, this);
        mRcvChat.setAdapter(mAdapter);
        new FavouriteAsyn() .execute();




        //  mRcvChat.setAdapter(new MessageFavouriteAdapter(getContext(),mListMessages , this));



//        mAdView = (AdView) view.findViewById(R.id.adView);
//        final AdRequest adRequest = new AdRequest.Builder()
//                .build()
//                ;
//
//        mAdView.loadAd(adRequest);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }



    @Override
    public void onResume() {
        super.onResume();
        if (mListMessages != null) {
          //  mListMessages.clear();
         //   mListMessages.addAll(mDb.getAllRows());
            mAdapter.notifyDataSetChanged();
            if (mListMessages.size() > 0) {
                mLayoutNotfound.setVisibility(View.GONE);
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    @Override
    public void remove(String code, final int pos) {

    }

    @Override
    public void update() {
        super.update();

    }

    class  FavouriteAsyn extends AsyncTask<Void,Void,Void>{
        public FavouriteAsyn(){
            if(mDb==null)
            mDb = new DatabaseHandler(getContext());
        }
        @Override
        protected Void doInBackground(Void... voids) {
            mListMessages.addAll(mDb.getAllRows());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mListMessages.size() > 0) {
                mLayoutNotfound.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}

package com.duongkk.clipboardcopy.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.duongkk.clipboardcopy.R;
import com.duongkk.clipboardcopy.adapters.MessageFavouriteAdapter;
import com.duongkk.clipboardcopy.databases.DatabaseHandler;
import com.duongkk.clipboardcopy.interfaces.CallBackFirebase;
import com.duongkk.clipboardcopy.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends BaseFragment implements View.OnClickListener, CallBackFirebase {

    private  RecyclerView mRcvChat;
    private  static MessageFavouriteAdapter mAdapter;
    public   static List<Message> mListMessages;
    private   LinearLayout mLayoutNotfound;
    private  DatabaseHandler mDb;
     View rootView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = new DatabaseHandler(getContext());
        //RLog.e(SharedPref.getInstance(getContext()).getString(Constant.KEY_URL_ID,""));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  getList();
        mRcvChat = (RecyclerView) view.findViewById(R.id.rcv_chat);
        mRcvChat.setHasFixedSize(true);
        mRcvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        mListMessages = new ArrayList<>();
        mListMessages.addAll(mDb.getAllRows());
        mAdapter = new MessageFavouriteAdapter(getContext(), mListMessages, this);
        mRcvChat.setAdapter(mAdapter);
        mLayoutNotfound = (LinearLayout) view.findViewById(R.id.ll_notfound);
        if (mListMessages.size() > 0) {
            mLayoutNotfound.setVisibility(View.GONE);
        }
        //  mRcvChat.setAdapter(new MessageFavouriteAdapter(getContext(),mListMessages , this));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListMessages != null) {
          //  mListMessages.clear();
         //   mListMessages.addAll(mDb.getAllRows());
            mAdapter.notifyDataSetChanged();

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
    public void update(List<Message> msgs) {
        super.update(msgs);

    }
}

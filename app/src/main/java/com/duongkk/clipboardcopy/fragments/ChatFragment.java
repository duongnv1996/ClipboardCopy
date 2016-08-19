package com.duongkk.clipboardcopy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.duongkk.clipboardcopy.AppController;
import com.duongkk.clipboardcopy.R;
import com.duongkk.clipboardcopy.adapters.MessageAdapter;
import com.duongkk.clipboardcopy.models.Message;
import com.duongkk.clipboardcopy.utils.CommonUtils;
import com.duongkk.clipboardcopy.utils.Constant;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 8/19/2016.
 */
public class ChatFragment extends Fragment implements ChildEventListener,View.OnClickListener{
    private RecyclerView mRcvChat;
    private MessageAdapter mAdapter;
    private List<Message> mListMessages;
    private Firebase mRoot;

    private EditText mEdtMessage;
    private FloatingActionButton mBtnSend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getContext());
        mRoot = new Firebase(Constant.URL_ROOT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRcvChat = (RecyclerView)view.findViewById(R.id.rcv_chat);
        mRcvChat.setHasFixedSize(true);
        mRcvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        mListMessages = new ArrayList<>();


        mAdapter = new MessageAdapter(getContext(), mListMessages);
        mRcvChat.setAdapter(mAdapter);


        mEdtMessage = (EditText) view.findViewById(R.id.edt_chat);
        mBtnSend = (FloatingActionButton) view.findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mEdtMessage.setOnClickListener(this);
        mRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        mRoot.addChildEventListener(this);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentchat, container, false);
        return rootView;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Message msg = dataSnapshot.getValue(Message.class);
        if(msg!=null){
            if(msg.getId().equals(AppController.getInstance().getImei())) msg.setClient(true);
            mListMessages.add(msg);
           mAdapter.notifyDataSetChanged();
            mRcvChat.smoothScrollToPosition(mAdapter.getItemCount()-1);
        }

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.edt_chat:{
                if(mAdapter.getItemCount()>0){
                    mRcvChat.smoothScrollToPosition(mAdapter.getItemCount()-1);
                }
                break;
            }

            case R.id.btn_send:{

                String content = mEdtMessage.getText().toString();
                if(!content.equals("")){
                    Message msg = new Message();
                    msg.setId(CommonUtils.getImei(getContext()));
                    msg.setDate(CommonUtils.getCurrentTime());
                    msg.setContent(content);
                    mRoot.push().setValue(msg, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            mEdtMessage.setText("");
                        }
                    });
                }
                break;
            }
        }
    }
}

package com.duongkk.clipboardcopy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.duongkk.clipboardcopy.R;
import com.duongkk.clipboardcopy.adapters.MessageAdapter;
import com.duongkk.clipboardcopy.application.AppController;
import com.duongkk.clipboardcopy.interfaces.CallBackFirebase;
import com.duongkk.clipboardcopy.models.Message;
import com.duongkk.clipboardcopy.utils.CommonUtils;
import com.duongkk.clipboardcopy.utils.Constant;
import com.duongkk.clipboardcopy.utils.RLog;
import com.duongkk.clipboardcopy.utils.SharedPref;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 8/19/2016.
 */
public class ChatFragment extends BaseFragment implements ChildEventListener,View.OnClickListener,CallBackFirebase{
    private RecyclerView mRcvChat;
    private MessageAdapter mAdapter;
    private List<Message> mListMessages;
    private Firebase mRoot;

    private EditText mEdtMessage;
    private FloatingActionButton mBtnSend;
    private LinearLayout mLayoutNotfound;

    private String txt="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RLog.e(SharedPref.getInstance(getContext()).getString(Constant.KEY_URL_ID,""));
        mRoot = new Firebase(Constant.URL_ROOT_FINAL+SharedPref.getInstance(getContext()).getString(Constant.KEY_URL_ID,""));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRcvChat = (RecyclerView)view.findViewById(R.id.rcv_chat);
        mRcvChat.setHasFixedSize(true);
        mRcvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        mListMessages = new ArrayList<>();


        mAdapter = new MessageAdapter(getContext(), mListMessages,this);
        mRcvChat.setAdapter(mAdapter);
        mRcvChat.setItemAnimator(new DefaultItemAnimator());

        mEdtMessage = (EditText) view.findViewById(R.id.edt_chat);
        mBtnSend = (FloatingActionButton) view.findViewById(R.id.btn_send);
        mBtnSend.setVisibility(View.VISIBLE);
        mBtnSend.showButtonInMenu(true);
        mBtnSend.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_up));
       // mBtnSend.setHideAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_down));
        mBtnSend.setOnClickListener(this);
        mEdtMessage.setOnClickListener(this);
        mRoot.addChildEventListener(ChatFragment.this);
        mLayoutNotfound =(LinearLayout)view.findViewById(R.id.ll_notfound);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragmentchat, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        txt="";
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        mLayoutNotfound.setVisibility(View.GONE);
        Message msg = dataSnapshot.getValue(Message.class);

        if(msg!=null && !msg.getContent().equals(txt)){
            txt = msg.getContent();
            msg.setCode(dataSnapshot.getKey());
            if(msg.getId().equals(AppController.getInstance().getImei())) msg.setClient(true);
            mListMessages.add(msg);
            mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
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
        RLog.e("Not found~");
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
                mBtnSend.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_down));
                mBtnSend.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_up));
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

    @Override
    public void remove(String code, final int pos) {
        Firebase node = mRoot.child(code);
        if(node!=null)
            node.removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                mAdapter.removeItem(pos);
            }
        });
    }

}

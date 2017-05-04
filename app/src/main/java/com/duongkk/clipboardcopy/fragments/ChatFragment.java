package com.duongkk.clipboardcopy.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
import com.firebase.client.ValueEventListener;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 8/19/2016.
 */
public class ChatFragment extends BaseFragment implements ChildEventListener,View.OnClickListener,CallBackFirebase, ValueEventListener {
    private RecyclerView mRcvChat;
    private CardView mCardMoreItem;
    private ProgressBar mLoading;
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
        mCardMoreItem = (CardView) view.findViewById(R.id.card_moreitem);
        mLoading = (ProgressBar)view.findViewById(R.id.progressBar) ;
        mLayoutNotfound =(LinearLayout)view.findViewById(R.id.ll_notfound);
        mRcvChat.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        mRcvChat.setLayoutManager(layoutManager);
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
        mCardMoreItem.setOnClickListener(this);
        mEdtMessage.setOnClickListener(this);
        mRoot.addChildEventListener(this);
        mRoot.addListenerForSingleValueEvent(this);
        mLoading.getIndeterminateDrawable().setColorFilter(ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null), PorterDuff.Mode.MULTIPLY);
        mRcvChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isAtBottom(mRcvChat)){
                    mCardMoreItem.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_down_faster));
                    mCardMoreItem.setVisibility(View.GONE);
                } else if(  dy<0 && mCardMoreItem.getVisibility() == View.GONE ){
                mCardMoreItem.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_up_faster));
                mCardMoreItem.setVisibility(View.VISIBLE);
            }
//                int visibleItemCount = layoutManager.getChildCount();
//                int totalItemCount = layoutManager.getItemCount();
//                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
//                    //End of list
//                    mCardMoreItem.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_down_faster));
//                    mCardMoreItem.setVisibility(View.GONE);
//                }else if( pastVisibleItems + visibleItemCount >= totalItemCount-2 &&   dy<0 && mCardMoreItem.getVisibility() == View.GONE ){
//                    mCardMoreItem.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.scale_up_faster));
//                    mCardMoreItem.setVisibility(View.VISIBLE);
//                }

            }
        });
    }

    public static boolean isAtBottom(RecyclerView recyclerView) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return isAtBottomBeforeIceCream(recyclerView);
        } else {
            return !ViewCompat.canScrollVertically(recyclerView, 1);
        }
    }

    private static boolean isAtBottomBeforeIceCream(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int count = recyclerView.getAdapter().getItemCount();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int pos = linearLayoutManager.findLastVisibleItemPosition();
            int lastChildBottom = linearLayoutManager.findViewByPosition(pos).getBottom();
            if (lastChildBottom == recyclerView.getHeight() - recyclerView.getPaddingBottom() && pos == count - 1)
                return true;
        }
        return false;
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
        try {
            Message msg = dataSnapshot.getValue(Message.class);
            if (msg != null && !msg.getContent().equals(txt)) {
                txt = msg.getContent();
                msg.setCode(dataSnapshot.getKey());
                if (msg.getId().equals(AppController.getInstance().getImei())) msg.setClient(true);
                mListMessages.add(msg);
                mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                mRcvChat.scrollToPosition(mAdapter.getItemCount() - 1);

            }
        }catch (Exception e){
            e.printStackTrace();
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

    //addListenerForSingleValueEvent
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mLoading.setVisibility(View.GONE);
        mLayoutNotfound.setVisibility(View.GONE);


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
            case R.id.card_moreitem:{
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

    public class ChatAsynTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            mRoot.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    try {
                        Message msg = dataSnapshot.getValue(Message.class);
                        if (msg != null && !msg.getContent().equals(txt)) {
                            txt = msg.getContent();
                            msg.setCode(dataSnapshot.getKey());
                            if (msg.getId().equals(AppController.getInstance().getImei())) msg.setClient(true);
                        //    mListMessages.add(msg);
                         //   publishProgress(null);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
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
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }
}

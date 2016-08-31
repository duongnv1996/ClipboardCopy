package com.duongkk.clipboardcopy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.duongkk.clipboardcopy.adapters.MessageSearchAdapter;
import com.duongkk.clipboardcopy.application.AppController;
import com.duongkk.clipboardcopy.databases.DatabaseHandler;
import com.duongkk.clipboardcopy.interfaces.CallBackFirebase;
import com.duongkk.clipboardcopy.models.Message;
import com.duongkk.clipboardcopy.utils.Constant;
import com.duongkk.clipboardcopy.utils.SharedPref;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements CallBackFirebase,ChildEventListener{

    List<Message> listMessages;
    DatabaseHandler db ;
    SearchView searchView;
    MenuItem searchMenuItem;

    Firebase mRoot;
    private RecyclerView mRcvChat;
    private MessageSearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db=new DatabaseHandler(this);
        Intent intent = getIntent();
        if(intent!=null){
            Bundle bundle = intent.getBundleExtra(Constant.KEY_BUNDLE);
            if(bundle!=null) listMessages = bundle.getParcelableArrayList(Constant.KEY_MSG);
        }
        if(listMessages==null) {
            listMessages = db.getAllRows();
        }else{
            listMessages.addAll(db.getAllRows());
        }
        mRoot = new Firebase(Constant.URL_ROOT_FINAL+ SharedPref.getInstance(this).getString(Constant.KEY_URL_ID,""));
        mRoot.addChildEventListener(this);
        mRcvChat = (RecyclerView) findViewById(R.id.rcv_chat);
        mRcvChat.setHasFixedSize(true);
        mRcvChat.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageSearchAdapter(this, listMessages, this);
        mRcvChat.setAdapter(mAdapter);
    }
    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public MenuItem getSearchMenuItem() {
        return searchMenuItem;
    }

    public void setSearchMenuItem(MenuItem searchMenuItem) {
        this.searchMenuItem = searchMenuItem;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView)menu.findItem(R.id.action_settings).getActionView();
        searchView.requestFocus();
        searchMenuItem= menu.findItem(R.id.action_settings);
        searchMenuItem.expandActionView();
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //((MessageSearchAdapter) mRcvChat.getAdapter()).resetView(false);
                if (newText.isEmpty()) {
                    ((MessageSearchAdapter) mRcvChat.getAdapter()).getFilter().filter("");
                } else {
                    ((MessageSearchAdapter) mRcvChat.getAdapter()).getFilter().filter(newText.toLowerCase());
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void remove(String code, int pos) {

    }
    String txt="";
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        try {
            Message msg = dataSnapshot.getValue(Message.class);
            if(msg!=null && !msg.getContent().equals(txt)){
                txt = msg.getContent();
                msg.setCode(dataSnapshot.getKey());
                if(msg.getId().equals(AppController.getInstance().getImei())) msg.setClient(true);
                listMessages.add(msg);
                mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
                //mRcvChat.smoothScrollToPosition(mAdapter.getItemCount()-1);
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
}

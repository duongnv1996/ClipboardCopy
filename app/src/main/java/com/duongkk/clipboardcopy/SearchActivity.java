package com.duongkk.clipboardcopy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.duongkk.clipboardcopy.databases.DatabaseHandler;
import com.duongkk.clipboardcopy.models.Message;
import com.duongkk.clipboardcopy.utils.Constant;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    MaterialSearchView searchView;
    List<Message> listMessages;
    DatabaseHandler db ;
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


    }



}

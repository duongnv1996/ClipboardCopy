package com.duongkk.clipboardcopypro.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.duongkk.clipboardcopypro.R;
import com.duongkk.clipboardcopypro.application.AppController;
import com.duongkk.clipboardcopypro.databases.DatabaseHandler;
import com.duongkk.clipboardcopypro.fragments.FavouriteFragment;
import com.duongkk.clipboardcopypro.interfaces.CallBackFirebase;
import com.duongkk.clipboardcopypro.models.Message;
import com.duongkk.clipboardcopypro.utils.CommonUtils;
import com.duongkk.clipboardcopypro.utils.Constant;
import com.duongkk.clipboardcopypro.utils.RLog;

import java.util.List;

import me.himanshusoni.chatmessageview.ChatMessageView;

/**
 * Created by MyPC on 8/19/2016.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    List<Message> listMessages;
    ClipboardManager clipboard;
    Context context;
    static  SparseBooleanArray sparseBooleanArray;
    String imei;
    DatabaseHandler db ;
    CallBackFirebase callbackFirebase;
    public MessageAdapter(Context context,List<Message> listMessages,CallBackFirebase callbackFirebase){
        this.listMessages = listMessages;
        this.callbackFirebase = callbackFirebase;
        this.context = context;
        sparseBooleanArray = new SparseBooleanArray();
        imei = AppController.getInstance().getImei();
        int i=0;
        clipboard= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        for (Message msg:this.listMessages) {
            if(msg.getId().equals(imei)){
                sparseBooleanArray.put(i,true);
            }
        }
        db = new DatabaseHandler(context);
    }
    public void addItem(Message msg){
        if(msg.getId().equals(AppController.getInstance().getImei())) msg.setClient(true);
       // if(msg.getId().equals(imei)) sparseBooleanArray.put(listMessages.size()-1,true);
        listMessages.add(msg);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false) );
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Message msg = listMessages.get(position);
        holder.cardMessage.setVisibility(View.VISIBLE);
        holder.cardMessageRecieve.setVisibility(View.VISIBLE);
        if(msg.isClient()){
            //sender
            holder.cardMessageRecieve.setVisibility(View.GONE);
            holder.tvContent.setText(msg.getContent());
            String date = getGreaterTime(msg);
            holder.tvTime.setText(date);
            holder.cardMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyTextContent(msg);
                }
            });
            holder.cardMessage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showMenuText(msg,view,position);
                    return true;
                }
            });
        }else{
            //recieve
            holder.cardMessage.setVisibility(View.GONE);
            holder.tvContentRecieve.setText(msg.getContent());
            String date = getGreaterTime(msg);
            holder.tvTimeRecieve.setText(date);
            holder.cardMessageRecieve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyTextContent(msg);
                }
            });
            holder.cardMessageRecieve.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showMenuText(msg,view,position);
                    return true;
                }
            });
        }

    }

    private void showMenuText(final Message msg, View view, final int pos) {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        new MaterialDialog.Builder(context).title(R.string.message)
                .itemsColorRes(R.color.primary_text)
                .items(R.array.item_long_click)
                .itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                switch (position){
                    case 0:{
                        copyTextContent(msg);
                        break;
                    }
                    case 1:{
                       CommonUtils.shareSimpleText(msg.getContent(),context);
                        break;
                    }
                    case 2:{
                      if( db.insertRow(msg) ==-1){
                          RLog.e("Cannot insert!");
                      }else{
                          if(FavouriteFragment.mListMessages!=null )
                              FavouriteFragment.mListMessages.add(msg);
                          Toast.makeText(context, R.string.add_favourite,Toast.LENGTH_SHORT).show();
                      }
                        break;
                    }
                    case 3:{
                     callbackFirebase.remove(msg.getCode(),pos);
                        break;
                    }
                }
            }
        }).show();
    }
    public void removeItem(int pos){
        if(pos<listMessages.size()){
            listMessages.remove(pos);
            notifyDataSetChanged();
        }
    }
    private void copyTextContent(Message msg) {
        AppController.getInstance().setCoppiedText(msg.getContent());
        clipboard.setText(msg.getContent());


        Toast.makeText(context, R.string.coppied,Toast.LENGTH_SHORT).show();
    }

    private String getGreaterTime(Message msg) {
        String currentDate = CommonUtils.getCurrentTimeByFormat(Constant.KEY_DATE_FORMAT);
        String date = msg.getDate();
        String arrTime[] = date.split(" ");
        if(currentDate.equals(arrTime[0])) date=arrTime[1];
        return date;
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvContent;
        TextView tvTime;
        ChatMessageView cardMessage;
        TextView tvContentRecieve;
        TextView tvTimeRecieve;
        ChatMessageView cardMessageRecieve;
        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            cardMessage = (ChatMessageView) itemView.findViewById(R.id.ll_sender);
            tvContentRecieve = (TextView) itemView.findViewById(R.id.tv_content_reciever);
            tvTimeRecieve = (TextView) itemView.findViewById(R.id.tv_time_reciever);
            cardMessageRecieve = (ChatMessageView) itemView.findViewById(R.id.ll_reciever);
        }

    }


}

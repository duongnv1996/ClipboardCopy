package com.duongkk.clipboardcopy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duongkk.clipboardcopy.AppController;
import com.duongkk.clipboardcopy.R;
import com.duongkk.clipboardcopy.models.Message;

import java.util.List;

import me.himanshusoni.chatmessageview.ChatMessageView;

/**
 * Created by MyPC on 8/19/2016.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    List<Message> listMessages;
    Context context;
    SparseBooleanArray sparseBooleanArray;
    String imei;
    public MessageAdapter(Context context,List<Message> listMessages){
        this.listMessages = listMessages;
        this.context = context;
        sparseBooleanArray = new SparseBooleanArray();
        imei = AppController.getInstance().getImei();
        int i=0;
        for (Message msg:this.listMessages) {
            if(msg.getId().equals(imei)){
                sparseBooleanArray.put(i,true);
            }
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false) );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message msg = listMessages.get(position);
        if(msg.isClient()){
            holder.cardMessage.setBackgroundColors(R.color.material_amber_800,R.color.material_amber_500);
            holder.tvTime.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvContent.setTextColor(context.getResources().getColor(R.color.white));
        }else{

           // holder =new ViewHolder( LayoutInflater.from(context).inflate(R.layout.item_server_chat,null,false));
            holder.cardMessage.setBackgroundColors(R.color.white,R.color.white_bg);
            holder.tvTime.setTextColor(context.getResources().getColor(R.color.secondary_text));
            holder.tvContent.setTextColor(context.getResources().getColor(R.color.primary_text));
        }
                    holder.tvContent.setText(msg.getContent());
                    holder.tvTime.setText(msg.getDate());
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvContent;
        TextView tvTime;
        ChatMessageView cardMessage;
        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            cardMessage = (ChatMessageView) itemView.findViewById(R.id.ll_message);
        }

    }
}

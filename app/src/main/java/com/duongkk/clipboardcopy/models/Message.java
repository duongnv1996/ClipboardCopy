package com.duongkk.clipboardcopy.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MyPC on 8/19/2016.
 */
public class Message implements Parcelable {
    String code;
    String id;
    String date;
    String content;
    boolean isClient;

    public Message(){};

    protected Message(Parcel in) {
        code = in.readString();
        id = in.readString();
        date = in.readString();
        content = in.readString();
        isClient = in.readByte() != 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(id);
        parcel.writeString(date);
        parcel.writeString(content);
        parcel.writeByte((byte) (isClient ? 1 : 0));
    }
}

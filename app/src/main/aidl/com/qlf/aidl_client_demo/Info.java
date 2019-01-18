package com.qlf.aidl_client_demo;

import android.os.Parcel;
import android.os.Parcelable;

public class Info implements Parcelable {
    private String content;

    public Info(){

    }

    public Info(Parcel in){
        content = in.readString();
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel source) {
            return new Info(source);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
    }

    /**
     * 参数是一个Parcel,用它来存储传输数据
     * @param dest
     */
    public void readFromParcel(Parcel dest){
        content = dest.readString();
    }

    @Override
    public String toString() {
        return "Info{" +
                "content='" + content + '\'' +
                '}';
    }
}

package com.lisheny.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/6/15 0015.
 */

public class Book implements Parcelable {

    private String name;
    private int price;

    protected Book(Parcel in) {
        name = in.readString();
        price = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getprice() {
        return price;
    }

    public void setprice(int price) {
        price = price;
    }

    public Book(String name, int price) {
        this.name = name;
        price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(price);
    }

//    public void  readFromParcel(Parcel parcel){
//        name = parcel.readString();
//        price = parcel.readInt();
//    }
}

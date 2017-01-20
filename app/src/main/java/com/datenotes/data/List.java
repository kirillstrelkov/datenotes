package com.datenotes.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class List extends BaseList implements Parcelable{
    public static final String KEY = "ui_notes_list";
    public static final long DEFAULT_ID = -1;

    private long id;

    public List() {
        super();
        id = DEFAULT_ID;
    }

    public List(String name) {
        super(name);
        id = DEFAULT_ID;
    }

    public List(String name, long id) {
        super(name);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeTypedList(notes);
    }

    public void setId(long id) {
        this.id = id;
    }

    public static final Creator<List> CREATOR = new Creator<List>() {
        @Override
        public List[] newArray(int size) {
            return new List[size];
        }

        @Override
        public List createFromParcel(Parcel source) {
            return new List(source);
        }
    };

    public List(Parcel source) {
        id = source.readLong();
        name = source.readString();
        notes = source.createTypedArrayList(UIBaseNote.CREATOR);
    }

    @Generated(hash = 438328932)
    public List(long id) {
        this.id = id;
    }
}

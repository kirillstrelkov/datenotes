package com.datenotes.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;

import java.text.ParseException;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Note extends BaseNote implements Parcelable{
    public static final String KEY_DELETE = "key_delete_note";
    public static final String KEY = "ui_date_note";
    public static final long DEFAULT_ID = -1;

    private long id;


    public Note(String note) {
        this(note, new Date());
    }

    public Note(String note, Date date) {
        this(note, date, DEFAULT_ID);
    }

    public Note(String note, Date date, long id) {
        super(note, date);
        this.id = id;
    }

    public Note(String note, String date, long id) throws ParseException {
        this(note, date);
        this.id = id;
    }

    public Note(String note, String date) throws ParseException {
        super(note, date);
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
        parcel.writeString(note);
        parcel.writeSerializable(date);
    }

    public void setId(long id) {
        this.id = id;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }

        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }
    };

    public Note(Parcel source) {
        super(source.readString(), (Date) source.readSerializable());
        id = source.readLong();
    }

    @Generated(hash = 65163784)
    public Note(long id) {
        this.id = id;
    }

    @Generated(hash = 1851166271)
    public Note() {
    }
}

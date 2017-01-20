package com.datenotes.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Note implements Parcelable {
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    public static final String KEY_DELETE = "key_delete_note";
    public static final String KEY = "key_note";

    public static final Long DEFAULT_ID = null;

    @Id
    private Long id;

    private String note;
    private Date date;
    private Long listId;

    public Note() {
        note = null;
        date = null;
        id = DEFAULT_ID;
    }

    public Note(String note) {
        this(note, new Date());
    }

    public Note(Long id, String note, Date date) {
        this.note = note;
        this.date = date;
        this.id = id;
    }

    public Note(Long id, String note, String date) throws ParseException {
        this(id, note, DATE_TIME_FORMAT.parse(date));
    }

    public Note(String note, String date) throws ParseException {
        this(note, DATE_TIME_FORMAT.parse(date));
    }

    public Note(String note, Date date) {
        this(DEFAULT_ID, note, date);
    }

    public Note(Long id, String note, String date, Long listId) throws ParseException {
        this(id, note, DATE_TIME_FORMAT.parse(date), listId);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFomattedDate() {
        return DATE_TIME_FORMAT.format(getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note dateNote = (Note) o;

        if (note != null ? !note.equals(dateNote.note) : dateNote.note != null) return false;
        return date != null ? date.equals(dateNote.date) : dateNote.date == null;

    }

    @Override
    public int hashCode() {
        int result = note != null ? note.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DateNote{" +
                "date=" + date +
                ", note='" + note + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(id);
        parcel.writeString(note);
        parcel.writeSerializable(date);
        parcel.writeLong(listId);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getListId() {
        return this.listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
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
        this((Long) source.readValue(Long.class.getClassLoader()),
                source.readString(),
                (Date) source.readSerializable(),
                source.readLong());
    }

    @Generated(hash = 2107550673)
    public Note(Long id, String note, Date date, Long listId) {
        this.id = id;
        this.note = note;
        this.date = date;
        this.listId = listId;
    }

    public void setDate(String date) throws ParseException {
        this.date = DATE_TIME_FORMAT.parse(date);
    }
}

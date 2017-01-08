package com.datenotes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateNote {
    public static final String KEY_NOTE = "key_note";
    public static final String KEY_DATE = "key_date";
    public static final String KEY_ID = "key_id";
    public static final long DEFAULT_ID = -1;
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    private String note;
    private Date date;
    private long id;

    public DateNote(String note) {
        this(note, new Date());
    }

    public DateNote(String note, Date date) {
        this(note, date, DEFAULT_ID);
    }

    public DateNote(String note, Date date, long id) {
        this.note = note;
        this.date = date;
        this.id = id;
    }

    public DateNote(String note, String date, long id) throws ParseException {
        this(note, date);
        this.id = id;
    }

    public DateNote(String note, String date) throws ParseException {
        this(note, DATE_TIME_FORMAT.parse(date));
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

    public long getId() {
        return id;
    }

    public String getFomattedDate() {
        return DATE_TIME_FORMAT.format(getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateNote dateNote = (DateNote) o;

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
}

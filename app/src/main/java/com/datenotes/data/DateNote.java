package com.datenotes.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateNote {
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    protected String note;
    protected Date date;

    DateNote(String note, Date date) {
        this.note = note;
        this.date = date;
    }

    DateNote(String note, String date) throws ParseException {
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

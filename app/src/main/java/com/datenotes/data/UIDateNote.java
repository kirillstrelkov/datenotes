package com.datenotes.data;

import java.text.ParseException;
import java.util.Date;

public class UIDateNote extends DateNote {
    public static final String KEY_NOTE = "key_note";
    public static final String KEY_DATE = "key_date";
    public static final String KEY_ID = "key_id";
    public static final String KEY_DELETE = "key_delete_note";
    public static final long DEFAULT_ID = -1;

    private long id;

    public UIDateNote(String note) {
        this(note, new Date());
    }

    public UIDateNote(String note, Date date) {
        this(note, date, DEFAULT_ID);
    }

    public UIDateNote(String note, Date date, long id) {
        super(note, date);
        this.id = id;
    }

    public UIDateNote(String note, String date, long id) throws ParseException {
        this(note, date);
        this.id = id;
    }

    public UIDateNote(String note, String date) throws ParseException {
        super(note, date);
    }

    public long getId() {
        return id;
    }

}

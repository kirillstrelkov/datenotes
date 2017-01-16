package com.datenotes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.datenotes.data.UIDateNote;
import com.datenotes.db.NotesContract.NoteEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class NoteIO {
    public static final SimpleDateFormat SQLLITE_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static NoteIO notesIO;
    private NotesDbHelper dbHelper;

    private NoteIO() {
    }

    private NoteIO(Context context) {
        dbHelper = new NotesDbHelper(context);
    }

    public static NoteIO create(Context context) {
        if (notesIO == null) {
            notesIO = new NoteIO(context);
        }
        return notesIO;
    }

    public List<UIDateNote> getDataFromDb() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_NAME_NOTE,
                NoteEntry.COLUMN_NAME_DATE
        };

        String sortOrder = NoteEntry.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = database.query(
                NoteEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<UIDateNote> notes = new LinkedList<>();
        while (cursor.moveToNext()) {
            String note = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_NOTE));
            String dateAsString = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_DATE));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(NoteEntry._ID));
            try {
                notes.add(new UIDateNote(note, SQLLITE_DATETIME_FORMAT.parse(dateAsString), id));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        return notes;
    }

    public long deleteNote(UIDateNote note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] args = {String.valueOf(note.getId())};
        return db.delete(NoteEntry.TABLE_NAME, " _id = ?", args);
    }

    public long addNoteToDb(UIDateNote note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_NAME_NOTE, note.getNote());
        values.put(NoteEntry.COLUMN_NAME_DATE, SQLLITE_DATETIME_FORMAT.format(note.getDate()));

        return db.insert(NoteEntry.TABLE_NAME, null, values);
    }

    public long updateNoteToDb(UIDateNote note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_NAME_NOTE, note.getNote());
        values.put(NoteEntry.COLUMN_NAME_DATE, SQLLITE_DATETIME_FORMAT.format(note.getDate()));

        String[] args = {String.valueOf(note.getId())};
        return db.update(NoteEntry.TABLE_NAME, values, " _id = ?", args);
    }
}

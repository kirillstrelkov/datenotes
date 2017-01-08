package com.datenotes.db;

import android.provider.BaseColumns;

public final class NotesContract {
    private NotesContract() {
    }

    public static class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_DATE = "date";
    }
}


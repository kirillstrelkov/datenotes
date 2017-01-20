package com.datenotes.data;

import com.datenotes.data.Note;

import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NoteTest {
    @Test
    public void newNoteWithDateAndNote() throws Exception {
        String note = "Message";
        Date date = new Date();
        Note dateNote = new Note(note, date);
        assertThat(dateNote.getDate(), is(date));
        assertThat(dateNote.getNote(), is(note));
    }

    @Test
    public void newNoteWithDateAndNoteAsString() throws Exception {
        String note = "Message";
        String formattedDate = "21:58 02.01.2017";
        Note dateNote = new Note(note, formattedDate);
        assertThat(dateNote.getNote(), is(note));
        assertThat(dateNote.getFomattedDate(), is(formattedDate));
    }

    @Test
    public void newNoteWithNote() throws Exception {
        String note = "Message";
        Note dateNote = new Note(note);
        assertThat(dateNote.getDate(), is(new Date()));
        assertThat(dateNote.getNote(), is(note));
        assertEquals((double) dateNote.getDate().getTime(), (double) new Date().getTime(), 1.0);
    }

    @Test
    public void formattedDate() throws Exception {
        String note = "Message";
        String dateAsString = "21:58 02.01.2017";
        Note dateNote = new Note(note, Note.DATE_TIME_FORMAT.parse(dateAsString));
        assertThat(dateNote.getFomattedDate(), is(dateAsString));
    }

}

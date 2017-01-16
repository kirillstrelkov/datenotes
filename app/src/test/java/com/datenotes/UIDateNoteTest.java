package com.datenotes;

import com.datenotes.data.UIDateNote;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class UIDateNoteTest {
    @Test
    public void newNoteWithDateAndNote() throws Exception {
        String note = "Message";
        Date date = new Date();
        UIDateNote dateNote = new UIDateNote(note, date);
        assertEquals(dateNote.getDate(), date);
        assertEquals(dateNote.getNote(), note);
    }

    @Test
    public void newNoteWithDateAndNoteAsString() throws Exception {
        String note = "Message";
        String formattedDate = "21:58 02.01.2017";
        UIDateNote dateNote = new UIDateNote(note, formattedDate);
        assertEquals(dateNote.getNote(), note);
        assertEquals(dateNote.getFomattedDate(), formattedDate);
    }

    @Test
    public void newNoteWithNote() throws Exception {
        String note = "Message";
        UIDateNote dateNote = new UIDateNote(note);
        assertEquals(dateNote.getDate(), new Date());
        assertEquals(dateNote.getNote(), note);
        assertEquals((double) dateNote.getDate().getTime(), (double) new Date().getTime(), 1.0);
    }

    @Test
    public void formattedDate() throws Exception {
        String note = "Message";
        String dateAsString = "21:58 02.01.2017";
        UIDateNote dateNote = new UIDateNote(note, UIDateNote.DATE_TIME_FORMAT.parse(dateAsString));
        assertEquals(dateNote.getFomattedDate(), dateAsString);
    }

}

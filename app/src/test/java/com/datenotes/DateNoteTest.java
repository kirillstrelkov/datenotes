package com.datenotes;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateNoteTest {
    @Test
    public void newNoteWithDateAndNote() throws Exception {
        String note = "Message";
        Date date = new Date();
        DateNote dateNote = new DateNote(note, date);
        assertEquals(dateNote.getDate(), date);
        assertEquals(dateNote.getNote(), note);
    }

    @Test
    public void newNoteWithDateAndNoteAsString() throws Exception {
        String note = "Message";
        String formattedDate = "21:58 02.01.2017";
        DateNote dateNote = new DateNote(note, formattedDate);
        assertEquals(dateNote.getNote(), note);
        assertEquals(dateNote.getFomattedDate(), formattedDate);
    }

    @Test
    public void newNoteWithNote() throws Exception {
        String note = "Message";
        DateNote dateNote = new DateNote(note);
        assertEquals(dateNote.getDate(), new Date());
        assertEquals(dateNote.getNote(), note);
        assertEquals((double) dateNote.getDate().getTime(), (double) new Date().getTime(), 1.0);
    }

    @Test
    public void formattedDate() throws Exception {
        String note = "Message";
        String dateAsString = "21:58 02.01.2017";
        DateNote dateNote = new DateNote(note, DateNote.DATE_TIME_FORMAT.parse(dateAsString));
        assertEquals(dateNote.getFomattedDate(), dateAsString);
    }

}

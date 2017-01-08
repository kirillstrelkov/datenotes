package com.datenotes;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class FileUtilsTest {
    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    private static DateNote NOTE1;
    private static DateNote NOTE2;

    @BeforeClass
    public static void init() throws ParseException {
        NOTE1 = new DateNote("Message", "01:01 01.01.2016");
        NOTE2 = new DateNote("Message\nMessage2", "12:12 11.11.2016");
    }

    @Test
    public void joinPaths() throws Exception {
        String path = FileUtils.joinPaths("/", "tmp");
        Assert.assertEquals(path, "/tmp");

        path = FileUtils.joinPaths("/tmp", "out.csv");
        Assert.assertEquals(path, "/tmp/out.csv");

        path = FileUtils.joinPaths("/", "tmp", "out.csv");
        Assert.assertEquals(path, "/tmp/out.csv");
    }

    @Test
    public void saveSingleDateNote() throws Exception {
        List<DateNote> notes = new LinkedList<>();
        notes.add(NOTE1);

        String path = FileUtils.joinPaths(new File(TMP_DIR).getPath(), "out.csv");
        FileUtils.save(notes, path);
    }

    @Test
    public void getsNotesFromFile() throws Exception {
        List<DateNote> notes = new LinkedList<>();
        notes.add(NOTE1);

        String path = FileUtils.joinPaths(new File(TMP_DIR).getPath(), "out.csv");
        FileUtils.save(notes, path);

        List<DateNote> readNotes = FileUtils.getNotesFromCSV(path);
        org.junit.Assert.assertThat(readNotes, is(notes));
    }

    @Test
    public void saveAndGetMultipleNotes() throws Exception {
        List<DateNote> notes = new LinkedList<>();
        notes.add(NOTE1);
        notes.add(NOTE2);

        String path = FileUtils.joinPaths(new File(TMP_DIR).getPath(), "out.csv");
        FileUtils.save(notes, path);

        List<DateNote> readNotes = FileUtils.getNotesFromCSV(path);
        org.junit.Assert.assertThat(readNotes, is(notes));
    }
}

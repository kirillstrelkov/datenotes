package com.datenotes.helpers;

import com.datenotes.data.Note;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileUtilsTest {
    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    private static Note NOTE1;
    private static Note NOTE2;

    @BeforeClass
    public static void init() throws ParseException {
        NOTE1 = new Note("Message", "01:01 01.01.2016");
        NOTE2 = new Note("Message\nMessage2", "12:12 11.11.2016");
    }

    @Test
    public void joinPaths() throws Exception {
        String path = FileUtils.joinPaths("/", "tmp");
        assertThat(path, is("/tmp"));

        path = FileUtils.joinPaths("/tmp", "out.csv");
        assertThat(path, is("/tmp/out.csv"));

        path = FileUtils.joinPaths("/", "tmp", "out.csv");
        assertThat(path, is("/tmp/out.csv"));
    }

    @Test
    public void saveSingleDateNote() throws Exception {
        List<Note> notes = new LinkedList<>();
        notes.add(NOTE1);

        String path = FileUtils.joinPaths(new File(TMP_DIR).getPath(), "out.csv");
        FileUtils.save(notes, path);
    }

    @Test
    public void getsNotesFromFile() throws Exception {
        List<Note> notes = new LinkedList<>();
        notes.add(NOTE1);

        String path = FileUtils.joinPaths(new File(TMP_DIR).getPath(), "out.csv");
        FileUtils.save(notes, path);

        List<Note> readNotes = FileUtils.getNotesFromCSV(path);
        assertThat(readNotes, is(notes));
    }

    @Test
    public void saveAndGetMultipleNotes() throws Exception {
        List<Note> notes = new LinkedList<>();
        notes.add(NOTE1);
        notes.add(NOTE2);

        String path = FileUtils.joinPaths(new File(TMP_DIR).getPath(), "out.csv");
        FileUtils.save(notes, path);

        List<Note> readNotes = FileUtils.getNotesFromCSV(path);
        assertThat(readNotes, is(notes));
    }
}

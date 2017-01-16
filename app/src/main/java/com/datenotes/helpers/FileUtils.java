package com.datenotes.helpers;

import com.datenotes.data.UIDateNote;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {
    public static final String MIME_TYPE_CSV = "text/csv";
    public static final String ENCODING = "UTF-8";
    public static final CSVFormat CSV_FORMAT = CSVFormat.RFC4180.withHeader("note", "date");

    public static String joinPaths(String... paths) {
        if (paths.length > 0) {
            String fullPath = paths[0];
            for (int i = 0; i < paths.length; i++) {
                if (i > 0) {
                    fullPath = new File(new File(fullPath), paths[i]).getPath();
                }
            }
            return fullPath;
        } else {
            throw new IllegalArgumentException("Incorrect number of arguments");
        }
    }

    public static void save(List<UIDateNote> notes, OutputStream out) throws IOException {
        Appendable appendable = new PrintWriter(new OutputStreamWriter(out, ENCODING));
        CSVPrinter printer = new CSVPrinter(appendable, CSV_FORMAT);
        for (UIDateNote note : notes) {
            printer.print(note.getNote());
            printer.print(note.getFomattedDate());
            printer.println();
        }
        printer.close();
    }

    public static void save(List<UIDateNote> notes, String path) throws IOException {
        save(notes, new FileOutputStream(path));
    }

    public static List<UIDateNote> getNotesFrom(InputStream in) throws IOException, ParseException {
        List<UIDateNote> notes = new LinkedList<>();

        CSVParser parser = new CSVParser(new InputStreamReader(in, ENCODING), CSV_FORMAT);
        for (CSVRecord csvRecord : parser) {
            if (csvRecord.getRecordNumber() > 1 && csvRecord.size() > 1) {
                notes.add(new UIDateNote(csvRecord.get("note"), csvRecord.get("date")));
            }
        }

        return notes;
    }

    public static List<UIDateNote> getNotesFromCSV(String path) throws IOException, ParseException {
        return getNotesFrom(new FileInputStream(path));
    }
}

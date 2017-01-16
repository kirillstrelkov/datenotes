package com.datenotes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.datenotes.data.UIDateNote;
import com.datenotes.db.NoteIO;
import com.datenotes.helpers.FileUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int IMPORT_REQUEST_CODE = 42;
    private static final int EXPORT_REQUEST_CODE = 43;

    private NoteIO noteIO;
    private ListView listView;
    private List<UIDateNote> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorPrimary));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNote.class);
                startActivityForResult(intent, AddNote.ADD_NOTE_ID);
            }
        });

        noteIO = NoteIO.create(this);
        notes = noteIO.getDataFromDb();
        listView = (ListView) findViewById(R.id.listView);
        NoteListAdapter adapter = new NoteListAdapter(this, R.layout.note_list_item, notes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), AddNote.class);
                UIDateNote note = ((NoteListAdapter) adapterView.getAdapter()).getItem(i);
                intent.putExtra(UIDateNote.KEY_DATE, note.getFomattedDate());
                intent.putExtra(UIDateNote.KEY_NOTE, note.getNote());
                intent.putExtra(UIDateNote.KEY_ID, note.getId());
                startActivityForResult(intent, AddNote.UPDATE_NOTE_ID);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_import:
                createImportActivity();
                break;
            case R.id.action_export:
                createExportActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();

            switch (requestCode) {
                case AddNote.ADD_NOTE_ID:
                case AddNote.UPDATE_NOTE_ID:
                    boolean toDelete = data.getBooleanExtra(UIDateNote.KEY_DELETE, false);
                    String msg = data.getStringExtra(UIDateNote.KEY_NOTE);
                    String date = data.getStringExtra(UIDateNote.KEY_DATE);
                    long id = data.getLongExtra(UIDateNote.KEY_ID, UIDateNote.DEFAULT_ID);
                    UIDateNote note = null;
                    try {
                        note = new UIDateNote(msg, date, id);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (note != null) {
                        if (toDelete) {
                            deleteNote(note);
                        } else {
                            saveNote(note, true);
                        }
                    }
                    break;
                case IMPORT_REQUEST_CODE:
                    importNotes(uri);
                    break;
                case EXPORT_REQUEST_CODE:
                    exportNotes(uri);
                    break;
            }
        }
    }

    private void importNotes(Uri uri) {
        try {
            List<UIDateNote> importedNotes = FileUtils.getNotesFrom(getContentResolver().openInputStream(uri));
            String msg = String.format(getString(R.string.msg_notes_imported), importedNotes.size());
            long importedCount = 0;
            for (UIDateNote n : importedNotes) {
                if (saveNote(n, false)) {
                    importedCount++;
                }
            }
            showToast(String.format(getString(R.string.msg_notes_imported), importedCount));
        } catch (Exception e) {
            e.printStackTrace();
            showToast(getString(R.string.msg_failed_import));
        }
    }

    private void exportNotes(Uri uri) {
        String msg = String.format(getString(R.string.msg_notes_exported), notes.size());
        try {
            FileUtils.save(notes, getContentResolver().openOutputStream(uri));
            showToast(msg);
        } catch (IOException e) {
            e.printStackTrace();
            showToast(getString(R.string.msg_failed_export));
        }
    }

    private void deleteNote(UIDateNote note) {
        showToast(String.format(getString(R.string.msg_note_removed), note.getNote(), note.getFomattedDate()));
        noteIO.deleteNote(note);
        notes.remove(note);
        reloadData();
    }

    private boolean saveNote(UIDateNote newNote, boolean showMsg) {
        if (notes.contains(newNote)) {
            showToast(String.format(getString(R.string.msg_note_duplicate), newNote.getNote(), newNote.getFomattedDate()));
            return false;
        }
        boolean is_update = newNote.getId() != UIDateNote.DEFAULT_ID;
        if (showMsg) {
            String msg;
            if (is_update) {
                msg = String.format(getString(R.string.msg_note_updated), newNote.getNote(), newNote.getFomattedDate());
            } else {
                msg = String.format(getString(R.string.msg_note_added), newNote.getNote(), newNote.getFomattedDate());
            }
            showToast(msg);
        }

        if (is_update) {
            for (UIDateNote n : notes) {
                if (n.getId() == newNote.getId()) {
                    n.setNote(newNote.getNote());
                    n.setDate(newNote.getDate());
                    noteIO.updateNoteToDb(n);
                    break;
                }
            }
        } else {
            notes.add(0, newNote);
            noteIO.addNoteToDb(newNote);
        }
        reloadData();
        return true;
    }

    private void createExportActivity() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType(FileUtils.MIME_TYPE_CSV);
        startActivityForResult(Intent.createChooser(intent, "Create csv file to export"), EXPORT_REQUEST_CODE);
    }

    private void createImportActivity() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(FileUtils.MIME_TYPE_CSV);
        startActivityForResult(Intent.createChooser(intent, "Select csv file to import"), IMPORT_REQUEST_CODE);
    }

    private void reloadData() {
        ((NoteListAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

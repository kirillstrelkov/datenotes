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

import com.datenotes.adapters.NoteListAdapter;
import com.datenotes.data.List;
import com.datenotes.data.Note;
import com.datenotes.data.NoteDao;
import com.datenotes.helpers.FileUtils;

import java.io.IOException;

public class NotesActivity extends AppCompatActivity {
    private static final int IMPORT_REQUEST_CODE = 42;
    private static final int EXPORT_REQUEST_CODE = 43;

    private ListView listView;
    private java.util.List notes;
    private NoteDao noteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorPrimary));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNoteActivity.class);
                startActivityForResult(intent, AddNoteActivity.ADD_NOTE_ID);
            }
        });

        noteDao = ((App) getApplication()).getDaoSession().getNoteDao();

        Intent intent = getIntent();
        List list = intent.getParcelableExtra(List.KEY);

        notes = list.getNotes();
        listView = (ListView) findViewById(R.id.listView);
        NoteListAdapter adapter = new NoteListAdapter(this, R.layout.note_list_item, this.notes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), AddNoteActivity.class);

                Note note = ((NoteListAdapter) adapterView.getAdapter()).getItem(i);
                intent.putExtra(Note.KEY, note);

                startActivityForResult(intent, AddNoteActivity.UPDATE_NOTE_ID);
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
                case AddNoteActivity.ADD_NOTE_ID:
                case AddNoteActivity.UPDATE_NOTE_ID:
                    Note note = data.getParcelableExtra(Note.KEY);
                    boolean toDelete = data.getBooleanExtra(Note.KEY_DELETE, false);
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
            java.util.List<Note> importedNotes = FileUtils.getNotesFrom(getContentResolver().openInputStream(uri));
            long importedCount = 0;
            for (Note n : importedNotes) {
                if (saveNote(n, false)) {
                    importedCount++;
                }
            }
            String msg = String.format(getString(R.string.msg_notes_imported), importedCount);
            showToast(msg);
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

    private void deleteNote(Note note) {
        showToast(String.format(getString(R.string.msg_note_removed), note.getNote(), note.getFomattedDate()));
        noteDao.delete(note);
        notes.remove(note);
        reloadData();
    }

    private boolean saveNote(Note newNote, boolean showMsg) {
        if (notes.contains(newNote)) {
            showToast(String.format(getString(R.string.msg_note_duplicate), newNote.getNote(), newNote.getFomattedDate()));
            return false;
        }
        boolean is_update = newNote.getId() != Note.DEFAULT_ID;
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
            noteDao.update(newNote);
        } else {
            notes.add(0, newNote);
            noteDao.insert(newNote);
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

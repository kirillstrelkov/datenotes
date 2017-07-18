package com.datenotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.datenotes.adapters.NotesAdapter;
import com.datenotes.data.DaoSession;
import com.datenotes.data.List;
import com.datenotes.data.Note;
import com.datenotes.helpers.FileUtils;

import java.io.IOException;

public class ListWithNotesActivity extends AppCompatActivity {
    private static final int IMPORT_REQUEST_CODE = 42;
    private static final int EXPORT_REQUEST_CODE = 43;

    private ListView listView;
    private List list;
    private int initialListHash;
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Intent intent = getIntent();
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        list = intent.getParcelableExtra(List.KEY);
        list.__setDaoSession(daoSession);
        initialListHash = list.hashCode();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notes);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
        supportActionBar.setTitle(list.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NoteActivity.class);
                intent.putExtra(List.KEY, list);
                startActivityForResult(intent, NoteActivity.ADD_NOTE_ID);
            }
        });

        java.util.List<Note> notes = list.getNotes();
        listView = (ListView) findViewById(R.id.listView);
        notesAdapter = new NotesAdapter(this, R.layout.note_list_item, notes);
        listView.setAdapter(notesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), NoteActivity.class);

                Note note = ((NotesAdapter) adapterView.getAdapter()).getItem(i);
                intent.putExtra(Note.KEY, note);

                startActivityForResult(intent, NoteActivity.UPDATE_NOTE_ID);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_list_import:
                createImportActivity();
                break;
            case R.id.action_list_export:
                createExportActivity();
                break;
            case R.id.action_list_delete:
                deleteList();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();

            switch (requestCode) {
                case NoteActivity.ADD_NOTE_ID:
                case NoteActivity.UPDATE_NOTE_ID:
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void importNotes(Uri uri) {
        try {
            java.util.List<Note> importedNotes = FileUtils.getNotesFrom(getContentResolver().openInputStream(uri));
            long importedCount = 0;
            for (Note n : importedNotes) {
                n.setListId(list.getId());
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
        java.util.List<Note> notes = list.getNotes();
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
        list.removeNote(note);
        reloadData();
    }

    private boolean saveNote(Note note, boolean showMsg) {
        java.util.List<Note> notes = list.getNotes();
        if (notes.contains(note)) {
            showToast(String.format(getString(R.string.msg_note_duplicate), note.getNote(), note.getFomattedDate()));
            return false;
        }
        boolean is_update = note.getId() != Note.DEFAULT_ID;
        if (showMsg) {
            String msg;
            if (is_update) {
                msg = String.format(getString(R.string.msg_note_updated), note.getNote(), note.getFomattedDate());
            } else {
                msg = String.format(getString(R.string.msg_note_added), note.getNote(), note.getFomattedDate());
            }
            showToast(msg);
        }

        if (is_update) {
            list.updateNote(note);
        } else {
            list.addNote(note);
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
        notesAdapter.updateAdapter(list.getNotes());
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private Intent getParceableIntent() {
        Intent intent = new Intent();
        intent.putExtra(List.KEY, list);
        return intent;
    }

    private void deleteList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                list.delete();
                finish();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setMessage(R.string.dialog_delete_list_msg).setTitle(R.string.dialog_delete_title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (initialListHash != list.hashCode()) {
            setResult(RESULT_OK, getParceableIntent());
        }
        super.onBackPressed();
    }
}

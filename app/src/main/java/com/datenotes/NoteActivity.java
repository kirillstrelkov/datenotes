package com.datenotes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.datenotes.data.List;
import com.datenotes.data.Note;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {
    public static final int ADD_NOTE_ID = 1;
    public static final int UPDATE_NOTE_ID = 2;
    private Calendar calendar;
    private EditText textDate;
    private EditText textNote;
    private List list;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_note));
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);

        textDate = (EditText) findViewById(R.id.textDate);
        textNote = (EditText) findViewById(R.id.textNote);
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tpd = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        setTime(hour, minutes);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                tpd.show();

                DatePickerDialog dpd = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        setDate(year, month, day);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        setDateToField();

        Intent intent = getIntent();
        note = intent.getParcelableExtra(Note.KEY);
        list = intent.getParcelableExtra(List.KEY);
        if (note != null) {
            textDate.setText(note.getFomattedDate());
            textNote.setText(note.getNote());
        }
    }

    private void setTime(int hours, int minutes) {
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        setDateToField();
    }

    private void setDateToField() {
        textDate.setText(Note.DATE_TIME_FORMAT.format(calendar.getTime()));
    }

    private void setDate(int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        setDateToField();
    }

    protected void deleteNote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = getDateNoteIntent();
                intent.putExtra(Note.KEY_DELETE, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                goBack();
            }
        });
        builder.setMessage(R.string.dialog_delete_note_msg).setTitle(R.string.dialog_delete_title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void goBack() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        if (note != null) {
            menu.findItem(R.id.action_note_add).setTitle(R.string.update_note);
            menu.findItem(R.id.action_note_delete).setVisible(true);
        }
        this.invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_note_add:
                addNewNote();
                break;
            case R.id.action_note_delete:
                deleteNote();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        onBackPressed();
        return true;
    }

    protected void addNewNote() {
        Intent intent = getDateNoteIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private Intent getDateNoteIntent() {
        Intent intent = new Intent();
        try {
            if (note == null) {
                note = new Note(
                        null,
                        textNote.getText().toString(),
                        textDate.getText().toString(),
                        list.getId()
                );
            } else {
                note.setDate(textDate.getText().toString());
                note.setNote(textNote.getText().toString());
            }
            intent.putExtra(Note.KEY, note);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return intent;
    }
}
